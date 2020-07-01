package com.SWE.Project.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DB {
    Connection conn;

    public DB() throws SQLException {
        conn = DriverManager.getConnection("jdbc:derby:c://BookStore;create=true");
        ResultSet res = conn.getMetaData().getTables(null, "APP", "USERS", null);
        if (!res.next()) {
            conn.createStatement().executeUpdate("create table users (email varchar(30) primary key," +
                    " user_name varchar(30)," +
                    " first_name varchar(30)," +
                    " last_name varchar(30)," +
                    " password varchar(30)," +
                    " acc_type varchar(30))");
        }
    }

    public Connection getConn() {
        return conn;
    }

    public List<User> showAll() throws SQLException {
        ResultSet res = (new DB()).getConn().createStatement().executeQuery("select * from users");
        List<User> allData = new ArrayList<User>();
        while(res.next()) {
            User user = new User();
            user.setEmail(res.getString("email"));
            user.setuName(res.getString("user_name"));
            user.setfName(res.getString("first_name"));
            user.setlName(res.getString("last_name"));
            user.setPass(res.getString("password"));
            user.setAccType(res.getString("acc_type"));
            allData.add(user);
        }
        return allData;
    }
}
