/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpu_scheduler;

/**
 *
 * @author ESLAM
 */
import java.awt.Color;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.util.*;

public class Algorithms {

    private ArrayList<Process> queue = new ArrayList<>();
    private int context, nProcesses, cpu_time, result, t = 0;
    private float total_wait, total_ta;
    public static int CurTime = 0;
    ArrayList<Process> q = new ArrayList<>();

    public Algorithms(int result) {
        Scanner in = new Scanner(System.in);
        cpu_time = 0;
        total_wait = 0;
        total_ta = 0;
        this.result = result;
        System.out.print("Enter # of processes : ");
        nProcesses = in.nextInt();
        if (result == 2 || result == 3) {
            System.out.print("Enter context switching time : ");
            context = in.nextInt();
        }
        int Quantum = 0;
        if (result == 4) {
            System.out.print("Enter Quantum time : ");
            Quantum = in.nextInt();
        }

        for (int i = 0; i < nProcesses; i++) {
            String name, color;
            int arrival_time, burst_time, priority = 0;
            System.out.print("Enter proccess " + (i + 1) + " name : ");
            name = in.next();
            System.out.print("Enter proccess " + (i + 1) + " arrival time : ");
            arrival_time = in.nextInt();
            System.out.print("Enter proccess " + (i + 1) + " burst time : ");
            burst_time = in.nextInt();
            if (result != 1 && result != 2) {
                System.out.print("Enter proccess " + (i + 1) + " priority : ");
                priority = in.nextInt();
            }
            System.out.print("Enter proccess " + (i + 1) + " color : ");
            color = in.next();

            queue.add(new Process(name, arrival_time, burst_time, priority, Quantum, color));
            System.out.println("");

        }
        if (result == 2) {
            System.out.println("Process |arrival |burst |");
        }
        if (result == 3) {
            System.out.println("Process |arrival |burst |priority |");
        }
        if (result == 4) {
            System.out.println("Process |arrival |burst |priority |Quantum |AG");
        }
        for (int i = 0; i < queue.size(); i++) {
            System.out.printf("%-8s|", queue.get(i).name);
            System.out.printf("%-8s|", queue.get(i).arrival_time);
            System.out.printf("%-6s|", queue.get(i).burst_time);
            if (result == 3 || result == 4) {
                System.out.printf("%-9s|", queue.get(i).priority);
            }
            if (result == 4) {
                System.out.printf("%-9s|", queue.get(i).Quantum);
            }
            if (result == 4) {
                System.out.printf("%-9s|", queue.get(i).AG_Factor);
            }
            System.out.println("");
        }
        sort(0, cpu_time);

    }

    public void sort(int start, int limit) {
        for (int i = start; i < queue.size() - 1; i++) {
            for (int j = i + 1; j < queue.size(); j++) {
                if (Compare(queue.get(i), queue.get(j), limit) > 0) {

                    Collections.swap(queue, i, j);
                }
            }
        }
    }

    public void aging(int start, int limit) {
        for (int i = 0; i < queue.size(); i++) {
            if (queue.get(i).arrival_time < limit) {
                queue.get(i).priority = queue.get(i).o_priority - (limit - queue.get(i).arrival_time) / 2;
                if (queue.get(i).priority < 0) {
                    queue.get(i).priority = 0;
                }
            }
        }
        sort(start + 1, limit);
    }

    public int Compare(Process p1, Process p2, int limit) {
        if (p1.arrival_time <= limit && p2.arrival_time <= limit && limit != 0) {
            if (p1.priority < p2.priority) {
                return -1;
            } else if (p1.priority == p2.priority) {
                if (p1.arrival_time < p2.arrival_time) {
                    return -1;
                } else if (p1.arrival_time == p2.arrival_time) {
                    return p1.name.compareTo(p2.name);
                } else {
                    return 1;
                }
            } else {
                return 1;
            }
        } else {
            if (p1.arrival_time < p2.arrival_time) {
                return -1;
            } else if (p1.arrival_time == p2.arrival_time) {
                if (p1.priority < p2.priority) {
                    return -1;
                } else if (p1.priority == p2.priority) {
                    return p1.name.compareTo(p2.name);
                } else {
                    return 1;
                }
            } else {
                return 1;
            }
        }
    }

