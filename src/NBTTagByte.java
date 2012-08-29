import java.io.DataOutputStream;
import java.io.IOException;

public class NBTTagByte extends NBTTag{
    private byte payload;

    public NBTTagByte(){
        super(1, "Byte");
    }

    public String getValue(){
        return ""+payload;
    }

    public void read(NBTReader reader) throws IOException{
        payload = reader.getInputStream().readByte();
    }

    public void write(DataOutputStream stream, boolean list) throws IOException{
        super.write(stream, list);
        stream.writeByte(payload);
    }

    public void setValue(String value){
        payload = value.length() > 0 ? Byte.parseByte(value) : 0;
    }
}