package commands;

import core.Command;
import core.Terminal;

import javax.swing.*;

public class LsCommand implements Command {
    @Override
    public String execute(String[] args, JFrame window, Terminal terminal) {
        if (args.length == 1) return "ls: missing arguments";
        String directory = args[1];
        return "ls " + directory;
    }
}
