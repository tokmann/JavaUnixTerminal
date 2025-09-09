package commands;

import core.Command;
import core.Terminal;

import javax.swing.*;

public class ExitCommand implements Command {
    @Override
    public String execute(String[] args, JFrame window, Terminal terminal) {
        Timer timer = new Timer(1500, e -> {
            window.dispose();
        });
        timer.start();
        return "Exiting...";
    }
}
