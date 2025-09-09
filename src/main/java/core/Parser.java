package core;

public class Parser {
    public String[] parse(String command) {
        if (command.isEmpty()) return new String[] {};
        return command.split(" ");
    }
}
