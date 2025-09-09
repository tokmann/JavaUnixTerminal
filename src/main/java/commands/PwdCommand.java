package commands;

import core.Command;
import core.Terminal;

import javax.swing.*;

public class PwdCommand implements Command {
    @Override
    public String execute(String[] args, JFrame window, Terminal terminal) {
        return terminal.getCurrentDir();
    }
}
