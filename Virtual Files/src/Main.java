import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class Main {
    public static Directory root = new Directory();
    public static ArrayList<Directory> Directories = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Directory.CreateStructure();
        Disk.loadBlocks();
        while (true) {
            System.out.print("\ncommand -> ");
            Scanner input = new Scanner(System.in);
            String text = input.nextLine();
            if (text.equals("exit")) System.exit(0);
            try {
                Run(text);
            } catch (IOException e) {
                System.out.println("Error Happened while Running !");
            }
            Terminate();
        }
    }

    public static String[] commands = {"CreateFile", "CreateFolder", "DeleteFile",
            "DeleteFolder", "DisplayDiskStatus", "DisplayDiskStructure"};

    public static void Run(String text) throws IOException {
        String cmd = "", path = "";
        Parser p = new Parser(text);
        boolean isParsed = p.parseCommands();
        if (!isParsed) {
            System.out.println("Error Happened while parsing !");
            return;
        }
        cmd = p.getCmd();
        path = p.getPath();
        int index = Arrays.asList(commands).indexOf(cmd);
        checkList(index, path);
    }

    public static void checkList(int index, String path) throws IOException {
        switch (index) {
            case 0:
                CreateFile(path);
                break;
            case 1:
                CreateFolder(path);
                break;
            case 2:
                DeleteFile(path);
                break;
            case 3:
                DeleteFolder(path);
                break;
            case 4:
                DisplayDiskStatus();
                break;
            case 5:
                root.DisplayDirectoryStructure(Directories.get(0), 0);
                break;
            default:
                System.out.println("Please Enter Right Command");
        }
    }

    public static void CreateFile(String path) throws IOException {
        Parser.parseDrct(path);
        File file = new File(path);
        System.out.println("\nCreate file in which allocation technique ::" +
                "\n\t1- Contiguous Allocation" +
                "\n\t2- LinkedList Allocation" +
                "\n\t3- Indexed Allocation");
        Scanner input = new Scanner(System.in);
        int inp = input.nextInt();
        switch (inp) {
            case 1:
                file.setAllocatedBlocks(Disk.contiguous_allocation());
                break;
            case 2:
                file.setAllocatedBlocks(Disk.linked_allocation());
                break;
            case 3:
                file.setAllocatedBlocks(Disk.indexed_allocation());
                break;
            default:
                System.out.println("Wrong Input !");
        }

        for (int j = 0; j < Directories.size(); j++)
            for (int i = path.length() - 1; i > 0; i--)
                if (path.charAt(i) == '/')
                    if (Directories.get(j).directoryPath.equals(path.substring(0, i))) {
                        Directories.get(j).files.add(file);
                        j = Directories.size();
                        break;
                    }
        Files.write(Paths.get("Files.txt"), (file.fileName + "\n" + file.getAllocatedBlocks() + "\n").getBytes(), StandardOpenOption.APPEND);
        Directory.CreateStructure();
    }

    public static void CreateFolder(String path) throws IOException {
        Parser.parseDrct(path);
        for (Directory d : Directories)
            if (d.directoryPath.equals(path)) {
                System.out.println("Path is Already Exist !");
                return;
            }
        int count = 0;
        for (int i = 0; i < path.length(); i++)
            if (path.charAt(i) == '/')
                count++;
        if (count == 1) {
            root.subDirectories = new ArrayList<>();
            root.subDirectories.add(new Directory(path));
            Directories.add(new Directory(path));

        } else {
            String[] parts = path.split("/");
            for (int i = 0; i < parts.length; i++) {
                String tmp = "";
                tmp += i == parts.length - 1 ? parts[i] : parts[i] + "/";
                for (int j = 0; j < root.subDirectories.size(); j++) {
                    if (tmp.equals(root.subDirectories.get(j).directoryPath)) {
                        root = root.subDirectories.get(j);
                        if (root.subDirectories == null) {
                            root.subDirectories = new ArrayList<>();
                            root.subDirectories.add(new Directory(path));
                            Directories.add(new Directory(path));
                            i = parts.length;
                            break;
                        }
                    }
                }
            }
        }
        for (Directory d : Directories)
            if (d.directoryPath.equals("root"))
                root = d;
        Directory.CreateStructure();
    }

    public static void DeleteFile(String path) throws IOException {
        for (Directory d : Directories)
            for (File f : d.files)
                if (f.filePath.equals(path)) {
                    Disk.deleteBlocks(f.getAllocatedBlocks(), f.fileName);
                    f.deleted = true;
                    break;
                }
        Parser.removePath(path);
    }

    public static void DeleteFolder(String path) throws IOException {
        for (Directory dd : Directories)
            if (dd.directoryPath.equals(path)) {
                for (File f : dd.files) {
                    Disk.deleteBlocks(f.getAllocatedBlocks(), f.fileName);
                    f.deleted = true;
                }
                for (Directory d : dd.subDirectories)
                    d.deleted = true;
                dd.deleted = true;
            }
        Parser.removePath(path);
    }

    public static void DisplayDiskStatus() {
        int count = 0;
        int[] arr = Disk.getBlocks();
        ArrayList<Integer> emptyBlocks = new ArrayList<>(), allocBlocks = new ArrayList<>();
        for (int i = 0; i < arr.length; i++)
            if (arr[i] == 1) {
                count++;
                allocBlocks.add(i);
            } else emptyBlocks.add(i);
        System.out.println("Disk Status :: \n" +
                "\n1- Disk Size = " + Disk.getBlockSize() * Disk.getBlocks().length +
                "\n2- Empty Size = " + Disk.getBlockSize() * (Disk.getBlocks().length - count) +
                "\n3- Allocated Size = " + Disk.getBlockSize() * count +
                "\n4- Empty Blocks in the Disk = " + emptyBlocks +
                "\n5- Allocated Blocks in the Disk = " + allocBlocks);
    }

    private static void Terminate() throws IOException {
        Directory d = null;
        for (int i = 0; i < Main.Directories.size(); i++) {
            if (Main.Directories.get(i).deleted) {
                for (int j = 0; j < Main.Directories.get(i).subDirectories.size(); j++) {
                    for (int k = 0; k < Main.Directories.get(i).subDirectories.get(j).files.size(); k++) {
                        DeleteFile(Main.Directories.get(i).subDirectories.get(j).files.get(k).filePath);
                        Main.Directories.get(i).subDirectories.get(j).files.remove(
                                Main.Directories.get(i).subDirectories.get(j).files.get(k));
                        System.out.println("k = " + k);
                        k--;
                    }
                    d = Main.Directories.get(i).subDirectories.get(j);
                    DeleteFolder(d.directoryPath);
                    Main.Directories.get(i).subDirectories.remove(d);
                    j--;
                }
                DeleteFolder(Main.Directories.get(i).directoryPath);
                Main.Directories.remove(d);
            }

            for (int j = 0; j < Main.Directories.get(i).files.size(); j++) {
                if (Main.Directories.get(i).files.get(j).deleted) {
                    DeleteFile(Main.Directories.get(i).files.get(j).filePath);
                    Main.Directories.get(i).files.remove(Main.Directories.get(i).files.get(j));
                    j--;
                }
            }
        }
    }

}
