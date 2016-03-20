package co.khanal.capstone_project.utililty;

/**
 * Created by abhi on 3/18/16.
 */
public class Script {

    private long id;
    private String fileName;
    private String content;

    public Script() {
    }

    public Script(String fileName, String content) {
        this.fileName = fileName;
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Script)) return false;

        Script script = (Script) o;

        if (getId() != script.getId()) return false;
        if (!getFileName().equals(script.getFileName())) return false;
        return getContent().equals(script.getContent());

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
}
