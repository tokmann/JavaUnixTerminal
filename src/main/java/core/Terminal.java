package core;

import core.vfs.VfsDirectory;
import core.vfs.VfsFile;
import core.vfs.VfsLoader;
import core.vfs.VfsNode;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.io.File;

public class Terminal {

    private String currentDir;
    private List<String> history;
    private int historyIndex = -1;
    private Parser parser;

    private VfsDirectory vfsRoot = null;

    public Terminal(String vfsPath) {
        this.history = new ArrayList<>();
        this.parser = new Parser();

        if (vfsPath != null && !vfsPath.isEmpty()) {
            File f = new File(vfsPath);
            if (f.exists() && f.isFile() && vfsPath.toLowerCase().endsWith(".xml")) {
                try {
                    this.vfsRoot = VfsLoader.loadFromXml(f);
                    this.currentDir = "/";
                } catch (Exception e) {
                    System.err.println("Warning: failed to load VFS XML: " + e.getMessage());
                    this.currentDir = System.getProperty("user.dir");
                }
            } else if (f.exists() && f.isDirectory()) {
                this.currentDir = f.getAbsolutePath();
            } else {
                System.err.println("Warning: provided VFS path does not exist or is not a directory or XML: " + vfsPath);
                this.currentDir = System.getProperty("user.dir");
            }
        } else {
            try {
                vfsRoot = new VfsDirectory("");
                VfsDirectory home = new VfsDirectory("home");
                vfsRoot.addChild(home);

                VfsFile readme = new VfsFile(
                        "README.txt",
                        "Добро пожаловать в эмулятор!".getBytes(java.nio.charset.StandardCharsets.UTF_8)
                );
                home.addChild(readme);

                currentDir = "/";
            } catch (Exception e) {
                currentDir = System.getProperty("user.dir");
            }
        }
    }

    public boolean hasVfs() { return vfsRoot != null; }

    public VfsNode resolvePath(String path) {
        return resolveVfsPath(path);
    }

    public String getCurrentDir() {
        return currentDir;
    }

    private VfsNode resolveVfsPath(String path) {
        if (vfsRoot == null) return null;
        String p = path;
        if (!p.startsWith("/")) {
            if (!currentDir.endsWith("/")) p = currentDir + "/" + p;
            else p = currentDir + p;
        }
        String[] parts = p.split("/");
        LinkedList<String> stack = new LinkedList<>();
        for (String part : parts) {
            if (part.isEmpty() || ".".equals(part)) continue;
            if ("..".equals(part)) {
                if (!stack.isEmpty()) stack.removeLast();
            } else {
                stack.add(part);
            }
        }
        VfsNode node = vfsRoot;
        for (String part : stack) {
            if (!(node instanceof VfsDirectory)) return null;
            VfsDirectory dir = (VfsDirectory) node;
            node = dir.getChild(part);
            if (node == null) return null;
        }
        return node;
    }

    public String readVfsFileAsString(String path) {
        VfsNode node = resolveVfsPath(path);
        if (node == null || node.isDirectory()) return null;
        VfsFile file = (VfsFile) node;
        return file.getTextUtf8();
    }

    public List<String> listVfsDir(String path) {
        List<String> res = new ArrayList<>();
        VfsNode node = resolveVfsPath(path == null ? currentDir : path);
        if (node == null) return res;
        if (!node.isDirectory()) {
            res.add(node.getName());
            return res;
        }
        VfsDirectory dir = (VfsDirectory) node;
        for (VfsNode ch : dir.listChildren()) res.add(ch.getName() + (ch.isDirectory() ? "/" : ""));
        return res;
    }

    public boolean vfsChangeDir(String path) {
        VfsNode node = resolveVfsPath(path);
        if (node != null && node.isDirectory()) {
            if (path.startsWith("/")) currentDir = path;
            else {
                VfsDirectory d = (VfsDirectory) node;
                StringBuilder sb = new StringBuilder();
                java.util.Deque<String> parts = new java.util.ArrayDeque<>();
                VfsDirectory cur = d;
                while (cur != null && cur.getParent() != null) {
                    parts.addFirst(cur.getName());
                    cur = cur.getParent();
                }
                sb.append("/");
                String join = String.join("/", parts);
                if (!join.isEmpty()) sb.append(join);
                currentDir = sb.toString();
            }
            return true;
        }
        return false;
    }

    public String executeCommand(String input, JFrame window) throws Exception {
        if (!input.trim().isEmpty()) {
            history.add(input);
        }
        historyIndex = history.size();
        List<String> args = parser.parse(input);
        if (args.isEmpty()) return "";

        Command command = CommandFactory.getCommand(args.get(0));
        if (command != null) {
            return command.execute(args, window, this);
        } else {
            return args.get(0) + ": command not found";
        }
    }

    public List<String> getHistory() {
        return history;
    }

    public void setCurrentDir(String dir) {
        this.currentDir = dir;
    }

    public String getPrevHistory() {
        if (history.isEmpty()) return "";
        if (historyIndex > 0) historyIndex--;
        return history.get(historyIndex);
    }

    public String getNextHistory() {
        if (history.isEmpty()) return "";
        if (historyIndex < history.size() - 1) {
            historyIndex++;
            return history.get(historyIndex);
        } else {
            historyIndex = history.size();
            return "";
        }
    }
}
