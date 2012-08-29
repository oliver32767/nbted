import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public abstract class NBTTag{
    protected int typeId;
    protected String typeName;
    protected boolean hasName;
    protected boolean canBeSet;

    protected String name;

    public NBTTag(int typeId, String typeName){
        this.typeId = typeId;
        this.typeName = "TAG_"+typeName;
        hasName = true;
        canBeSet = true;
        name = "";
    }

    public String getTypeName(){
        return typeName;
    }

    public int getTypeId(){
        return typeId;
    }

    public String getName(){
        return name;
    }

    public boolean getHasName(){
        return hasName;
    }

    public void setName(String name){
        this.name = name;
    }

    public boolean getCanBeSet(){
        return canBeSet;
    }

    public abstract String getValue();

    public abstract void read(NBTReader reader) throws IOException;

    public void write(DataOutputStream stream, boolean list) throws IOException{
        if (!list){
            stream.writeByte(typeId);
            if (hasName && name.length() > 0){
                stream.writeShort(name.length());
                stream.writeBytes(name);
            }
        }
    }

    public abstract void setValue(String value);

    public String getString(){
        return typeName+(getHasName()&& name.length() > 0 ? ("('"+name+"') ") : " ")+getValue();
    }

    public static NBTTag createTagById(int id){
        if (id == 0){
            return new NBTTagEnd();
        }
        if (id == 1){
            return new NBTTagByte();
        }
        if (id == 2){
            return new NBTTagShort();
        }
        if (id == 3){
            return new NBTTagInt();
        }
        if (id == 4){
            return new NBTTagLong();
        }
        if (id == 5){
            return new NBTTagFloat();
        }
        if (id == 6){
            return new NBTTagDouble();
        }
        if (id == 7){
            return new NBTTagByteArray();
        }
        if (id == 8){
            return new NBTTagString();
        }
        if (id == 9){
            return new NBTTagList();
        }
        if (id == 10){
            return new NBTTagCompound();
        }
        if (id == 11){
            return new NBTTagIntArray();
        }
        return null;
    }
}