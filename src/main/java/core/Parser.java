package core;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    public List<String> parse(String command) throws Exception {
        if (command == null || command.isEmpty()) return new ArrayList<>();

        boolean inQuotes = false;
        char quoteChar = '\0';
        StringBuilder currentToken = new StringBuilder();
        List<String> tokens = new ArrayList<>();

        for (int i = 0; i < command.length(); i++) {
            char currentChar = command.charAt(i);

            if (currentChar == '"' || currentChar == '\'') {
                if (inQuotes) {
                    if (currentChar == quoteChar) {
                        inQuotes = false;
                        tokens.add(currentToken.toString());
                        currentToken.setLength(0);
                        quoteChar = '\0';
                    } else {
                        currentToken.append(currentChar);
                    }
                } else {
                    inQuotes = true;
                    quoteChar = currentChar;
                    if (currentToken.length() > 0) {
                        tokens.add(currentToken.toString());
                        currentToken.setLength(0);
                    }
                }
            } else if (inQuotes) {
                currentToken.append(currentChar);
            } else if (currentChar == ' ') {
                if (currentToken.length() > 0) {
                    tokens.add(currentToken.toString());
                    currentToken.setLength(0);
                }
            } else {
                currentToken.append(currentChar);
            }
        }

        if (currentToken.length() > 0) {
            tokens.add(currentToken.toString());
        }

        if (inQuotes) {
            throw new Exception("Unclosed quotes in command");
        }

        System.out.println(tokens);
        return tokens;
    }
}
