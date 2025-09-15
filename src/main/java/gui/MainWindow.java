package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.UnknownHostException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import core.Terminal;
import utils.OSUtils;

public class MainWindow {

    private JFrame window;
    private JTextArea textArea;
    private Terminal terminal;

    private final String vfsPath;
    private final String scriptPath;

    public MainWindow() {
        this(null, null);
    }

    public MainWindow(String vfsPath, String scriptPath) {
        this.vfsPath = vfsPath;
        this.scriptPath = scriptPath;

        try {
            this.terminal = new Terminal(vfsPath);

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

            String startupInfo;
            try {
                startupInfo = "Emulator started with parameters:\n"
                        + "VFS    : " + (vfsPath == null ? "(not set)" : vfsPath) + "\n"
                        + "Script : " + (scriptPath == null ? "(not set)" : scriptPath) + "\n\n"
                        + OSUtils.getPrompt();
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }

            textArea.setText(startupInfo);
            textArea.setCaretPosition(textArea.getDocument().getLength());

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
                                if (output.equals("")) {
                                    textArea.append(OSUtils.getPrompt());
                                } else {
                                    textArea.append('\n' + output + '\n' + OSUtils.getPrompt());
                                }
                            }
                            textArea.setCaretPosition(textArea.getDocument().getLength());
                        }

                        if (e.getKeyCode() == KeyEvent.VK_UP) {
                            e.consume();
                            String prev = terminal.getPrevHistory();
                            replaceCurrentInput(prev);
                        }

                        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                            e.consume();
                            String next = terminal.getNextHistory();
                            replaceCurrentInput(next);
                        }

                    } catch (UnknownHostException exception1) {
                        throw new RuntimeException();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        } catch (UnknownHostException exception1) {
            throw new RuntimeException();
        }
    }

    public void show() {
        window.setVisible(true);
        if (scriptPath != null && !scriptPath.isEmpty()) {
            runStartScript(scriptPath);
        }
    }

    private void runStartScript(String scriptPath) {
        Thread t = new Thread(() -> {
            File f = new File(scriptPath);
            if (!f.exists() || !f.isFile()) {
                appendText("\nError: start script not found: " + scriptPath + "\n" + promptSafe());
                return;
            }

            try (BufferedReader br = Files.newBufferedReader(f.toPath(), StandardCharsets.UTF_8)) {
                String line;
                int ln = 0;
                while ((line = br.readLine()) != null) {
                    ln++;
                    final String cmd = line.trim();
                    if (cmd.isEmpty() || cmd.startsWith("#")) continue;

                    appendText("\n" + promptSafe() + cmd);

                    String output;
                    try {
                        output = terminal.executeCommand(cmd, window);
                    } catch (Exception e) {
                        String msg = "Error executing script line " + ln + ": " + e.getMessage();
                        appendText("\n" + msg + "\n" + promptSafe());
                        continue;
                    }

                    if (output == null || output.isEmpty()) {
                        appendText("\n" + promptSafe());
                    } else {
                        appendText("\n" + output + "\n" + promptSafe());
                    }

                    try { Thread.sleep(80); } catch (InterruptedException ignored) {}
                }
            } catch (IOException e) {
                appendText("\nError reading script: " + e.getMessage() + "\n" + promptSafe());
            }
        }, "StartScriptRunner");
        t.setDaemon(true);
        t.start();
    }

    private String promptSafe() {
        try {
            return OSUtils.getPrompt();
        } catch (UnknownHostException e) {
            return "> ";
        }
    }

    private void appendText(String text) {
        SwingUtilities.invokeLater(() -> {
            textArea.append(text);
            textArea.setCaretPosition(textArea.getDocument().getLength());
        });
    }

    private void replaceCurrentInput(String newText) {
        try {
            int promptPosition = textArea.getText().lastIndexOf(OSUtils.getPrompt()) + OSUtils.getPrompt().length();
            textArea.replaceRange(newText, promptPosition, textArea.getDocument().getLength());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
