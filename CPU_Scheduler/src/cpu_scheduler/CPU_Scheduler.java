/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpu_scheduler;
import java.util.*;

/**
 *
 * @author ESLAM
 */
public class CPU_Scheduler {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int result = 0;
        System.out.println("1-SJF\t2-SRTF\t3-Priority\t4-AG");
        result = in.nextInt();
        ArrayList<Process> Q = new ArrayList<Process>();
        if (result == 1) {
            Algorithms SJF = new Algorithms(result);
            System.out.println("");
            Q = SJF.scheduling();
        }
        else if (result == 2) {
            Algorithms SRTF = new Algorithms(result);
            System.out.println("");
            SRTF.scheduling();
        }
        else if (result == 3) {
            Algorithms priority = new Algorithms(result);
            System.out.println("");
            priority.scheduling();
        }
        else if (result == 4) {
            Algorithms AG = new Algorithms(result);
            System.out.println("");
            AG.scheduling();
        }
    }
}
