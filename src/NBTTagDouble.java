import java.io.DataOutputStream;
import java.io.IOException;

public class NBTTagDouble extends NBTTag{
    private double payload;

    public NBTTagDouble(){
        super(6, "Double");
    }

    public String getValue(){
        return ""+payload+"D";
    }

    public void read(NBTReader reader) throws IOException{
        payload = reader.getInputStream().readDouble();
    }

    public void write(DataOutputStream stream, boolean list) throws IOException{
        super.write(stream, list);
        stream.writeDouble(payload);
    }

    public void setValue(String value){
        payload = value.length() > 0 ? Byte.parseByte(value) : 0D;
    }
}