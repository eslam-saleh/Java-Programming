package com.SWE.Project.models;

import java.sql.*;

public class UserRegister{
    private User newUser;
    private String pass2;
    private String key;
    private Connection conn;

    public UserRegister(String email, String uName, String fName, String lName, String pass, String pass2, String accType) {
        newUser = new User(email, uName, fName, lName, pass, accType);
        this.pass2 = pass2;
    }

    public UserRegister(String email, String uName, String fName, String lName, String pass, String pass2, String accType, String key) {
        newUser = new User(email, uName, fName, lName, pass, accType);
        this.pass2 = pass2;
        this.key = key;
    }

    public String signup() throws SQLException {
        String message = "";
        if(!newUser.getEmail().contains("@")) message += "Invalid Email Address";
        if(!newUser.getPass().equals(pass2)) message += "Passwords don't match";
        if(newUser.getPass().length() < 8) message += "Passwords must be atlest 8 digits";
        if(message.length() != 0) return message;
        conn = (new DB()).getConn();
        PreparedStatement ps;
        ResultSet res;
        boolean chk1 = true, chk2 = true, chk3 = true;
        if (newUser.getAccType().toLowerCase().equals("admin")) {
            if (!key.equals("AdminKey00")) chk1 = false;
            if (!chk1) {
                if (Session.getSession() == null) {
                    ps = conn.prepareStatement("select * from users");
                    res = ps.executeQuery();
                    if (res.next())
                        chk2 = false;
                } else if (!Session.getSession().getAccType().toLowerCase().equals("admin"))
                    chk3 = false;
            }
        }
        if (chk1 || (chk2 && chk3)) {
            ps = conn.prepareStatement("select * from users where email = ? or user_name = ?");
            ps.setString(1, newUser.getEmail());
            ps.setString(2, newUser.getuName());
            res = ps.executeQuery();
            if (!res.next()) {
                ps = conn.prepareStatement("insert into users values (?, ?, ?, ?, ?, ?)");
                ps.setString(1, newUser.getEmail());
                ps.setString(2, newUser.getuName());
                ps.setString(3, newUser.getfName());
                ps.setString(4, newUser.getlName());
                ps.setString(5, newUser.getPass());
                ps.setString(6, newUser.getAccType());
                ps.executeUpdate();
                message = "User Added Successfully";
            } else
                message = "Username or Email Address Already Exists";
        } else
            message = "You don't have privileges to register a new admin";
        conn.close();
        return message;
    }
}
