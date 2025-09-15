package commands;

import core.Command;
import core.Terminal;

import javax.swing.*;
import java.util.List;

public class EchoCommand implements Command {
    @Override
    public String execute(List<String> args, JFrame window, Terminal terminal) {
        if (args.size() <= 1) return "";
        return String.join(" ", args.subList(1, args.size()));
    }
}
