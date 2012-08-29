import java.io.DataOutputStream;
import java.io.IOException;

public class NBTTagLong extends NBTTag{
    private long payload;

    public NBTTagLong(){
        super(4, "Long");
    }

    public String getValue(){
        return ""+payload+"L";
    }

    public void read(NBTReader reader) throws IOException{
        payload = reader.getInputStream().readLong();
    }

    public void write(DataOutputStream stream, boolean list) throws IOException{
        super.write(stream, list);
        stream.writeLong(payload);
    }

    public void setValue(String value){
        payload = value.length() > 0 ? Long.parseLong(value) : 0L;
    }
}