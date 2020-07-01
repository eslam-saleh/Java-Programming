package com.SWE.Project.services;

import com.SWE.Project.models.DB;
import com.SWE.Project.models.User;
import com.SWE.Project.models.Session;
import com.SWE.Project.models.UserLoging;
import com.SWE.Project.models.UserRegister;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import java.sql.SQLException;

@RestController
public class UserController {
    @RequestMapping("/register/admin")
    public String register(@RequestParam("UserName") String uName,
                          @RequestParam("Email") String email,
                          @RequestParam("FirstName") String fName,
                          @RequestParam("LastName") String lName,
                          @RequestParam("Password1") String pass1,
                          @RequestParam("Password2") String pass2,
                          @RequestParam("Key") String key
    ) throws SQLException {
        UserRegister newUser = new UserRegister(email, uName, fName, lName, pass1, pass2, "admin", key);
        return newUser.signup();
    }

    @RequestMapping("/register/user")
    public String register(@RequestParam("UserName") String uName,
                          @RequestParam("Email") String email,
                          @RequestParam("FirstName") String fName,
                          @RequestParam("LastName") String lName,
                          @RequestParam("Password1") String pass1,
                          @RequestParam("Password2") String pass2
    ) throws SQLException {
        UserRegister newUser = new UserRegister(email, uName, fName, lName, pass1, pass2, "user");
        return newUser.signup();
    }

    @RequestMapping("/login")
    public String login(@RequestParam("UserName/Email") String uName,
                        @RequestParam("Password") String pass
    ) throws SQLException {
        UserLoging loginUser = new UserLoging(uName, pass);
        return loginUser.login();
    }

    @RequestMapping("/logout")
    public String login() throws SQLException {
        UserLoging loginUser = new UserLoging("", "");
        return loginUser.logOut();
    }

    @RequestMapping("/list/users")
    public List<User> listUsers() throws SQLException {
        if(Session.getSession() == null)
            return null;
        else if(Session.getSession().getAccType().toLowerCase().equals("admin"))
            return (new DB()).showAll();
        return null;
    }
}
