package com.SWE.Project.models;

public class User {
    private String email;
    private String uName;
    private String fName;
    private String lName;
    private String pass;
    private String accType;

    public User() {
    }

    public User(String email, String uName, String fName, String lName, String pass, String accType) {
        this.email = email;
        this.uName = uName;
        this.fName = fName;
        this.lName = lName;
        this.pass = pass;
        this.accType = accType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }
}
