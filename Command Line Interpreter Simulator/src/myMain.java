import java.io.*;
import java.nio.file.*;
import java.util.*;

public class myMain {
    public static void main(String[] args) throws IOException {
        while (true) {
            System.out.print("[current -> " + Globals.getPath() + " ] ");
            Scanner input = new Scanner(System.in);
            String text = input.nextLine();
            if (text.equals("exit")) System.exit(0);
            if (text.contains("|")) {
                ArrayList<String> Texts = new ArrayList<String>();
                String tmp = "";
                for (int i = 0; i < text.length(); i++) {
                    if (text.charAt(i) != '|') tmp += text.charAt(i);
                    if (text.charAt(i) == '|' || i==text.length()-1) {
                        Texts.add(tmp);
                        tmp="";
                    }
                }
                for (int i=0 ; i<Texts.size() ; i++) Run(Texts.get(i));
                continue;
            }
            try {
                Run(text);
            } catch (IOException e) {
                System.out.println("Error Happened while Running !");
            }
        }
    }

    public static String[] commands = {"cd", "ls", "cp", "cat", "more", "mkdir", "rmdir", "mv", "rm", "args", "date", "help", "pwd", "clear"};

    public static void Run(String text) throws IOException {
        ArrayList<String> argss = new ArrayList<String>();
        String Cmd = "", Cmd2 = "", redirect = "";
        boolean isEntered = false;

        Parser p = new Parser(text);
        boolean isParsed = p.parse();
        Cmd = p.getCmd();
        argss = p.getArguments();
        int index = Arrays.asList(commands).indexOf(Cmd);

        Terminal t = new Terminal();

        int Symbole = p.getSymbol();
        if (Symbole !=0) {
            if (Cmd.equals("pwd")||Cmd.equals("cat")||Cmd.equals("ls")||Cmd.equals("more")||Cmd.equals("date"))
                t.isPrint = false;
        }else
            t.isPrint = true;
        System.out.println(t.isPrint);
        if (Symbole == 1) { // Redirect Operator >
            redirect = checkList(index, t, argss);
            Path file = Paths.get(p.getNewPath());
            Files.write(file, Collections.singleton(redirect));
            isEntered = true;
        } else if (Symbole == 2) { // Redirect Operator >>
            redirect = checkList(index, t, argss);
            Path file = Paths.get(p.getNewPath());
            Files.write(file, Collections.singleton(redirect), StandardOpenOption.APPEND);
            isEntered = true;
        }
        if (!isEntered) checkList(index, t, argss);
    }

    public static String checkList(int index, Terminal t, ArrayList<String> argss) throws IOException {
        switch (index) {
            case 0:
                t.cd(argss.get(0));
                break;
            case 1:
                return t.ls();
            case 2:
                t.cp(argss.get(0), argss.get(1));
                break;
            case 3:
                return t.cat(argss);
            case 4:
                return t.more(argss.get(0));
            case 5:
                t.mkdir(argss.get(0));
                break;
            case 6:
                t.rmdir(argss.get(0));
                break;
            case 7:
                t.mv(argss.get(0), argss.get(1));
                break;
            case 8:
                t.rm(argss.get(0));
                break;
            case 9:
                t.args(argss.get(0));
                break;
            case 10:
                return t.date();
            case 11:
                t.help();
                break;
            case 12:
                return t.pwd();
            case 13:
                t.clear();
                break;
            default:
                System.out.println("Please Enter Right Command");
        }
        return "";
    }
}
