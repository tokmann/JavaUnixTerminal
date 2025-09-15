package core.vfs;

public abstract class VfsNode {
    protected final String name;
    protected VfsDirectory parent;

    public VfsNode(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public VfsDirectory getParent() { return parent; }
    void setParent(VfsDirectory p) { this.parent = p; }
    public abstract boolean isDirectory();
}
