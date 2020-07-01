import java.io.*;
import java.nio.file.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import static java.lang.System.out;

public class Terminal extends Globals {
    public static String Redirect = "";
    public static boolean isPrint = true;

    public static void cd(String p) {
        if (p == "" || p == null) p = Globals.getPath();
        try {
            if (Files.isDirectory(Paths.get(p)) || Files.isDirectory(Paths.get(Globals.getPath()+p))) {
                Globals.setPath(p);
            } else
                throw new Exception("error");
        } catch (Exception e) {
            out.println("not such a file or folder path");
        }
    }

    public static String ls() {
        File f = new File(Globals.getPath());
        File[] listOfFiles = f.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                if (isPrint) System.out.println(file.getName());
                else Redirect += file.getName() + '\n';
            }
        }
        return Redirect;
    }

    public static void cp(String p1, String p2) throws IOException {
        if (p1 == "" || p1 == null || p2 == "" || p2 == null) {
            out.println("command needs two arguments");
            return;
        }
             Files.copy(Paths.get(p1), new FileOutputStream(p2));
    }

    public static String cat(ArrayList<String> argss) {
        try {
            String tmp = "", p = "";
            for (int i=0 ; i < argss.size() ; i++) {
                p = argss.get(i);
                Scanner input = new Scanner(new File(p));
                while (input.hasNextLine()) {
                    tmp = input.nextLine();
                    if (isPrint) System.out.println(tmp);
                    else Redirect += tmp + '\n';
                }
            }
        } catch (IOException e) {
            out.println("not such a file or folder path");
        }
        return Redirect;
    }

    public static String more(String p) {
        try {
            if (p == "" || p == null) p = Globals.getPath();
            String tmp = "";
            Scanner input = new Scanner(new File(p));
            while (input.hasNextLine()) {
                tmp = input.nextLine();
                if (isPrint) System.out.println(tmp);
                else Redirect += tmp + '\n';
            }
        } catch (IOException e) {
            out.println("not such a file or folder path");
        }
        return Redirect;
    }

    public static void mkdir(String p) {
        if (p == "" || p == null) {
            out.println("command needs one argument");
            return;
        }
        new File(p).mkdirs();
    }

    public static void rmdir(String p) {
        if (p == "" || p == null) {
            out.println("command needs one argument");
            return;
        }
        File directory = new File(p);
        directory.delete();
    }

    public static void mv(String p1, String p2) throws IOException {
        if (p1 == "" || p1 == null || p2 == "" || p2 == null) {
            out.println("command needs two arguments");
            return;
        }
        Files.copy(Paths.get(p1), new FileOutputStream(p2));
        File directory = new File(p1);
        directory.delete();
    }

    public static void rm(String p) {
        if (p == "" || p == null) {
            out.println("command needs one argument");
            return;
        }
        File directory = new File(p);
        directory.delete();
    }

    public static void args(String p) {
        System.out.println(p);
        switch (p) {
            case "cd":
                System.out.println("Number of args is 1: Source Path");
                break;
            case "cp":
                System.out.println("Number of args is 2: Source Path, Destination Path");
                break;
            case "cat":
                System.out.println("Number of args is 1 or 2: Source Path, Destination Path");
                break;
            case "more":
                System.out.println("Number of args is 1: The file to be separated into pages");
                break;
            case "mkdir":
                System.out.println("Number of args is 1: The new Directory Path");
                break;
            case "rmdir":
                System.out.println("Number of args is 1: The Directory to be removed");
                break;
            case "mv":
                System.out.println("Number of args is 2: Moving Source Path to Destination Path");
                break;
            case "rm":
                System.out.println("Number of args is 1: The File/Folder to be removed");
                break;
            case "ls":
            case "pwd":
            case "date":
            case "help":
            case "clear":
            case "args":
                System.out.println("No arguments needed");
                break;
        }
    }

    public static String date() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        if (isPrint) System.out.println(dateFormat.format(date));
        else Redirect = dateFormat.format(date);
        return Redirect;
    }

    public static void help() {
        System.out.println(
                "cd 1 arg" + '\n' + "ls no args" + '\n' + "cp 2 args" + '\n' + "cat 1 or 2 args" + '\n'
                        + "more 1 arg" + '\n' + "Pipe Operator 1 or 2 args" + '\n'
                        + "Redirect Operator > 1 args" + '\n' + "Redirect Operator >> 1 args" + '\n'
                        + "mkdir 1 arg" + '\n' + "rmdir 1 arg" + '\n' + "mv 2 args" + '\n' + "rm 1 arg" + '\n'
                        + "date no args" + '\n' + "pwd no args" + '\n' + "clear no args");
        System.out.print("Date : ");
        date();
        System.out.println("Stop All : exit");
    }

    public static String pwd() {
        if (isPrint) System.out.println(Globals.getPath());
        return Globals.getPath();
    }

    public static void clear() {
        for (int i = 0; i <= 50; i++)
            System.out.println();
    }
}
