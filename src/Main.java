import java.io.File;

public class Main{
    public static void main(String[] args){
        NBTReader reader = new NBTReader(new File(args[0]));
        for (int i = 0; i < args.length; i++){
            if (isCommand(args[i], "-h", "--help")){
                help();
                return;
            }
            if (isCommand(args[i], "-l", "--list")){
                list(reader);
                return;
            }
            if (isCommand(args[i], "-g", "--get")){
                if (args.length < i + 2 || args[i + 1].startsWith("-")){
                    incorrect();
                    return;
                }
                boolean verbose = false;
                for (int j = i + 2; j < args.length; j++){
                    if (isCommand(args[j], "-v", "--verbose")){
                        verbose = true;
                    }
                }
                get(reader, args[i + 1], verbose);
                return;
            }
            if (isCommand(args[i], "-a", "--add")){
                if (args.length < i + 3 || args[i + 1].startsWith("-") || args[i + 2].startsWith("-")){
                    incorrect();
                    return;
                }
                boolean canBeSet = NBTTag.createTagById(parseTag(args[i + 2])).getCanBeSet();
                String value = "";
                if (canBeSet && !(args.length < i + 4 || args[i + 3].startsWith("-"))){
                    value = args[i + 3];
                }
                add(reader, args[0], args[i + 1], parseTag(args[i + 2]), value);
                return;
            }
            if (isCommand(args[i], "-m", "--move")){
                if (args.length < i + 3 || args[i + 1].startsWith("-") || args[i + 2].startsWith("-")){
                    incorrect();
                    return;
                }
                move(reader, args[0], args[i + 1], args[i + 2]);
                return;
            }
            if (isCommand(args[i],"-r", "--remove")){
                if (args.length < i + 2 || args[i + 1].startsWith("-")){
                    incorrect();
                    return;
                }
                remove(reader, args[0], args[i + 1]);
                return;
            }
            if (isCommand(args[i], "-s", "--set")){
                if (args.length < i + 3 || args[i + 1].startsWith("-") || args[i + 2].startsWith("-")){
                    incorrect();
                    return;
                }
                set(reader, args[0], args[i + 1], args[i + 2]);
                return;
            }
        }
        help();
    }

    public static void list(NBTReader reader){
        reader.enableOutput();
        reader.readNextTag();
    }

    public static void get(NBTReader reader, String tagPath, boolean verbose){
        reader.setUntil(tagPath);
        reader.readNextTag();
        NBTTag tag = reader.getFinalTag();
        if (tag == null){
            System.out.println("No such tag!");
            return;
        }
        if (!verbose){
            System.out.println(tag.getValue());
            return;
        }
        System.out.println("Type: "+tag.getTypeName());
        System.out.println("Name: "+tag.getName());
        System.out.println("Data: "+tag.getValue());
    }

    public static void set(NBTReader reader, String filename, String tagPath, String value){
        NBTTag mainTag = reader.readNextTag();
        NBTTag tag = getTagFromPath(tagPath, mainTag);
        tag.setValue(value);
        NBTWriter writer = new NBTWriter(filename, mainTag, 1);
    }

    public static void add(NBTReader reader, String filename, String tagPath, int type, String value){
        NBTTag mainTag = reader.readNextTag();
        NBTTag parentTag = getParentTagFromPath(tagPath, mainTag);
        if (!(parentTag instanceof NBTTagCompound)){
            System.out.println("Can't add there.");
            return;
        }
        NBTTagCompound tag = (NBTTagCompound)parentTag;
        if (tag.hasTag(getNameFromPath(tagPath))){
            System.out.println("Already exists.");
            return;
        }
        NBTTag newTag = NBTTag.createTagById(type);
        newTag.setName(getNameFromPath(tagPath));
        newTag.setValue(value);
        tag.addTag(newTag);
        NBTWriter writer = new NBTWriter(filename, mainTag, 1);
    }

    public static void remove(NBTReader reader, String filename, String tagPath){
        NBTTag mainTag = reader.readNextTag();
        NBTTagCompound tag = ((NBTTagCompound)getParentTagFromPath(tagPath, mainTag));
        if (tag == null){
            System.out.println("No such tag!");
            return;
        }
        tag.removeTag(getNameFromPath(tagPath));
        NBTWriter writer = new NBTWriter(filename, mainTag, 1);
    }

