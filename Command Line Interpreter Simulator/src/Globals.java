public class Globals {

    private static String path = "C:\\Users\\ESLAM\\Desktop";

    public static void setPath(String path) {
        if (path.contains(":"))
            Globals.path = path;
        else
            Globals.path += path;
    }

    public static String getPath() {
        return path;
    }
}
