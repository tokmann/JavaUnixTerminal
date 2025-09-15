package commands;

import core.CommandFactory;
import core.Terminal;
import core.Command;
import core.vfs.VfsFile;
import core.vfs.VfsNode;

import javax.swing.*;
import java.util.List;

public class CatCommand implements Command {

    @Override
    public String execute(List<String> args, JFrame window, Terminal terminal) {
        if (args.size() < 2) return "cat: missing argument";
        String content = terminal.readVfsFileAsString(args.get(1));
        if (content == null) return "cat: file not found or is directory";
        return content;
    }
}
