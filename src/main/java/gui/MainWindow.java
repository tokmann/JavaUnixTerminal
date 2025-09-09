package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.UnknownHostException;

import core.Terminal;
import utils.OSUtils;

public class MainWindow {

    private JFrame window;
    private JTextArea textArea;
    private Terminal terminal;

    public MainWindow() {
        try {
            Terminal terminal = new Terminal();

            window = new JFrame();
            window.setTitle("Emulator - " + OSUtils.getPrompt());
            window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            window.setSize(1000, 700);
            window.setLocationRelativeTo(null);
            window.setLayout(new BorderLayout());

            textArea = new JTextArea();
            textArea.setBackground(Color.BLACK);
            textArea.setForeground(Color.GREEN);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 17));
            try {
                textArea.setText(OSUtils.getPrompt());
                textArea.setCaretPosition(textArea.getDocument().getLength());
            } catch (UnknownHostException exception1) {
                throw new RuntimeException();
            }

            JScrollPane scrollPane = new JScrollPane(textArea);
            window.add(scrollPane, BorderLayout.CENTER);

            textArea.setEditable(true);

            textArea.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    try {
                        int promptPosition = textArea.getText().lastIndexOf(OSUtils.getPrompt()) + OSUtils.getPrompt().length();

                        if (textArea.getCaretPosition() < promptPosition) {
                            textArea.setCaretPosition(textArea.getDocument().getLength());
                        }

                        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE &&
                                textArea.getCaretPosition() <= promptPosition) {
                            e.consume();
                            return;
                        }

                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            e.consume();
                            String input = textArea.getText().substring(promptPosition);
                            if (input.isEmpty()) {
                                textArea.append('\n' + OSUtils.getPrompt());
                            } else {
                                String output = terminal.executeCommand(input, window);
                                //System.out.println(output);
                                if (output.equals("")) {
                                    textArea.append(OSUtils.getPrompt());
                                } else {
                                    textArea.append('\n' + output + '\n' + OSUtils.getPrompt());
                                }
                            }
                            textArea.setCaretPosition(textArea.getDocument().getLength());
                        }
                    } catch (UnknownHostException exception1) {
                        throw new RuntimeException();
                    }
                }
            });
        } catch (UnknownHostException exception1) {
            throw new RuntimeException();
        }
    }

    public void show() {
        window.setVisible(true);
    }
}
