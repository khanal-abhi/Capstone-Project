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
        ScriptUtility.deleteAllScripts(getContext());
        script = new Script("hello world", "some data goes here.");
        script.setId(1);

    }

    public void testDeleteScript() throws Exception {
        ScriptUtility.scriptToFile(script, getContext());
        ScriptUtility.deleteScript(script, getContext());
        assertEquals(0, ScriptUtility.getScripts(getContext()).size());
    }

    public void tearDown() throws Exception {
        if(script != null)
            script = null;

        if(file != null)
            file.delete();
    }

    public void testGetFiles() throws Exception {
        ScriptUtility.scriptToFile(script, getContext());
        assertEquals(1, ScriptUtility.getScripts(getContext()).size());
    }

    public void testScriptFromFile() throws Exception {
        file = ScriptUtility.scriptToFile(script, getContext());
        Script newScript = ScriptUtility.scriptFromFile(file);
        assertEquals(script.getFileName(), (newScript.getFileName()));
    }

    public void testScriptToFile() throws Exception {
        ScriptUtility.scriptToFile(script, getContext());
        assertEquals(script.getContent(), ScriptUtility.getScripts(getContext()).get(0).getContent());
        assertEquals(script.getFileName(), ScriptUtility.getScripts(getContext()).get(0).getFileName());
    }

    public void testProperFilename() throws Exception {
        assertEquals("hello_world.txt", ScriptUtility.properFilename("hello world"));
    }

    public void testImproperFilename() throws Exception {
        assertEquals("hello world", ScriptUtility.improperFilename("hello_world.txt"));
    }

    public void testDeleteAllScripts() throws Exception {
        ScriptUtility.scriptToFile(script, getContext());
        ScriptUtility.deleteAllScripts(getContext());
        assertEquals(0, ScriptUtility.getScripts(getContext()).size());
    }

    public void testListMultipleFiles() throws Exception{
        ScriptUtility.scriptToFile(script, getContext());
        script.setFileName("another file");
        ScriptUtility.scriptToFile(script, getContext());
        assertEquals(2, ScriptUtility.getScripts(getContext()).size());
    }
}