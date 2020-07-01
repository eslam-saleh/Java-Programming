package com.company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static ArrayList<User> users = new ArrayList<>();
    public static String currentUser = "admin", currentPassword = "admin";
    public static Directory root = new Directory();
    public static ArrayList<Directory> Directories = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        readUsersFromFile();
        readCapabilitiesFromFile();
        while (true) {
            for (User u : users) System.out.println(u.getUserName() + " " + u.getPath() + " " + u.getCap());
            System.out.print("\ncommand -> ");
            Scanner input = new Scanner(System.in);
            String text = input.nextLine();
            if (text.equals("exit")) System.exit(0);
            try {
                Run(text);
            } catch (IOException e) {
                System.out.println("Error Happened while Running !");
            }
            writeUsersToFile(users);
        }
    }

    public static String[] commands = {"TellUser", "CreateUser", "DeleteUser", "LogIn",
            "Grant", "CreateFolder", "DeleteFolder"};

    public static void Run(String text) throws IOException {
        String cmd = "", path = "", cap = "", userName = "", password = "";
        Parser p = new Parser(text);
        boolean isParsed = p.parseCommands();
        if (!isParsed) {
            System.out.println("Error Happened while parsing !");
            return;
        }
        cmd = p.getCmd();
        userName = p.getUserName();
        password = p.getPass();
        path = p.getPath();
        cap = p.getCap();
        int index = Arrays.asList(commands).indexOf(cmd);
        checkList(index, userName, password, path, cap);
    }

    public static void checkList(int index, String userName, String password,
                                 String path, String cap) throws IOException {
        switch (index) {
            case 0:
                TellUser();
                break;
            case 1:
                if (currentUser.equals("admin"))
                    CreateUser(userName, password);
                else System.out.println("Just Admins could add new users");
                break;
            case 2:
                if (currentUser.equals("admin"))
                    DeleteUser(userName);
                else System.out.println("Just Admins could add new users");
                break;
            case 3:
                LogIn(userName, password);
                break;
            case 4:
                if (currentUser.equals("admin"))
                    Grant(userName, path, cap);
                else System.out.println("Just Admins could add new users");
                break;
            case 5:
                for (User u : users)
                    if (u.getUserName().equals(currentUser) && u.create)
                        CreateFolder(path);
                break;
            case 6:
                for (User u : users)
                    if (u.getUserName().equals(currentUser) && u.delete) {
                        DeleteFolder(path);
                        Terminate();
                    }
                break;
            default:
                System.out.println("Please Enter Right Command");
        }
    }

    private static void TellUser() {
        System.out.println("Current User : " + currentUser);
    }

    private static void LogIn(String username, String pass) {
        for (User u : users)
            if (u.getUserName().equals(username) && u.getPassword().equals(pass)) {
                currentUser = username;
                currentPassword = pass;
                return;
            }
        System.out.println("No Such User !");
    }

    private static void CreateUser(String userName, String password) {
        for (int i = 0; i < users.size(); i++) {
            if (userName.equals(users.get(i).getUserName())) {
                System.out.println("This user created before");
                return;
            }
        }
        User user = new User(userName, password);
        users.add(user);
    }

    private static void DeleteUser(String userName) {
        if (userName.equals("admin")) {
            System.out.println("You can't delete this user");
            return;
        }
        for (int i = 0; i < users.size(); i++) {
            if (userName.equals(users.get(i).getUserName())) {
                users.remove(users.get(i));
                return;
            }
        }
        System.out.println("There is no such user with this name");
    }

    private static void Grant(String userName, String path, String cap) throws IOException {
        for (int i = 0; i < users.size(); i++) {
            if (userName.equals(users.get(i).getUserName())) {
                users.get(i).setPath(path);
                users.get(i).setCap(cap);

                String txt = new String(Files.readAllBytes(Paths.get("capabilities.txt")));
                String[] parts = txt.split("\n");
                for (int j = 0; j < parts.length; j++) {
                    String[] parts2 = parts[j].split(",");
                    if (parts2[0].equals(path)) {
                        parts[j] += "," + userName + "," + cap;
                        return;
                    }
                }
                new FileOutputStream("capabilities.txt");
                for (int j = 0; j < parts.length; j++) {
                    Files.write(Paths.get("capabilities.txt"), (parts[j] + "\n").getBytes(), StandardOpenOption.APPEND);
                }
                Files.write(Paths.get("capabilities.txt"), (path + "," + userName + "," + cap + "\n").getBytes(), StandardOpenOption.APPEND);
                return;
            }
        }
    }

    public static void CreateFolder(String path) throws IOException {
        for (User u : users)
            if (u.getUserName().equals(currentUser) && path.contains(u.getPath())) {
                Files.write(Paths.get("DiskStructure.vfs"), (path + '\n').getBytes(), StandardOpenOption.APPEND);
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
                return;
            }
    }

    public static void DeleteFolder(String path) throws IOException {
        for (User u : users)
            if (u.getUserName().equals(currentUser) && path.contains(u.getPath())) {
                for (Directory dd : Directories)
                    if (dd.directoryPath.equals(path)) {
                        for (Directory d : dd.subDirectories)
                            d.deleted = true;
                        dd.deleted = true;
                    }
                String txt = new String(Files.readAllBytes(Paths.get("DiskStructure.vfs")));
                String[] parts = txt.split("\n");
                new FileOutputStream("DiskStructure.vfs");

                for (String i : parts) {
                    if (i.charAt(i.length() - 1) == '\r' || i.charAt(i.length() - 1) == '\n')
                        i = i.substring(0, i.length() - 1);
                    if (!i.equals(path))
                        Files.write(Paths.get("DiskStructure.vfs"), (i + '\n').getBytes(), StandardOpenOption.APPEND);
                }
                return;
            }
    }

    private static void writeUsersToFile(ArrayList<User> users) throws IOException {
        String str = "";
        BufferedWriter writer = new BufferedWriter(new FileWriter("user.txt", false));
        for (int i = 0; i < users.size(); i++) {
            str += users.get(i).getUserName();
            str += ",";
            str += users.get(i).getPassword();
            str += '\n';
            writer.write(str);
            str = "";
        }
        writer.close();

    }

    private static void readUsersFromFile() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("user.txt"));
            String line = reader.readLine();
            while (line != null) {
                ArrayList<String> args = new ArrayList<String>(Arrays.asList(line.split(",")));
                String userName = args.get(0);
                String password = args.get(1);
                User user = new User(userName, password);
                users.add(user);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readCapabilitiesFromFile() throws IOException {
        String txt = new String(Files.readAllBytes(Paths.get("capabilities.txt")));
        String[] parts = txt.split("\n");
        for (int j = 0; j < parts.length; j++) {
            String[] parts2 = parts[j].split(",");
            for (int k = 1; k < parts2.length; k += 2) {
                for (int i = 0; i < users.size(); i++) {
                    if (parts2[k].equals(users.get(i).getUserName())) {
                        users.get(i).setPath(parts2[0]);
                        users.get(i).setCap(parts2[k + 1]);
                    }
                }
            }
        }
    }

    private static void Terminate() throws IOException {
        Directory d = null;
        for (int i = 0; i < Main.Directories.size(); i++) {
            if (Main.Directories.get(i).deleted) {
                for (int j = 0; j < Main.Directories.get(i).subDirectories.size(); j++) {
                    d = Main.Directories.get(i).subDirectories.get(j);
                    DeleteFolder(d.directoryPath);
                    Main.Directories.get(i).subDirectories.remove(d);
                    j--;
                }
                DeleteFolder(Main.Directories.get(i).directoryPath);
                Main.Directories.remove(d);
            }
        }
    }
}
