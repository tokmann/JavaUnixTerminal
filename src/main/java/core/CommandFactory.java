package core;

import commands.*;

public class CommandFactory {
    public static Command getCommand(String name) {
        if (name == null || name.isEmpty()) return null;

        return switch (name) {
            case "cd" -> new CdCommand();
            case "clear" -> new ClearCommand();
            case "exit" -> new ExitCommand();
            case "ls" -> new LsCommand();
            case "pwd" -> new PwdCommand();
            case "echo" -> new EchoCommand();
            case "cat" -> new CatCommand();
            case "touch" -> new TouchCommand();
            default -> null;
        };
     }
}
