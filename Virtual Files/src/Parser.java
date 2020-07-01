
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.io.*;

class Parser {
    private String text, cmd = "", path = "";

    Parser(String text) {
        this.text = text;
    }

    static void parseDrct(String path) throws IOException {
        Files.write(Paths.get("DiskStructure.vfs"), (path + '\n').getBytes(), StandardOpenOption.APPEND);
    }

    public static void removePath(String path) throws IOException {
        String txt = new String(Files.readAllBytes(Paths.get("DiskStructure.vfs")));
        String[] parts = txt.split("\n");
        new FileOutputStream("DiskStructure.vfs");

        for (String i : parts) {
            if (i.charAt(i.length() - 1) == '\r' || i.charAt(i.length() - 1) == '\n')
                i = i.substring(0, i.length() - 1);
            if (!i.equals(path))
                Files.write(Paths.get("DiskStructure.vfs"), (i + '\n').getBytes(), StandardOpenOption.APPEND);
        }
    }

    boolean parseCommands() {
        if (text.length() == 0 || text.equals(" ")) {
            return false;
        }
        if (!text.contains(" ")) {
            cmd = text;
        } else {
            ArrayList<String> args = new ArrayList<String>(Arrays.asList(text.split(" ")));
            cmd = args.get(0);
            path = args.get(1);
        }
        return true;
    }

    String getCmd() {
        return cmd;
    }

    String getPath() {
        return path;
    }
}
