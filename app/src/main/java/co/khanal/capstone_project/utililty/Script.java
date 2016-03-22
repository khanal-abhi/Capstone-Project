package co.khanal.capstone_project.utililty;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import co.khanal.capstone_project.ScriptsProvider;

/**
 * Created by abhi on 3/18/16.
 */
public class Script implements Parcelable{

    public static final String KEY = "script_key";
    public static final String FILENAME_KEY = "filename_key";
    public static final String CONTENT_KEY = "content_key";

    private long id;
    private String fileName;
    private String content;

    public Script() {
    }

    public Script(String fileName, String content) {
        this.fileName = fileName;
        this.content = content;
    }

    protected Script(Parcel in) {
        Bundle bundle = in.readBundle();
        fileName = bundle.getString(FILENAME_KEY);
        content = bundle.getString(CONTENT_KEY);
    }

    public static final Creator<Script> CREATOR = new Creator<Script>() {
        @Override
        public Script createFromParcel(Parcel in) {
            return new Script(in);
        }

        @Override
        public Script[] newArray(int size) {
            return new Script[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Script)) return false;

        Script script = (Script) o;

        return
                id == script.getId() &&
                        fileName == script.getFileName() &&
                        content == script.getContent();

    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + getFileName().hashCode();
        result = 31 * result + getContent().hashCode();
        return result;
    }

    public Script(long id, String fileName, String content) {
        this.id = id;
        this.fileName = fileName;
        this.content = content;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putString(FILENAME_KEY, fileName);
        bundle.putString(CONTENT_KEY, content);
        dest.writeBundle(bundle);
    }

    @Override
    public String toString() {
        return "Script{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public static Script fromCursor(Cursor cursor){
        return new Script(
                cursor.getLong(ScriptsProvider.ID_INDEX),
                cursor.getString(ScriptsProvider.FILENAME_INDEX),
                cursor.getString(ScriptsProvider.CONTENT_INDEX)
        );
    }
}
