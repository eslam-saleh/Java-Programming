package src;

public class SphereVolume implements ISubscriber {
    public double sphereVolume(double radius) {
        double spV = 0;
        spV = (4 * 22 * radius * radius * radius) / (3 * 7);
        return spV;
    }

    @Override
    public void notifySubscriber(String input) {
        double radius = Double.parseDouble(input);
        System.out.println("SphereVolume = " + sphereVolume(radius));
    }
}