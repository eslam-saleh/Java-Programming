import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;

public class Directory {
    String directoryPath = "", directoryName = "";
    ArrayList<File> files = new ArrayList<>();
    ArrayList<Directory> subDirectories = new ArrayList<Directory>();
    boolean deleted = false;

    Directory() {
        ;
    }

    Directory(String path) {
        if (path.charAt(path.length() - 1) == '\r' || path.charAt(path.length() - 1) == '\n')
            this.directoryPath = path.substring(0, path.length() - 1);
        else this.directoryPath = path;

        if (directoryPath.equals("root")) {
            directoryName = "root";
        } else
            for (int i = path.length() - 1; i > 0; i--) {
                if (path.charAt(i) == '/') {
                    directoryName = path.substring(i + 1);
                    break;
                }
            }
    }

    public void DisplayDirectoryStructure(Directory root, int level) throws IOException {
        for (int i = 0; i < level; i++)
            System.out.print("\t");
        System.out.println(root.directoryName);

        if (root.subDirectories == null && root.files.size() == 0)
            return;
        int plus = level + 1;
        for (int i = 0; i < root.subDirectories.size(); i++)
            DisplayDirectoryStructure(root.subDirectories.get(i), plus);

        for (int i = 0; i < root.files.size(); i++) {
            for (int j = 0; j < plus; j++)
                System.out.print("\t");
            System.out.print(root.files.get(i).fileName + '\n');
        }
    }

    public static void CreateStructure() throws IOException {
        Main.Directories.clear();
        String txt = new String(Files.readAllBytes(Paths.get("DiskStructure.vfs")));
        String[] parts = txt.split("\n");

        Arrays.sort(parts);
        Main.root = new Directory(parts[0]);
        Main.Directories.add(Main.root);
        for (int i = 1; i < parts.length; i++)
            Main.Directories.add(new Directory(parts[i]));

        for (int i = 1; i < Main.Directories.size(); i++) {
            if (getSymbols(Main.Directories.get(i).directoryPath) ==
                    getSymbols(Main.Directories.get(i - 1).directoryPath)) {
                for (int j = i - 1; j >= 0; j--) {
                    if (getSymbols(Main.Directories.get(j).directoryPath) ==
                            getSymbols(Main.Directories.get(i).directoryPath) - 1) {
                        Main.Directories.get(j).subDirectories.add(Main.Directories.get(i));
                        break;
                    }
                }
            } else if (getSymbols(Main.Directories.get(i).directoryPath) >
                    getSymbols(Main.Directories.get(i - 1).directoryPath)) {
                Main.Directories.get(i - 1).subDirectories.add(Main.Directories.get(i));

            } else if (getSymbols(Main.Directories.get(i).directoryPath) <
                    getSymbols(Main.Directories.get(i - 1).directoryPath)) {
                for (int j = i - 1; j >= 0; j--) {
                    if (getSymbols(Main.Directories.get(j).directoryPath) ==
                            getSymbols(Main.Directories.get(i).directoryPath) - 1) {

                        Main.Directories.get(j).subDirectories.add(Main.Directories.get(i));
                        break;
                    }
                }
            }
        }
        for (int i = 0; i < Main.Directories.size(); i++)
            for (int j = 0; j < Main.Directories.get(i).subDirectories.size(); j++)
                if (Main.Directories.get(i).subDirectories.get(j).directoryName.contains(".")) {
                    Main.Directories.get(i).files.add(new File(Main.Directories.get(i).subDirectories.get(j).directoryPath));
                    Directory d = Main.Directories.get(i).subDirectories.get(j);
                    Main.Directories.get(i).subDirectories.remove(d);
                    j--;
                    Main.Directories.remove(d);
                    i--;
                    break;
                }

        String tmp = "", txt2 = new String(Files.readAllBytes(Paths.get("Files.txt")));
        String[] parts2 = txt2.split("\n");


        if (!txt2.isEmpty() && !txt2.equals("\n"))
            for (String i : parts2) {
                if (i.charAt(i.length() - 1) == '\r' || i.charAt(i.length() - 1) == '\n')
                    i = i.substring(0, i.length() - 1);
                if (i.contains("."))
                    tmp = i;
                else {
                    i = i.substring(1, i.length() - 1);
                    i = i.replaceAll(" ", "");
                    String[] blocks = i.split(",");
                    ArrayList<Integer> allocBlocks = new ArrayList<>();
                    for (String ch : blocks)
                        allocBlocks.add(Integer.parseInt(ch));
                    for (Directory d : Main.Directories)
                        for (File f : d.files) {
                            if (f.fileName.equals(tmp))
                                f.setAllocatedBlocks(allocBlocks);
                        }
                    Disk.setBlocks(allocBlocks);
                }
            }
    }

    private static int getSymbols(String line) {
        int count = 0;
        for (int j = 0; j < line.length(); j++) {
            if (line.charAt(j) == '/') {
                count++;
            }
        }
        return count;
    }
}