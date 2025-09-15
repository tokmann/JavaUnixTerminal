package core.vfs;

public class VfsFile extends VfsNode {
    private final byte[] data;

    public VfsFile(String name, byte[] data) {
        super(name);
        this.data = data == null ? new byte[0] : data;
    }

    @Override
    public boolean isDirectory() { return false; }

    public byte[] getData() { return data; }

    public String getTextUtf8() {
        try { return new String(data, java.nio.charset.StandardCharsets.UTF_8); }
        catch (Exception e) { return ""; }
    }
}
