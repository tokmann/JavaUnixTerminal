package core;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Terminal {

    private String currentDir;
    private List<String> history;
    private Parser parser;

    public Terminal() {
        this.currentDir = System.getProperty("user.dir");
        this.history = new ArrayList<>();
        this.parser = new Parser();
    }

    public String getCurrentDir() {
        return currentDir;
    }

    public String executeCommand(String input, JFrame window) {
        history.add(input);
        String[] args = parser.parse(input);
        if (args.length == 0) return "";

        Command command = CommandFactory.getCommand(args[0]);
        if (command != null) {
            return command.execute(args, window, this);
        } else {
            return args[0] + ": command not found";
        }
    }

    public List<String> getHistory() {
        return history;
    }

    public void setCurrentDir(String dir) {
        this.currentDir = dir;
    }
}
