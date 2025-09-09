package utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class OSUtils {
    public static String getPrompt() throws UnknownHostException {
        String user = System.getProperty("user.name");
        String host = InetAddress.getLocalHost().getHostName();
        String dir = "~";
        return user + "@" + host + ":" + dir + "$ ";
    }
}
