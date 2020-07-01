import java.util.*;

public class Parser extends Globals {
    private String text = "" ,cmd = "" ,newPath = "";
    private ArrayList<String> args = new ArrayList<String>();
    public boolean isRedCopy = false ,isRedOverwrite = false;

    Parser (String text){ this.text = text; }

    public boolean parse () {
        if (text.length() == 0 || text.equals(' ')) {
            return false;
        }
        String tmp = "";
        int i = 0;
        for (; text.charAt(i) != ' '; i++) {
            cmd += text.charAt(i);
            if (i == text.length()-1)
                break;
        }
          if (cmd.equals("date") ||cmd.equals("ls") ||cmd.equals("help") || cmd.equals("pwd") || cmd.equals("clear"))
             return false;

        if (cmd.equals("args")) {
            i++;
            for (; i != text.length(); i++)
                tmp += text.charAt(i);
            args.add(tmp);
            return false;
        }
        while (true) {
            if (text.charAt(i) != '\"') {
                i++;
                continue;
            } else {
                int start = i;
                boolean pathStart = false;
                for (; i != text.length(); i++) {
                    if (text.charAt(i) == '\"' && !pathStart) {
                        pathStart = true;
                        start = i + 1;
                    } else if (text.charAt(i) == '\"' && pathStart) {
                        pathStart = false;
                        if (text.substring(start, i).contains(":"))
                            args.add(text.substring(start, i));
                        else
                            args.add(Globals.getPath() + text.substring(start, i));
                    }
                }
            }
                break;
        }
        return true;
    }

    public String getCmd(){
        return cmd;
    }

    public ArrayList<String> getArguments(){
        return args;
    }

    public int getSymbol(){
        int i = 0;
        boolean isEntered = false;
        for (; i<text.length() ; i++){
            if (text.charAt(i)!='>' && !isEntered)
                continue;
            isEntered = true;
            if (text.charAt(i) == '>' && text.charAt(i+1) == '>'){
                isRedOverwrite = true;
                i+=3;
            }
            else if (text.charAt(i) == '>'){
                isRedCopy = true;
                i+=2;
            }
            newPath+=text.charAt(i);
        }
        if (isRedCopy) return 1;
        if (isRedOverwrite) return 2;
        return 0;
    }

    public String getNewPath(){
        return newPath;
    }
}

