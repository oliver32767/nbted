import java.io.DataOutputStream;
import java.io.IOException;

public class NBTTagShort extends NBTTag{
    private int payload;

    public NBTTagShort(){
        super(2, "Short");
    }

    public String getValue(){
        return ""+payload;
    }

    public void read(NBTReader reader) throws IOException{
        payload = reader.getInputStream().readShort();
    }

    public void write(DataOutputStream stream, boolean list) throws IOException{
        super.write(stream, list);
        stream.writeShort(payload);
    }

    public void setValue(String value){
        payload = value.length() > 0 ? Short.parseShort(value) : 0;
    }
}