    public boolean RUN() {
        for (int i = 0; i < queue.size(); i++) {
            if (queue.get(i).Current > 0) {
                return true;
            }
        }
        return false;
    }

    public boolean Available(int i) {
        return ((queue.get(i).arrival_time <= CurTime) && (queue.get(i).Current != 0));
    }

    public int WaitingAvg() {
        int Sum = 0;
        for (int i = 0; i < queue.size(); i++) {
            Sum += queue.get(i).wait_time;
        }
        return Sum;
    }

    public int TurnaroundAvg() {
        int Sum = 0;
        for (int i = 0; i < queue.size(); i++) {
            Sum += queue.get(i).ta_time;
        }
        return Sum;
    }

    public ArrayList<Process> scheduling() {
        if (result == 1) {
            int arrival = 0, finished = 0;
            ArrayList<Process> finishedNodes = new ArrayList();
            queue.sort(Comparator.comparing(Process::getBurst_time));

            while (finished < queue.size()) {
                for (int i = 0; i < queue.size(); i++) {
                    if (queue.get(i).arrival_time <= arrival && !queue.get(i).visited) {
                        queue.get(i).visited = true;
                        finished++;
                        queue.get(i).wait_time = arrival - queue.get(i).arrival_time;
                        total_wait += queue.get(i).wait_time;
                        queue.get(i).ta_time = queue.get(i).wait_time + queue.get(i).burst_time;
                        total_ta += queue.get(i).ta_time;
                        queue.get(i).start_time = arrival;
                        queue.get(i).end_time = arrival + queue.get(i).burst_time;
                        arrival += queue.get(i).burst_time - 1;
                        finishedNodes.add(queue.get(i));
                        break;
                    }
                }
                arrival++;
            }
            print(result);
        } else if (result == 2) {
            HashMap<Integer, String> Running = new HashMap<Integer, String>();
            Process CurProcess;
            CurTime = 0;
            Process PrevProcess = null;
            boolean first = true;
            CurProcess = queue.get(0);
            while (RUN()) {
                PrevProcess = CurProcess;
                for (int i = 0; i < queue.size(); i++) {
                    if (Available(i) && ((queue.get(i).Current < CurProcess.Current) || CurProcess.Current == 0)) {
                        CurProcess = queue.get(i);
                    }
                }
                if (first == true) {
                    PrevProcess = CurProcess;
                }
                for (int i = 0; i < queue.size(); i++) {
                    if (Available(i) && queue.get(i).name != CurProcess.name) {
                        queue.get(i).Incre_waiting();
                    }
                }
                if (CurProcess.Current != 0) {
                    CurProcess.Dicre_Current();
                }
                if ((PrevProcess.name != CurProcess.name)) {
                    for (int i = 0; i < context; i++) {
                        Running.put(CurTime, "Switching");
                        CurTime = CurTime + 1;
                    }
                    Running.put(CurTime, CurProcess.name);
                    for (int i = 0; i < queue.size(); i++) {
                        if (Available(i)) {
                            for (int j = 0; j < context; j++) {
                                queue.get(i).Incre_waiting();
                            }
                        }
                    }
                } else {
                    Running.put(CurTime, CurProcess.name);
                }
                CurTime++;
                first = false;
            }
            Running.put(CurTime, CurProcess.name);
            for (int i = 0; i < queue.size(); i++) {
                queue.get(i).Calc_Turnaround();
            }
            total_wait = WaitingAvg();
            total_ta = TurnaroundAvg();
            print(result);
            int begin = 0, end = 0;
            System.out.println("\nProcess Execution Order : \n");
            for (int i = end; i < Running.size(); i++) {
                if (i == 0) {
                    begin = i;
                } else {
                    begin = i - 1;
                }
                for (int j = begin + 1; j < Running.size(); j++) {
                    if (!(Running.get(begin).equals(Running.get(j))) || j == Running.size() - 1) {
                        end = j;
                        if (!Running.get(begin).equals("Switching")) {
                            System.out.println(Running.get(begin) + " begin : " + begin + " end : " + end);

                            Process P = new Process();
                            P.name = Running.get(begin);
                            P.start_time = begin;
                            P.end_time = end;
                            for (Process PP : queue) {
                                if (PP.name.equals(Running.get(begin))) {
                                    P.color = PP.color;
                                }
                            }
                            q.add(P);
                        }
                        i = end;
                        break;
                    }
                }
            }
            return q;

        } else if (result == 3) {

            for (int i = 0; i < queue.size(); i++) {
                if (queue.get(i).arrival_time > cpu_time) {
                    cpu_time = queue.get(i).arrival_time;
                }
                queue.get(i).start_time = cpu_time;
                cpu_time += queue.get(i).burst_time;
                queue.get(i).end_time = cpu_time;
                queue.get(i).end_time = cpu_time;
                queue.get(i).ta_time = queue.get(i).end_time - queue.get(i).arrival_time;
                queue.get(i).wait_time = queue.get(i).ta_time - queue.get(i).burst_time;
                total_wait += queue.get(i).wait_time;
                total_ta += queue.get(i).ta_time;
                cpu_time += context;
                aging(i, cpu_time);
            }
            print(result);
        } else if (result == 4) {
            boolean first = true;
            int count = 0;
            queue.sort(Comparator.comparing(Process::getAG_Factor));
            Process min = queue.get(count);
            queue.sort(Comparator.comparing(Process::getArrival_time));
            ArrayList<Integer> T_History = new ArrayList<Integer>();
            T_History.add(queue.get(0).arrival_time);

            for (int i = 0; i < queue.size(); i++) {
                q.add(queue.get(i));
            }
            ArrayList<Process> dieList = new ArrayList<Process>();
            ArrayList<Process> wait = new ArrayList<Process>();
            int qSum = queue.stream().mapToInt(Process -> Process.getQuantum()).sum(), waiting = 0;

            for (int j = 0; qSum != 0; j++) {
                if (q.get(j).arrival_time > t) {
                    wait.add(q.get(j));
                    q.remove(j);
                    j--;
                    continue;
                }
                if (wait.size() > 0) {
                    if (wait.get(waiting).arrival_time < t) {
                        wait.get(waiting).visited = true;
                        q.add(wait.get(waiting));
                        waiting++;
                    }
                }

                if (min.burst_time == 0) {
                    count++;
                    queue.sort(Comparator.comparing(Process::getAG_Factor));
                    min = queue.get(count);
                    queue.sort(Comparator.comparing(Process::getArrival_time));
                }

                if (dieList.contains(q.get(j))) {
                    q.remove(j);
                } else if (q.get(j) != min) {
                    q.add(q.get(j));
                }

                if (!q.get(j).visited) {
                    q.get(j).visited = true;
                    if (q.get(j) != min) {
                        run(j, first);
                    }
                } else {
                    if (q.get(j) != min) {
                        run(j, !first);
                    }
                }
                print(result);
                System.out.print("Queue ->  ");
                for (int k = 0; k < q.size(); k++) {
                    System.out.print(q.get(k).name + " ");
                }
                if (q.get(j) == min) {
                    MinFlag = true;
                    run(j, !first);
                    MinFlag = false;
                    print(result);
                    System.out.print("Queue ->  ");
                    for (int k = 0; k < q.size(); k++) {
                        System.out.print(q.get(k).name + " ");
                    }
                } else if (q.get(j).burst_time != 0) {
                    if (q.get(q.indexOf(min)).burst_time != 0) {
                        q.add(q.get(q.indexOf(min)));
                    }
                }
                if (q.get(j).burst_time == 0) {
                    dieList.add(q.get(j));
                    queue.get(queue.indexOf(q.get(j))).end_time = t;
                }
                qSum = queue.stream().mapToInt(Process -> Process.getQuantum()).sum();
                System.out.println("");
                System.out.println("T == " + t);
                T_History.add(t);
            }
            System.out.print("Wait List :: ");
            for (int k = 0; k < wait.size(); k++) {
                System.out.print(wait.get(k).name + " ");
            }
            System.out.print("\t\tDie List :: ");
            for (int k = 0; k < dieList.size(); k++) {
                System.out.print(dieList.get(k).name + " ");
            }

            print(result);
            System.out.println("T History -> " + T_History);

            for (int k = 0; k < q.size(); k++) {
                total_ta += queue.get(queue.indexOf(q.get(k))).ta_time += T_History.get(k + 1) - T_History.get(k);
            }
            for (int k = 0; k < queue.size(); k++) {
                total_wait += queue.get(k).wait_time += queue.get(k).end_time - queue.get(k).ta_time;
            }
            print(result);
            ArrayList<Process> qq = new ArrayList<>();
            // for graphical representaion
            for (int k = 0; k < q.size(); k++) {
                Process P = new Process();
                P.name = q.get(k).name;
                P.start_time = T_History.get(k);
                P.end_time = T_History.get(k + 1);
                P.color = q.get(k).color;
                qq.add(P);
            }
            return qq;
        }
        return queue;
    }
    boolean MinFlag = false;

