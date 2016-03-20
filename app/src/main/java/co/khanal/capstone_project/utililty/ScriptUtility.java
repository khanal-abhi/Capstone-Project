package co.khanal.capstone_project.utililty;

import android.content.Context;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by abhi on 3/18/16.
 */
public class ScriptUtility {

//    Static method that returns a list of all files with .txt extension in the app directory.
    public static List<File> getFiles(Context context){
        File dir = context.getFilesDir();
        List<File> files = Arrays.asList(dir);
        List<File> scripts = new ArrayList<>();

        for(File file : dir.listFiles()){
            if(file.isFile()){
                if(file.toString().contains(".txt")){
                    scripts.add(file);
                }
            }
        }
        return scripts;
    }

//    Static method that return a Script object from file, provided it is a .txt file.
    public static Script scriptFromFile(File file) throws IOException{
        Script script = null;
        if(file.isFile()){
            if(file.toString().contains(".txt")){
                FileInputStream fileInputStream = new FileInputStream(file);
                StringBuffer buffer = new StringBuffer();
                int data;
                while((data = fileInputStream.read()) != -1){
                    buffer.append((char)data);
                }
                script = new Script(improperFilename(file.getName()), buffer.toString());
            }
        }
        return script;
    }

//    Static method to save a script to file.
    public static File scriptToFile(Script script, Context context) throws IOException{
        if(script != null){
            File file = new File((context.getFilesDir().getPath()).toString() + "/" + properFilename(script.getFileName()));
            FileOutputStream outputStream = new FileOutputStream(file);
            StringBuffer buffer = new StringBuffer();
            for(int i = 0; i < script.getContent().length(); i++){
                outputStream.write(script.getContent().charAt(i));
            }
            return file;
        }
        return null;
    }

//    Static file to convert blank spaces to '_'.
    public static String properFilename(String filename){
        if(filename.length() == 0)
            return null;
        StringBuffer buffer = new StringBuffer();
        for(int i = 0; i < filename.length(); i++){
            char c = filename.charAt(i);
            if(c == ' '){
                c = '_';
            }
            buffer.append(c);
        }
        return buffer.toString() + ".txt";
    }


//     Static file to convert blank spaces to '_'.
    public static String improperFilename(String filename){
        if(filename.length() == 0)
            return null;
        StringBuffer buffer = new StringBuffer();
        for(int i = 0; i < filename.length(); i++){
            char c = filename.charAt(i);
            if(c == '_'){
                c = ' ';
            }
            buffer.append(c);
        }
        String result = buffer.toString();
        return result.replace(".txt", "");
    }

    public static boolean deleteScript(Script script, Context context){
        File file = new File((context.getFilesDir().getPath()).toString() + "/" + properFilename(script.getFileName()));
        return file.delete();
    }
}
