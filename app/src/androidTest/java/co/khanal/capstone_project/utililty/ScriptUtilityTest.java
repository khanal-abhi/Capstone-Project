package co.khanal.capstone_project.utililty;

import android.test.AndroidTestCase;

import java.io.File;

/**
 * Created by abhi on 3/20/16.
 */
public class ScriptUtilityTest extends AndroidTestCase {

    Script script;
    File file;

    public void setUp() throws Exception {
        super.setUp();
        script = new Script("hello world", "some data goes here.");

    }

    public void testDeleteScript() throws Exception {
        ScriptUtility.scriptToFile(script, getContext());
        ScriptUtility.deleteScript(script, getContext());
        assertEquals(0, ScriptUtility.getFiles(getContext()).size());
    }

    public void tearDown() throws Exception {
        if(script != null)
            script = null;

        if(file != null)
            file.delete();
    }

    public void testGetFiles() throws Exception {
        ScriptUtility.scriptToFile(script, getContext());
        assertEquals(1, ScriptUtility.getFiles(getContext()).size());
    }

    public void testScriptFromFile() throws Exception {
        file = ScriptUtility.scriptToFile(script, getContext());
        Script newScript = ScriptUtility.scriptFromFile(file);
        assertEquals(script.getFileName(), (newScript.getFileName()));
    }

    public void testScriptToFile() throws Exception {
        ScriptUtility.scriptToFile(script, getContext());
        assertEquals(ScriptUtility.properFilename(script.getFileName()), ScriptUtility.getFiles(getContext()).get(0).getName());
    }

    public void testProperFilename() throws Exception {
        assertEquals("hello_world.txt", ScriptUtility.properFilename("hello world"));
    }

    public void testImproperFilename() throws Exception {
        assertEquals("hello world", ScriptUtility.improperFilename("hello_world.txt"));
    }
}