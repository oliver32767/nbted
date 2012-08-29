import java.io.DataOutputStream;
import java.io.IOException;

public class NBTTagFloat extends NBTTag{
    private float payload;

    public NBTTagFloat(){
        super(5, "Float");
    }

    public String getValue(){
        return ""+payload+"F";
    }

    public void read(NBTReader reader) throws IOException{
        payload = reader.getInputStream().readFloat();
    }

    public void write(DataOutputStream stream, boolean list) throws IOException{
        super.write(stream, list);
        stream.writeFloat(payload);
    }

    public void setValue(String value){
        payload = value.length() > 0 ? Float.parseFloat(value) : 0F;
    }
}