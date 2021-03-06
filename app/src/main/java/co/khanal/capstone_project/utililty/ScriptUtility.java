package co.khanal.capstone_project.utililty;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.content.ContextCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhi on 3/18/16.
 */
public class ScriptUtility {

//    Static method that returns a list of all files with .txt extension in the app directory.
    public static List<Script> getScripts (Context context){
        File dir = context.getFilesDir();
        List<Script> scripts = new ArrayList<>();
        int id = 1;

        for(File file : dir.listFiles()){
            if(file.isFile()){
                if(file.toString().contains(".txt")){
                    try {
                        Script script = scriptFromFile(file);
                        script.setId(id);
                        scripts.add(script);
                    } catch (IOException e){
                        return null;
                    }
                }
            }
            id++;
        }

        if (isExternalStorageAvailable(context)){
            dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            for(File file : dir.listFiles()){
                if(file.isFile()){
                    if(file.toString().contains(".txt")){
                        try {
                            Script script = scriptFromFile(file);
                            script.setId(id);
                            scripts.add(script);
                        } catch (IOException e){
                            return null;
                        }
                    }
                }
                id++;
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


//     Static file to convert '_' to blank spaces.
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

    public static boolean deleteAllScripts(Context context){
        for(Script script : getScripts(context)){
            if(!deleteScript(script, context))
                return false;
        }
        return true;
    }

    public static boolean isExternalStorageAvailable(Context context){

        if(ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED)
            return false;

        if(Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()){
            return true;
        }
        return false;
    }
}
