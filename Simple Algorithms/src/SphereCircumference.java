package src;

import java.lang.Math.*;

public class SphereCircumference implements ISubscriber {
    @Override
    public void notifySubscriber(String input) {
        double circumference = 2 * Double.parseDouble(input) * Math.PI;
        System.out.println("SphereCircumference = " + circumference);
    }
}