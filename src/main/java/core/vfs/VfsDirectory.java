package core.vfs;

import java.util.*;

public class VfsDirectory extends VfsNode {
    private final Map<String, VfsNode> children = new LinkedHashMap<>();

    public VfsDirectory(String name) {
        super(name);
    }

    @Override
    public boolean isDirectory() { return true; }

    public void addChild(VfsNode node) {
        node.setParent(this);
        children.put(node.getName(), node);
    }

    public VfsNode getChild(String name) { return children.get(name); }

    public Collection<VfsNode> listChildren() { return children.values(); }

    public boolean hasChild(String name) { return children.containsKey(name); }
}
