
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class JSInterface {
    public void log(String log){
        System.out.println(log);
    }

    public static String readcode(String file){
        try {
            FileInputStream fin=new FileInputStream("./"+file);
            byte[] bys=new byte[fin.available()];
            fin.read(bys);
            fin.close();
            return new String(bys,"utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
