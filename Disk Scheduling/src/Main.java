import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        int result = 0, initial_start = 0;
        //System.out.print("Enter Sequence of input : ");
        //String seq = in.nextLine();
        //String seq = "38, 180, 130, 10, 50, 15,190, 90, 150";
        String seq = new String(Files.readAllBytes(Paths.get("seq.txt")));
        System.out.print("Enter Initial Start : ");
        initial_start = in.nextInt();
        //initial_start = 120;
        System.out.print("1-FCFS\t2-SSTF\t3-SCAN\t4-C-SCAN\t5-C-Look\t6-New Algo\t7-All :: ");
        result = in.nextInt();
        while (result > 0 && result < 8) {
            ArrayList<Process> Q = new ArrayList<Process>();
            if (result == 1) {
                Algorithms FCFS = new Algorithms(result, initial_start, seq);
                FCFS.scheduling();
            } else if (result == 2) {
                Algorithms SSTF = new Algorithms(result, initial_start, seq);
                SSTF.scheduling();
            } else if (result == 3) {
                Algorithms SCAN = new Algorithms(result, initial_start, seq);
                SCAN.scheduling();
            } else if (result == 4) {
                Algorithms C_SCAN = new Algorithms(result, initial_start, seq);
                C_SCAN.scheduling();
            } else if (result == 5) {
                Algorithms C_Look = new Algorithms(result, initial_start, seq);
                C_Look.scheduling();
            } else if (result == 6) {
                Algorithms New_Algo = new Algorithms(result, initial_start, seq);
                New_Algo.scheduling();
            } else {
                System.out.print("\nFCFS :: ");
                Algorithms FCFS = new Algorithms(1, initial_start, seq);
                FCFS.scheduling();
                System.out.print("\nSSTF :: ");
                Algorithms SSTF = new Algorithms(2, initial_start, seq);
                SSTF.scheduling();
                System.out.print("\nSCAN :: ");
                Algorithms SCAN = new Algorithms(3, initial_start, seq);
                SCAN.scheduling();
                System.out.print("\nC_SCAN :: ");
                Algorithms C_SCAN = new Algorithms(4, initial_start, seq);
                C_SCAN.scheduling();
                System.out.print("\nC_Look :: ");
                Algorithms C_Look = new Algorithms(5, initial_start, seq);
                C_Look.scheduling();
                System.out.print("\nNew_Algo :: ");
                Algorithms New_Algo = new Algorithms(6, initial_start, seq);
                New_Algo.scheduling();
            }
            System.out.print("\n\n1-FCFS\t2-SSTF\t3-SCAN\t4-C-SCAN\t5-C-Look\t6-New Algo :: ");
            result = in.nextInt();
        }
    }
}
