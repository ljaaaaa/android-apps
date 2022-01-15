package lilja.kiiski.doitlist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/* MYFILEHANDLER CLASS
- Basic file handler to make filehandling simpler
- Reads and writes to file
- Helper tool
 */
public class MyFileHandler {
    public final File FILE;

    public MyFileHandler(File file){
        FILE = file;
    }

    public String readFile(){
        String text = "";

        try {
            InputStream is = new FileInputStream(FILE);
            StringBuilder stringBuilder = new StringBuilder();

            if (is != null) {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader bf = new BufferedReader(isr);
                String receiveString = "";

                while ((receiveString = bf.readLine()) != null){
                    stringBuilder.append(receiveString);
                }

                is.close();
                text = stringBuilder.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return text;
    }

    public void writeFile(String text){
        try {
            FileOutputStream fos = new FileOutputStream(FILE);
            fos.write(text.getBytes());

            fos.flush();
            fos.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}