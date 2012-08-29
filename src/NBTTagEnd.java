import java.io.DataOutputStream;
import java.io.IOException;

public class NBTTagEnd extends NBTTag{
    public NBTTagEnd(){
        super(0, "End");
        hasName = false;
        canBeSet = false;
    }

    public String getValue(){
        return "";
    }

    public void read(NBTReader reader) throws IOException{
        reader.decreaseLevel();
    }

    public void setValue(String value){}
}