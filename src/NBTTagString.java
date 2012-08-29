import java.io.DataOutputStream;
import java.io.IOException;

public class NBTTagString extends NBTTag{
    private String payload;

    public NBTTagString(){
        super(8, "String");
    }

    public String getValue(){
        return payload;
    }

    public void read(NBTReader reader) throws IOException{
        payload = reader.readString();
    }

    public void write(DataOutputStream stream, boolean list) throws IOException{
        super.write(stream, list);
        stream.writeShort(payload.length());
        stream.writeBytes(payload);
    }

    public void setValue(String value){
        payload = value.length() > 0 ? value : "Empty";
    }
}