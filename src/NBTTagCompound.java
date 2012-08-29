import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class NBTTagCompound extends NBTTag{
    private ArrayList<NBTTag> tags;

    public NBTTagCompound(){
        super(10, "Compound");
        tags = new ArrayList<NBTTag>();
        canBeSet = false;
    }

    public String getValue(){
        return "";
    }

    public void read(NBTReader reader) throws IOException{
        reader.dumpTag2(this);
        int currentLevel = reader.getLevel();
        reader.increaseLevel();
        do{
            if (reader.getLevel() <= currentLevel){
                return;
            }else{
                NBTTag nextTag = reader.readNextTag();
                if (nextTag == null){
                    return;
                }
                if (nextTag.getTypeId() > 0){
                    tags.add(nextTag);
                }
            }
        }while (true);
    }

    public void write(DataOutputStream stream, boolean list) throws IOException{
        super.write(stream, list);
        for (int i = 0; i < tags.size(); i++){
            tags.get(i).write(stream, false);
        }
        (new NBTTagEnd()).write(stream, false);
    }

    public void setValue(String value){}

    public NBTTag getTagByName(String str){
        for (int i = 0; i < tags.size(); i++){
            if (tags.get(i).getName().equals(str)){
                return tags.get(i);
            }
        }
        return null;
    }

    public boolean hasTag(String str){
        return getTagByName(str) != null;
    }

    public void addTag(NBTTag tag){
        tags.add(tag);
    }

    public void removeTag(String str){
        removeTag(getTagByName(str));
    }

    public void removeTag(NBTTag tag){
        tags.remove(tag);
    }
}