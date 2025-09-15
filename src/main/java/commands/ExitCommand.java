package commands;

import core.Command;
import core.Terminal;

import javax.swing.*;
import java.util.List;

public class ExitCommand implements Command {
    @Override
    public String execute(List<String> args, JFrame window, Terminal terminal) {
        Timer timer = new Timer(1500, e -> {
            window.dispose();
        });
        timer.start();
        return "Exiting...";
    }
}
