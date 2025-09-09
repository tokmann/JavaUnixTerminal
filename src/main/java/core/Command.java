package core;

import javax.swing.*;

public interface Command {
    String execute(String[] args, JFrame window, Terminal terminal);
}
