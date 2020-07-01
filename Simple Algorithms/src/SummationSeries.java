package src;

import java.util.Scanner;

public class SummationSeries implements ISubscriber {

    static double seriesSum(int n) {
        int i = 1;
        // variable to calculate result
        double res = 0.0;
        boolean sign = true;
        while (n > 0) {
            n--;
            if (sign) {
                sign = !sign;
                res = res + (double) ++i / ++i;
            } else {
                sign = !sign;
                res = res - (double) ++i / ++i;
            }
        }
        return res;
    }

    @Override
    public void notifySubscriber(String input) {
        // TODO Auto-generated method stub
        int num = Integer.parseInt(input);
        System.out.println("SummationSeries : ");
        System.out.println(seriesSum(num));
    }
}
