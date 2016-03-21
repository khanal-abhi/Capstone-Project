package co.khanal.capstone_project;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Switch;

import java.io.IOException;
import java.util.List;

import co.khanal.capstone_project.utililty.Script;
import co.khanal.capstone_project.utililty.ScriptUtility;

public class ScriptsProvider extends ContentProvider {

    public static final String ID = "id";
    public static final String FILENAME = "filename";
    public static final String CONTENT = "content";

    public static final int ID_INDEX = 0;
    public static final int FILENAME_INDEX = 1;
    public static final int CONTENT_INDEX = 2;

    public ScriptsProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        String filename = selectionArgs[1];
        return
        ScriptUtility.deleteScript(new Script(0, filename, ""), getContext()) ? 0 : -1;
    }

    @Override
    public String getType(Uri uri) {
        return "text/plain";
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.

        String filename = values.getAsString(FILENAME);
        String content = values.getAsString(CONTENT);
        Script script = new Script(filename, content);
        try {
            ScriptUtility.scriptToFile(script, getContext());
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
        return Uri.parse("content://co.khanal.capstone_project.scripts/?filename=" + script.getFileName());
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        return new ScriptCursor(getContext());
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Script script = new Script(values.getAsString(FILENAME), values.getAsString(CONTENT));
        if(ScriptUtility.deleteScript(script, getContext())){
            try {
                ScriptUtility.scriptToFile(script, getContext());
                return 0;
            } catch (IOException e) {
                return -1;
            }
        } else {
            return -1;
        }
    }

    public class ScriptCursor implements Cursor{

        List<Script> scripts;
        int position;

        public ScriptCursor(Context context){
            this.scripts = ScriptUtility.getScripts(context);
            position = 0;
        }

        @Override
        public int getCount() {
            return scripts.size();
        }

        @Override
        public int getPosition() {
            return position;
        }

        @Override
        public boolean move(int offset) {
            if(position + offset < scripts.size()){
                position = position + offset;
                return true;
            }
            return false;
        }

        @Override
        public boolean moveToPosition(int position) {
            if(position < scripts.size()){
                this.position = position;
                return true;
            }
            return false;
        }

        @Override
        public boolean moveToFirst() {
            if(0 < scripts.size()){
                this.position = 0;
                return true;
            }
            return false;
        }

        @Override
        public boolean moveToLast() {
            position = scripts.size();
            return true;
        }

        @Override
        public boolean moveToNext() {
            if(position + 1 < scripts.size()){
                position++;
                return true;
            }
            return false;
        }

        @Override
        public boolean moveToPrevious() {
            if(position > 0){
                position--;
                return true;
            }
            return false;
        }

        @Override
        public boolean isFirst() {
            if(position == 0){
                return true;
            }
            return false;
        }

        @Override
        public boolean isLast() {
            if(position == scripts.size()-1){
                return true;
            }
            return false;
        }

        @Override
        public boolean isBeforeFirst() {
            return false;
        }

        @Override
        public boolean isAfterLast() {
            return false;
        }

        @Override
        public int getColumnIndex(String columnName) {
            if(columnName.contentEquals(ID)){
                return 0;
            } else if (columnName.contentEquals(FILENAME)) {
                return 1;
            } else if (columnName.contentEquals(CONTENT)) {
                return 3;
            }
            return -1;
        }

        @Override
        public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
            if(columnName.contentEquals(ID)){
                return 0;
            } else if (columnName.contentEquals(FILENAME)) {
                return 1;
            } else if (columnName.contentEquals(CONTENT)) {
                return 3;
            }
            throw new IllegalArgumentException("No column found with name: " + columnName);
        }

        @Override
        public String getColumnName(int columnIndex) {
            switch (columnIndex){
                case 0:
                    return ID;
                case 1:
                    return FILENAME;
                case 3:
                    return CONTENT;
                default:
                    return null;
            }
        }

        @Override
        public String[] getColumnNames() {
            return new String[]{
                    ID,
                    FILENAME,
                    CONTENT
            };
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public byte[] getBlob(int columnIndex) {
            return new byte[0];
        }

        @Override
        public String getString(int columnIndex) {
            switch (columnIndex){
                case 0:
                    return String.valueOf(scripts.get(position).getId());
                case 1:
                    return scripts.get(position).getFileName();
                case 2:
                    return scripts.get(position).getContent();
                default:
                    return null;
            }
        }

        @Override
        public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {

        }

        @Override
        public short getShort(int columnIndex) {
            return -1;
        }

        @Override
        public int getInt(int columnIndex) {
            return -1;
        }

        @Override
        public long getLong(int columnIndex) {
            if(columnIndex == 0){
                return scripts.get(position).getId();
            }
            return -1;
        }

        @Override
        public float getFloat(int columnIndex) {
            return -1f;
        }

        @Override
        public double getDouble(int columnIndex) {
            return -1;
        }

        @Override
        public int getType(int columnIndex) {
            return -1;
        }

        @Override
        public boolean isNull(int columnIndex) {
            return false;
        }

        @Override
        public void deactivate() {

        }

        @Override
        public boolean requery() {
            return false;
        }

        @Override
        public void close() {

        }

        @Override
        public boolean isClosed() {
            return false;
        }

        @Override
        public void registerContentObserver(ContentObserver observer) {

        }

        @Override
        public void unregisterContentObserver(ContentObserver observer) {

        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void setNotificationUri(ContentResolver cr, Uri uri) {

        }

        @Override
        public Uri getNotificationUri() {
            return null;
        }

        @Override
        public boolean getWantsAllOnMoveCalls() {
            return false;
        }

        @Override
        public void setExtras(Bundle extras) {

        }

        @Override
        public Bundle getExtras() {
            return null;
        }

        @Override
        public Bundle respond(Bundle extras) {
            return null;
        }
    }
}
