package commands;

import core.Command;
import core.Terminal;

import javax.swing.*;
import java.util.Arrays;

public class CdCommand implements Command {
    @Override
    public String execute(String[] args, JFrame window, Terminal terminal) {
        if (args.length == 1) return "cd: missing arguments";
        String path = args[1];
        //System.out.println(Arrays.toString(args));
        return "cd " + path;
    }
}