    public static void move(NBTReader reader, String filename, String oldPath, String newPath){
        NBTTag mainTag = reader.readNextTag();
        NBTTag tag = getTagFromPath(oldPath, mainTag);
        if (tag == null){
            System.out.println("No such tag!");
            return;
        }
        NBTTag newParent = getParentTagFromPath(newPath, mainTag);
        if (newParent == null || !(newParent instanceof NBTTagCompound)){
            System.out.println("Incorrect path.");
            return;
        }
        if (((NBTTagCompound)newParent).getTagByName(getNameFromPath(newPath)) != null){
            System.out.println("Tag with this name already exists.");
            return;
        }
        ((NBTTagCompound)getParentTagFromPath(oldPath, mainTag)).removeTag(tag);
        ((NBTTagCompound)newParent).addTag(tag);
        tag.setName(getNameFromPath(newPath));
        NBTWriter writer = new NBTWriter(filename, mainTag, 1);
    }

    public static void incorrect(){
        System.out.println("Wrong usage! Use -h to get list of commands.");
    }

    public static void help(){
        String[] strs = new String[]{
            "java -jar nbted.jar FILE OPTIONS",
            " -l, --list                      Prints all tags in the NBT file.",
            " -g, --get [TAG]                 Prints value of selected tag.",
            "  -v, --verbose                   Prints also name and type of the tag.",
            " -a, --add [TAG] [TYPE] <VALUE>  Adds tag of selected type and sets it.",
            " -r, --remove [TAG]              Removes selected tag.",
            " -s, --set [TAG] [VALUE]         Sets value of selected tag.",
            " -m, --move [TAG] [PATH]         Changes path or name of selected tag.",
            " -h, --help                      Prints this.",
            "",
            "Tag types:",
            " 0,  TAG_End, end                     The end of compound tag.",
            " 1,  TAG_Byte, byte                   8-bit number. Possible values: -127–128.",
            " 2,  TAG_Short, short                 16-bit number. Possible values: -32768–32767.",
            " 3,  TAG_Int, int                     32-bit number. Possible values: -2147483648–2147483647.",
            " 4,  TAG_Long, long                   64-bit number. Possible values: -9223372036854775808–9223372036854775807.",
            " 5,  TAG_Float, float                 32-bit floating point number.",
            " 6,  TAG_Double, double               64-bit floating point number.",
            " 7,  TAG_Byte_Array, byte_array       Array of bytes.",
            " 8,  TAG_String, string               UTF-8 string.",
            " 9,  TAG_List, list                   Contains other tags as list. Tags inside don't have type and name.",
            " 10, TAG_Compound, compound           Contains other tags. May be desribed as folder.",
            " 11, TAG_Int_Array, int_array         Array of integers.",
            "",
            "Possible values:",
            " Byte, short, int, long, float and double: A number.",
            " Byte array and int array: Length as int number.",
            " String: A string.",
            " List: Type of tags inside. Modifying tags inside is not allowed for now.",
            " Compound: No value. Tags inside are modified directly using absolute path.",
            " End: No value. Created automatically when compound is created."};
        for (String str : strs){
            System.out.println(str);
        }
    }

    public static NBTTag getTagFromPath(String s, NBTTag mainTag){
        String[] path2 = s.split("/");
        NBTTag tag = mainTag;
        for (int i = 0; i < path2.length; i++){
            tag = ((NBTTagCompound)tag).getTagByName(path2[i]);
        }
        return tag;
    }

    public static NBTTag getParentTagFromPath(String s, NBTTag mainTag){
        String[] path2 = s.split("/");
        NBTTag tag = mainTag;
        for (int i = 0; i < path2.length - 1; i++){
            tag = ((NBTTagCompound)tag).getTagByName(path2[i]);
        }
        return tag;
    }

    public static String getNameFromPath(String s){
        String[] path2 = s.split("/");
        return path2[path2.length - 1];
    }

    public static boolean isCommand(String str1, String... str2){
        for (int i = 0; i < str2.length; i++){
            if (str1.equals(str2[i])){
                return true;
            }
        }
        return false;
    }

    public static int parseTag(String str){
        str = str.toLowerCase();
        for (int i = 0; i < 12; i++){
            NBTTag tag = NBTTag.createTagById(i);
            if (str.equals(tag.getTypeName().toLowerCase())){
                return i;
            }
            if (str.equals(tag.getTypeName().replace("TAG_", "").toLowerCase())){
                return i;
            }
        }
        try{
            return Integer.parseInt(str);
        }catch(NumberFormatException e){
            System.out.println("No such tag type!");
            System.exit(0);
            return -1;
        }
    }
}