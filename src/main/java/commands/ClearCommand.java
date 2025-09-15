package commands;

import core.Command;
import core.Terminal;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ClearCommand implements Command {

    @Override
    public String execute(List<String> args, JFrame window, Terminal terminal) {
        JTextArea textArea = findTextArea(window);
        if (textArea != null) {
            textArea.setText("");
        }
        return "";
    }

    private JTextArea findTextArea(JFrame window) {
        for (Component c : window.getContentPane().getComponents()) {
            if (c instanceof JScrollPane scrollPane) {
                Component view = scrollPane.getViewport().getView();
                if (view instanceof JTextArea textArea) {
                    return textArea;
                }
            }
        }
        return null;
    }
}
