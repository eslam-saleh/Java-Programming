package com.company;

public class User {
    public boolean create = false, delete = false;
    private String userName;
    private String password;
    private String path;

    User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    String getPassword() {
        return password;
    }

    String getUserName() {
        return userName;
    }

    public void setCap(String cap) {
        if (cap.charAt(0) == '0')
            create = false;
        else if (cap.charAt(0) == '1')
            create = true;
        if (cap.charAt(1) == '0')
            delete = false;
        else if (cap.charAt(1) == '1')
            delete = true;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public String getCap() {
        return create ? "1" + (delete ? "1" : "0") : "0" + (delete ? "1" : "0");
    }
}
