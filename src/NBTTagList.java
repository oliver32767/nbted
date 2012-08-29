import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class NBTTagList extends NBTTag{
    private ArrayList<NBTTag> tags;
    private int listType;
    private int length;

    public NBTTagList(){
        super(9, "List");
        tags = new ArrayList<NBTTag>();
        listType = 0;
        length = 0;
        canBeSet = false;
    }

    public String getValue(){
        return "("+NBTTag.createTagById(listType).getTypeName()+")";
    }

    public void read(NBTReader reader) throws IOException{
        tags.clear();
        listType = reader.getInputStream().readByte();
        length = reader.getInputStream().readInt();
        reader.dumpTag2(this);
        reader.increaseLevel();
        for (int i = 0; i < length; i++){
            NBTTag nextTag = reader.readNextTag(false, listType);
            if (nextTag == null){
                return;
            }
            tags.add(nextTag);
        }
        reader.decreaseLevel();
    }

    public void write(DataOutputStream stream, boolean list) throws IOException{
        super.write(stream, list);
        stream.writeByte(listType);
        stream.writeInt(tags.size());
        for (int i = 0; i < tags.size(); i++){
            tags.get(i).write(stream, true);
        }
    }

    public void setValue(String value){}

    public NBTTag getTagByNum(int num){
        return tags.get(num);
    }
}