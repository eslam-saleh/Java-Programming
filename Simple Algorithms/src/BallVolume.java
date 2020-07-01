package src;

public class BallVolume implements ISubscriber {
    public double ball_volume(String r) {
        double pi = 22 / 7.0, volume = 0.0, c = 4 / 3.0;
        volume = c * pi * Math.pow(Double.parseDouble(r), 3);
        return volume;
    }
    @Override
    public void notifySubscriber(String input) {
        System.out.println("Ball Volume = " + ball_volume(input));
    }
}
