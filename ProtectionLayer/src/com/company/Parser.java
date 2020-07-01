package com.company;

import java.util.ArrayList;
import java.util.Arrays;

public class Parser {
    private String text, cmd = "", userName = "", pass = "", path = "", cap = "";

    Parser(String text) {
        this.text = text;
    }

    boolean parseCommands() {
        if (text.length() == 0 || text.equals(" ")) {
            return false;
        }
        if (!text.contains(" ")) {
            cmd = text;
        } else {
            ArrayList<String> args = new ArrayList<String>(Arrays.asList(text.split(" ")));
            if (args.size() == 3) {     // Create User Case
                cmd = args.get(0);
                userName = args.get(1);
                pass = args.get(2);
            } else if (args.size() == 4) {      // Grant User Case
                cmd = args.get(0);
                userName = args.get(1);
                path = args.get(2);
                cap = args.get(3);
            } else if (args.size() == 2) {
                switch (args.get(0)) {
                    case "DeleteUser":
                        cmd = args.get(0);
                        userName = args.get(1);
                        break;
                    case "DeleteFolder":
                    case "CreateFolder":
                        cmd = args.get(0);
                        path = args.get(1);
                        break;
                }
            }
        }
        return true;
    }

    String getCmd() {
        return cmd;
    }

    public String getUserName() {
        return userName;
    }

    public String getPass() {
        return pass;
    }

    public String getCap() {
        return cap;
    }

    public String getPath() {
        return path;
    }
}
