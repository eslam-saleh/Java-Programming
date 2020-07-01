package src;

public class Fibonacci implements ISubscriber {

    public static int Fibo(int n) {
        if (n == 0) {
            return 0;
        }
        if (n == 1 || n == 2) {
            return 1;
        }
        return Fibo(n - 2) + Fibo(n - 1);
    }
    @Override
    public void notifySubscriber(String input) {
        int numberofterms = Integer.parseInt(input);
        System.out.print("Fibonacci :" + "\n");
        for (int i = 0; i < numberofterms; i++) {
            System.out.print(Fibo(i) + "\n");
        }
    }
}
