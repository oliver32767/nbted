import java.io.DataOutputStream;
import java.io.IOException;

public class NBTTagIntArray extends NBTTag{
    private int[] payload;

    public NBTTagIntArray(){
        super(11, "Int_Array");
    }

    public String getValue(){
        StringBuilder output = new StringBuilder();
        output.append(payload.length);
        output.append(" items: ");
        for (int i = 0; i < Math.min(payload.length, 10); i++){
            output.append(payload[i]);
            output.append(", ");
        }
        output.append("...");
        return output.toString();
    }

    public void read(NBTReader reader) throws IOException{
        int length = reader.getInputStream().readInt();
        payload = new int[length];
        for (int i = 0; i < length; i++){
            payload[i] = reader.getInputStream().readInt();
        }
    }

    public void write(DataOutputStream stream, boolean list) throws IOException{
        super.write(stream, list);
        stream.writeInt(payload.length);
        for (int i = 0; i < payload.length; i++){
            stream.writeInt(payload[i]);
        }
    }

    public void setValue(String value){
        payload = new int[value.length() > 0 ? Integer.parseInt(value) : 100];
    }
}