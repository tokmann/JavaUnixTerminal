package core;

import javax.swing.*;
import java.util.List;

public interface Command {
    String execute(List<String> args, JFrame window, Terminal terminal);
}
