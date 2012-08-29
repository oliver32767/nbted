import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class NBTWriter{
    private File nbtFile;
    private NBTTag baseTag;
    private DataOutputStream outputStream;

    public NBTWriter(String str, NBTTag tag, int compression){
        baseTag = tag;
        if (!createFile(str)){
            return;
        }
        if (!createStream(compression)){
            return;
        }
        writeTag(baseTag);
        try{
            outputStream.close();
        }catch(IOException ex){
            System.out.println("Error when closing stream");
            ex.printStackTrace();
        }
    }

    private boolean createFile(String str){
        try{
            nbtFile = new File(str);
            nbtFile.createNewFile();
        }catch(IOException ex){
            System.out.println("Error when creating file.");
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean createStream(int compressing){
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(nbtFile);
            if (compressing <= 0){
                outputStream = new DataOutputStream(fileOutputStream);
            }
            if (compressing == 1){
                GZIPOutputStream gzipOutputStream = new GZIPOutputStream(fileOutputStream);
                outputStream = new DataOutputStream(gzipOutputStream);
            }
            if (compressing >= 2){
                System.out.println("ZLib decompression is not implemented yet.");
                System.exit(0);
            }
        }catch(IOException ex){
            System.out.println("Error when creating output stream.");
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    private void writeTag(NBTTag tag){
        try{
            tag.write(outputStream, false);
        }catch(IOException ex){
            System.out.println("Error when writing tag.");
            ex.printStackTrace();
        }
    }
}