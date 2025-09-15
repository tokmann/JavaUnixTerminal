package commands;

import core.Command;
import core.Terminal;
import core.vfs.VfsDirectory;
import core.vfs.VfsNode;

import javax.swing.*;
import java.util.List;

public class CdCommand implements Command {

    @Override
    public String execute(List<String> args, JFrame window, Terminal terminal) {
        if (args.size() < 2) return "cd: missing argument";
        boolean ok = terminal.vfsChangeDir(args.get(1));
        if (!ok) return "cd: directory not found";
        return "";
    }
}
