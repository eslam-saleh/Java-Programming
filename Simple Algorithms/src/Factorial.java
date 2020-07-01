package src;

public class Factorial {
    public Factorial(){

    }
    

    public int calculateFactorialWithLoop(int number){
        int result = 1;
        for (int i = number ; i>1 ; i--){
            result = result * i;
        }
        return result;
    }
}
