package commands;

import core.Command;
import core.Terminal;
import core.vfs.VfsDirectory;
import core.vfs.VfsFile;
import core.vfs.VfsNode;

import javax.swing.*;
import java.util.List;

public class LsCommand implements Command {

    @Override
    public String execute(List<String> args, JFrame window, Terminal terminal) {
        String targetPath = args.size() > 1 ? args.get(1) : terminal.getCurrentDir();
        List<String> children = terminal.listVfsDir(targetPath);
        if (children.isEmpty()) return "ls: empty or not found";
        return String.join("  ", children);
    }
}
