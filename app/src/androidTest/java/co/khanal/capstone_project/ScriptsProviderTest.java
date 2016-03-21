package co.khanal.capstone_project;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import co.khanal.capstone_project.utililty.Script;

/**
 * Created by abhi on 3/20/16.
 */
public class ScriptsProviderTest extends ApplicationTest {

    Script script;
    Uri uri;

    public void setUp() throws Exception {
        super.setUp();
        script = new Script(1, "some data", "Here goes some content just for testing purposes.");
        uri = Uri.parse("content://co.khanal.capstone_project.scripts/");
    }

    public void tearDown() throws Exception {
        getContext().getContentResolver().delete(uri, null, new String[]{String.valueOf(script.getId()), script.getFileName(), script.getContent()});
        if(script != null){
            script = null;
        }
    }

    public void testDelete() throws Exception {

        ContentValues values = new ContentValues();
        values.put(ScriptsProvider.FILENAME, this.script.getFileName());
        values.put(ScriptsProvider.CONTENT, this.script.getContent());
        getContext().getContentResolver().insert(uri, values);

        assertEquals(0, getContext().getContentResolver().delete(uri, null, new String[]{String.valueOf(script.getId()), script.getFileName(), script.getContent()}));
    }

    public void testInsert() throws Exception {
        Script script1 = new Script();

        ContentValues script = new ContentValues();
        script.put(ScriptsProvider.FILENAME, this.script.getFileName());
        script.put(ScriptsProvider.CONTENT, this.script.getContent());
        getContext().getContentResolver().insert(uri, script);

        Cursor cursor = getContext().getContentResolver().query(
                uri,
                null,
                null,
                null,
                null
        );

        if(cursor != null){
            if(cursor.moveToFirst()){
                script1.setId(cursor.getLong(ScriptsProvider.ID_INDEX));
                script1.setFileName(cursor.getString(ScriptsProvider.FILENAME_INDEX));
                script1.setContent(cursor.getString(ScriptsProvider.CONTENT_INDEX));
            }
        }

        assertEquals(this.script.getFileName(), script1.getFileName());

    }

    public void testQuery() throws Exception {
        Cursor cursor = getContext().getContentResolver().query(
                uri,
                new String[]{
                        ScriptsProvider.ID,
                        ScriptsProvider.FILENAME,
                        ScriptsProvider.CONTENT
                },
                null,
                new String[]{
                        String.valueOf(script.getId()),
                        script.getFileName(),
                        script.getContent()
                },
                null
        );
        assertFalse(cursor.moveToFirst());
    }

    public void testUpdate() throws Exception {

        Script script1 = new Script(script.getId(), script.getFileName(), script.getContent() + ":new");

        ContentValues script = new ContentValues();
        script.put(ScriptsProvider.FILENAME, this.script.getFileName());
        script.put(ScriptsProvider.CONTENT, this.script.getContent());
        getContext().getContentResolver().insert(uri, script);

        script.put(ScriptsProvider.FILENAME, this.script.getFileName());
        script.put(ScriptsProvider.CONTENT, this.script.getContent() + " :new");
        getContext().getContentResolver().update(uri, script, null, null);

        Cursor cursor = getContext().getContentResolver().query(
                uri,
                null,
                null,
                null,
                null
        );

        if(cursor != null){
            if(cursor.moveToFirst()){
                script1.setId(cursor.getLong(ScriptsProvider.ID_INDEX));
                script1.setFileName(cursor.getString(ScriptsProvider.FILENAME_INDEX));
                script1.setContent(cursor.getString(ScriptsProvider.CONTENT_INDEX));
            }
        }

        assertEquals(this.script.getContent()+ " :new", script1.getContent());

    }
}