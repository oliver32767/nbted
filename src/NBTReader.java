import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class NBTReader{
    private File nbtFile;
    private DataInputStream inputStream;
    private int level;
    private String until;
    private boolean finished;
    private boolean output;
    private int compression;
    private NBTTag finalTag;

    public NBTReader(File f){
        level = 0;
        nbtFile = f;
        until = "";
        finished = false;
        compression = 1;
        if (!createStream()){
            return;
        }
        output = false;
    }

    private boolean createStream(){
        try{
            FileInputStream fileInputStream = new FileInputStream(nbtFile);
            if (compression <= 0){
                inputStream = new DataInputStream(fileInputStream);
                if (inputStream.read() == 0){
                    compression = 2;
                    return createStream();
                }
//                 inputStream.close();
                inputStream = new DataInputStream(fileInputStream);
            }
            if (compression == 1){
                GZIPInputStream gzipInputStream = new GZIPInputStream(fileInputStream);
                inputStream = new DataInputStream(gzipInputStream);
            }
            if (compression >= 2){
                System.out.println("ZLib decompression is not implemented yet.");
                System.exit(0);
            }
        }catch(IOException ex){
            if (compression >= 1){
                compression = 0;
                return createStream();
            }
            System.out.println("Error when creating input stream.");
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public NBTTag readNextTag(){
        try{
            return readNextTag(true, inputStream.read());
        }catch(IOException ex){
            System.out.println("Error when reading tags.");
            ex.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    public NBTTag readNextTag(boolean hasName, int type){
        if (finished){
            return null;
        }
        try{
            NBTTag nextTag = NBTTag.createTagById(type);
            if (nextTag.getHasName() && hasName){
                nextTag.setName(readString());
                if (nextTag.getName().equals(until) && until.length() > 0){
                    finishReading();
                    finalTag = nextTag;
                }
            }
            nextTag.read(this);
            dumpTag(nextTag);
            return nextTag;
        }catch(IOException ex){
            System.out.println("Error when reading tags.");
            ex.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    public DataInputStream getInputStream(){
        return inputStream;
    }

    public String readString() throws IOException{
        int length = inputStream.readUnsignedShort();
        byte[] nameBytes = new byte[length];
        for (int i = 0; i < length; i++){
             nameBytes[i] = inputStream.readByte();
        }
        return new String(nameBytes, "UTF-8");
    }

    public int getLevel(){
        return level;
    }

    public void increaseLevel(){
        level++;
    }

    public void decreaseLevel(){
        level--;
    }

    public void dumpTag(NBTTag tag){
        if (tag.getTypeId() == (new NBTTagCompound()).getTypeId() || tag.getTypeId() == (new NBTTagList()).getTypeId()){
            return;
        }
        dumpTag2(tag);
    }

    public void dumpTag2(NBTTag tag){
        if (!output){
            return;
        }
        for(int i = 0; i < level; i++){
            System.out.print("    ");
        }
        System.out.println(tag.getString());
    }

    public void finishReading(){
        finished = true;
    }

    public void enableOutput(){
        output = true;
    }

    public void setUntil(String until){
        this.until = until;
    }

    public NBTTag getFinalTag(){
        return finalTag;
    }

    public int getCompression(){
        return compression;
    }
}