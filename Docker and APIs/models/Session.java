package com.SWE.Project.models;

public class Session {
    private static User activeUser = null;
    public static boolean active = false;

    public Session(User activeUser) {
        if (this.activeUser == null) {
            this.activeUser = activeUser;
            active = true;
        }
    }

    public static User getSession() {
        return activeUser;
    }

    public static void delSession() {
        activeUser = null;
    }
}
