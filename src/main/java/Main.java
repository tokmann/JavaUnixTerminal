import gui.MainWindow;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        String vfsPath = null;
        String scriptPath = null;

        for (int i = 0; i < args.length; i++) {
            String a = args[i];
            if (a.startsWith("--vfs=")) {
                vfsPath = a.substring("--vfs=".length());
            } else if (a.equals("--vfs") && i + 1 < args.length) {
                vfsPath = args[++i];
            } else if (a.startsWith("--script=")) {
                scriptPath = a.substring("--script=".length());
            } else if (a.equals("--script") && i + 1 < args.length) {
                scriptPath = args[++i];
            } else {
                if (vfsPath == null && scriptPath == null && args.length == 1) {
                    scriptPath = a;
                } else if (vfsPath == null && scriptPath == null && args.length == 2) {
                    vfsPath = args[0];
                    scriptPath = args[1];
                    break;
                }
            }
        }

        System.out.println("Starting Emulator with parameters:");
        System.out.println("  VFS    : " + (vfsPath == null ? "(not set)" : vfsPath));
        System.out.println("  Script : " + (scriptPath == null ? "(not set)" : scriptPath));
        System.out.println("Usage examples:");
        System.out.println("  java -jar emulator.jar --vfs=/path/to/vfs --script=/path/to/start.txt");
        System.out.println("  java -jar emulator.jar --script=start.txt");

        final String finalVfs = vfsPath;
        final String finalScript = scriptPath;

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainWindow mainWindow = new MainWindow(finalVfs, finalScript);
                mainWindow.show();
            }
        });
    }
}
