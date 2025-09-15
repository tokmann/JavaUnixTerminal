package commands;

import core.Command;
import core.Terminal;

import javax.swing.*;
import java.util.List;

public class CdCommand implements Command {
    @Override
    public String execute(List<String> args, JFrame window, Terminal terminal) {
        if (args.size() == 1) return "cd: missing arguments";
        String path = args.get(1);
        //System.out.println(Arrays.toString(args));
        return "cd " + path;
    }
}