    public void run(int i, boolean first) {
        int diff = 0, remainder = 0;
        if (first) {
            if (i == 0) {
                diff = q.get(i + 1).arrival_time - q.get(i).arrival_time;
                t = q.get(i).arrival_time;
            } else {
                diff = (int) Math.ceil((double) q.get(i).Quantum / 2);
                if (q.get(i).getAG_Factor() < q.get(i + 1).getAG_Factor()) {
                    diff = q.get(i).Quantum;
                }
            }
        } else {
            diff = (int) Math.ceil((double) q.get(i).Quantum / 2);
        }
        if (q.get(i).burst_time <= diff) {
            diff = q.get(i).burst_time;
            q.get(i).burst_time -= diff;
            q.get(i).Quantum = 0;
            q.get(i).visited = true;
            t += diff;
            return;
        }
        if (MinFlag) {
            diff = q.get(i).Quantum;
        }
        q.get(i).burst_time -= diff;

        if (q.get(i).burst_time < 0) {
            t += q.get(i).burst_time + diff;
            q.get(i).burst_time = 0;
            q.get(i).Quantum = 0;
            return;
        } else if (q.get(i).burst_time == 0) {
            q.get(i).Quantum = 0;
            t += diff;
            return;
        }

        remainder = q.get(i).Quantum - diff;
        if (remainder == 0) {
            q.get(i).Quantum += (int) Math.ceil((double) ((queue.stream().mapToInt(Process -> Process.getQuantum()).sum()) / ((queue.size() - 1) * 10.0)));
        } else {
            q.get(i).Quantum += remainder;
        }
        t += diff;
    }

