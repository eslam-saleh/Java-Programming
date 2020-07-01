package src;

public class MultiplicationSeries implements ISubscriber {
    @Override
    public void notifySubscriber(String input) {
        // TODO Auto-generated method stub
        int result = 1;
        for (int i = Integer.parseInt(input); i > 1; i--) {
            result = result * i;
        }
        System.out.println("MultiplicationSeries = " + result);
    }
}
