package co.khanal.capstone_project.utililty;

import android.content.Context;
import android.widget.ScrollView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by abhi on 3/18/16.
 */
public class ScriptUtility {

    private Context context;

    public ScriptUtility(Context context) {
        this.context = context;
    }

    public List<File> getFiles(){
        File dir = context.getFilesDir();
        List<File> files = Arrays.asList(dir);
        List<File> scripts = new ArrayList<>();

        for(File file : files){
            if(file.isFile()){
                if(file.toString().contains(".txt")){
                    scripts.add(file);
                }
            }
        }
        return scripts;
    }

    public Script fromFile(File file) throws IOException{
        Script script = null;
        if(file.isFile()){
            if(file.toString().contains(".txt")){
                FileInputStream fileInputStream = new FileInputStream(file);
                StringBuffer buffer = new StringBuffer();
                int data;
                while((data = fileInputStream.read()) != -1){
                    buffer.append((char)data);
                }
                script = new Script(file.toString(), buffer.toString());
            }
        }
        return script;
    }
}
