package src;

public class CircleArea implements ISubscriber {
    @Override
    public void notifySubscriber(String input) {
        // TODO Auto-generated method stub
        double pi = 22 / 7.0, area = 0.0;
        area = pi * Math.pow(Double.parseDouble(input), 2);
        System.out.println("CircleArea = " + area);
    }
}
