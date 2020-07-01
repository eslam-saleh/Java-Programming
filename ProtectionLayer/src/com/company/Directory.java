package com.company;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class Directory {
    String directoryPath = "", directoryName = "";
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