    public void print(int result) {
        if (result != 2) {
            System.out.println("\nProcess Execution Order : \n");
        }
        if (result == 1) {
            System.out.println("Process |arrival |burst |start |end   |waiting |turnaround |");
        } else if (result == 2) {
            System.out.println("Process |arrival |burst |start |end   |waiting |turnaround |");
        } else if (result == 3) {
            System.out.println("Process |arrival |burst |priority |start |end   |waiting |turnaround |");
        } else if (result == 4) {
            System.out.println("Process |arrival |burst |priority |Quantum  |AG       |start |end   |waiting |turnaround |");
        }
        for (int i = 0; i < queue.size(); i++) {
            System.out.printf("%-8s|", queue.get(i).name);
            System.out.printf("%-8s|", queue.get(i).arrival_time);
            System.out.printf("%-6s|", queue.get(i).burst_time);
            if (result == 3 || result == 4) {
                System.out.printf("%-9s|", queue.get(i).o_priority);
            }
            if (result == 4) {
                System.out.printf("%-9s|", queue.get(i).Quantum);
            }
            if (result == 4) {
                System.out.printf("%-9s|", queue.get(i).AG_Factor);
            }
            System.out.printf("%-6s|", queue.get(i).start_time);
            System.out.printf("%-6s|", queue.get(i).end_time);
            System.out.printf("%-8s|", queue.get(i).wait_time);
            System.out.printf("%-11s|", queue.get(i).ta_time);
            System.out.println("");
        }
        System.out.println("\nTotal waiting time = " + total_wait);
        System.out.println("Average waiting time = " + total_wait / nProcesses);
        System.out.println("Total turnaround time = " + total_ta);
        System.out.println("Average turnaround time = " + total_ta / nProcesses);
    }
}
