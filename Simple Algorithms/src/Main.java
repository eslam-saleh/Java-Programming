package src;

import java.util.Scanner;

public class Main {
    private static ISubscriber subscribers[] = {
            new Fibonacci(),
            new BallVolume(),
            new MultiplicationSeries(),
            new CircleArea(),
            new SphereVolume(),
            new SphereCircumference(),
            new lucasSeries(),
            new SummationSeries(),
            new TwoPowerrN(),
            new SphereSurfaceArea(),
            new ReallySimpleSubscriber()
    };

    public static void main(String[] args) {
        Topic mathTopic = new Topic();
        for (ISubscriber sub : subscribers) {
            mathTopic.addSubscriber(sub);
        }
        Scanner in = new Scanner(System.in);
        System.out.println("\nChoose which Algorithm to run.." +
                "\n 1- Fibonacci" +
                "\n 2- CircleVolume" +
                "\n 3- MultiplicationSeries" +
                "\n 4- CircleArea" +
                "\n 5- SphereVolume" +
                "\n 6- SphereCircumference" +
                "\n 7- LucasSeries" +
                "\n 8- SummationSeries" +
                "\n 9- 2PowerN" +
                "\n 10- SphereSurfaceArea" +
                "\n 11- All" +
                "\n 0- Exit");
        System.out.println("=========================");
        System.out.print("Your Choice :: ");
        int result = in.nextInt();
        while (result < 0 || result > 11) {
            System.out.println("Invalid Choice !");
            System.out.print("Your Choice :: ");
            result = in.nextInt();
        }
        while (result != 0) {
            Scanner sc = new Scanner(System.in);
            System.out.print("Your Input :: ");
            String input = sc.next();
			while (!input.matches("-?\\d+(\\.\\d+)?")) { // check if input is Numeric
				System.out.println("Invalid Input !");
				System.out.print("Your Input :: ");
				input = sc.next();
			}
            System.out.println("=========================");
            if (result == 11)
                for (int i = 0; i < 10; i++) {
                    mathTopic.dispatchEvent(input, i + 1);
                    System.out.println("=========================");
                }
            else
                mathTopic.dispatchEvent(input, result);
            System.out.println("=========================");
            System.out.print("Your Choice :: ");
            result = in.nextInt();
            while (result < 0 || result > 11) {
                System.out.println("Invalid Choice !");
                System.out.print("Your Choice :: ");
                result = in.nextInt();
            }
        }
    }
}
