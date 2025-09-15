package commands;

import core.Command;
import core.Terminal;

import javax.swing.*;
import java.util.List;

public class PwdCommand implements Command {
    @Override
    public String execute(List<String> args, JFrame window, Terminal terminal) {
        return terminal.getCurrentDir();
    }
}
