package src;

public class lucasSeries implements ISubscriber {
    public static int Fibo(int n) {
        if (n == 0) {
            return 2;
        }
        if (n == 1) {
            return 1;
        }
        return Fibo(n - 2) + Fibo(n - 1);
    }

    @Override
    public void notifySubscriber(String input) {
        int numberofterms = Integer.parseInt(input);
        System.out.println("LucasSeries : ");
        for (int i = 0; i < numberofterms; i++)
            System.out.print(Fibo(i) + " ");
        System.out.println();
    }
}
