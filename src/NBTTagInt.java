import java.io.DataOutputStream;
import java.io.IOException;

public class NBTTagInt extends NBTTag{
    private int payload;

    public NBTTagInt(){
        super(3, "Int");
    }

    public String getValue(){
        return ""+payload;
    }

    public void read(NBTReader reader) throws IOException{
        payload = reader.getInputStream().readInt();
    }

    public void write(DataOutputStream stream, boolean list) throws IOException{
        super.write(stream, list);
        stream.writeInt(payload);
    }

    public void setValue(String value){
        payload = value.length() > 0 ? Integer.parseInt(value) : 0;
    }
}