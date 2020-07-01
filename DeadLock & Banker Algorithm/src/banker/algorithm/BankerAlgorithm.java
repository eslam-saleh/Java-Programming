package banker.algorithm;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class BankerAlgorithm {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        ArrayList<Integer> avail = new ArrayList<>();
        ArrayList<ArrayList<Integer>> max = new ArrayList<>();
        ArrayList<ArrayList<Integer>> alloc = new ArrayList<>();
        ArrayList<ArrayList<Integer>> need = new ArrayList<>();
        System.out.print("Enter the number of Processes : ");
        int n = in.nextInt();
        System.out.print("Enter the number of Resources : ");
        int m = in.nextInt();
        for (int i = 0; i < m; i++) {
            System.out.print("Enter the initial available of Resource " + (i + 1) + " : ");
            avail.add(in.nextInt());
        }
        for (int i = 0; i < n; i++) {
            ArrayList<Integer> tempMax = new ArrayList<>();
            ArrayList<Integer> tempAlloc = new ArrayList<>();
            ArrayList<Integer> tempNeed = new ArrayList<>();
            for (int j = 0; j < m; j++) {
                System.out.print("Enter the max need of Resource " + (j + 1) + " for Process" + (i + 1) + " : ");
                tempMax.add(in.nextInt());
                System.out.print("Enter the allocated Resource " + (j + 1) + " for Process" + (i + 1) + " : ");
                tempAlloc.add(in.nextInt());
                tempNeed.add(tempMax.get(j) - tempAlloc.get(j));
            }
            max.add(tempMax);
            alloc.add(tempAlloc);
            need.add(tempNeed);
        }
        System.out.println("\nInitial System :-");
        printStatus(avail, max, alloc, need, n, m);
        printOrder(banker(avail, max, alloc, need, n, m), n);
        
        String str = "";
        while (!str.equals("quit")) {            
            str = in.nextLine();
            String temp[] = str.split(" ");
            temp[0] = temp[0].toLowerCase();
            if(temp[0].equals("quit")){
                break;
            }
            else if(temp[0].equals("rq")) {
                int index = Integer.valueOf(temp[1]);
                if(index >= n) {
                    n++;
                    ArrayList<Integer> tempMax = new ArrayList<>();
                    ArrayList<Integer> tempAlloc = new ArrayList<>();
                    ArrayList<Integer> tempNeed = new ArrayList<>();
                    for (int i = 0; i < m; i++) {
                        int x = Integer.valueOf(temp[i + 2]);
                        tempMax.add(x);
                        tempAlloc.add(0);
                        tempNeed.add(x);
                    }
                    max.add(tempMax);
                    alloc.add(tempAlloc);
                    need.add(tempNeed);
                }
                else {
                    for (int i = 0; i < m; i++) {
                        max.get(index).set(i, max.get(index).get(i) + Integer.parseInt(temp[i + 2]) - need.get(index).get(i));
                        need.get(index).set(i, Integer.parseInt(temp[i + 2]));
                    }
                }
                printOrder(banker(avail, max, alloc, need, n, m), n);
            }
        }
    }
    
    public static ArrayList<Integer> banker(
        ArrayList<Integer> avail,
        ArrayList<ArrayList<Integer>> max,
        ArrayList<ArrayList<Integer>> alloc,
        ArrayList<ArrayList<Integer>> need,
        int n, int m)
    {
        ArrayList<Integer> avail2 = new ArrayList<>(avail);
        ArrayList<ArrayList<Integer>> max2 = new ArrayList<>(max.stream().map(x -> new ArrayList<>(x)).collect(Collectors.toList()));
        ArrayList<ArrayList<Integer>> alloc2 = new ArrayList<>(alloc.stream().map(x -> new ArrayList<>(x)).collect(Collectors.toList()));
        ArrayList<ArrayList<Integer>> need2 = new ArrayList<>(need.stream().map(x -> new ArrayList<>(x)).collect(Collectors.toList()));
        boolean safe_state = true;
        ArrayList<Integer> order = new ArrayList<>();
        for (int c = 0; c < n; c++) {
            for (int i = 0; i < n; i++) {
                safe_state = true;
                if(order.contains(i))
                    continue;
                for (int j = 0; j < m; j++) {
                    if(need2.get(i).get(j) > avail2.get(j))
                        safe_state = false;
                }
                if(safe_state) {
                    System.out.println("P" + i + " request is Accepted");
                    order.add(i);
                    for (int j = 0; j < m; j++) {
                        avail2.set(j, avail2.get(j) - need2.get(i).get(j));
                        alloc2.get(i).set(j, alloc2.get(i).get(j) + need2.get(i).get(j));
                        need2.get(i).set(j, 0);
                        avail2.set(j, avail2.get(j) + alloc2.get(i).get(j));
                        alloc2.get(i).set(j, 0);
                    }
                } else {
                    System.out.println("P" + i + " request is Denied");
                }
                printStatus(avail2, max2, alloc2, need2, n, m);
            }
            if(order.size() == n)
                return order;
        }
        return null;
    }
    
    public static void printStatus(
        ArrayList<Integer> avail,
        ArrayList<ArrayList<Integer>> max,
        ArrayList<ArrayList<Integer>> alloc,
        ArrayList<ArrayList<Integer>> need,
        int n, int m)
    {
        System.out.println("\nAvailable : ");
        System.out.println("_______________________________________________________");
        for (int i = 0; i < m; i++) {
            System.out.println("\tR" + (i+1) + " : " + avail.get(i));
        }
        for (int i = 0; i < n; i++) {
            System.out.print("Process : P" + i);
            System.out.println("\nAllocation : ");
            for (int j = 0; j < m; j++) {
                System.out.println("\tR" + (j+1) + " : " + alloc.get(i).get(j));
            }
            System.out.println("\nMaximum : ");
            for (int j = 0; j < m; j++) {
                System.out.println("\tR" + (j+1) + " : " + max.get(i).get(j));
            }
            System.out.println("\nNeed : ");
            for (int j = 0; j < m; j++) {
                System.out.println("\tR" + (j+1) + " : " + need.get(i).get(j));
            }
        }
        System.out.println("_______________________________________________________");
    }
    
    public static void printOrder(ArrayList<Integer> order, int n) {
        if(order == null){
            System.out.println("Unsafe State");
        } else {
            System.out.print("\nExecution order is : ");
            for (int i = 0; i < n; i++) {
                System.out.print("P" + order.get(i));
                if(i != order.size()-1)
                    System.out.print(" -> ");
                else
                    System.out.print("\n\n---> ");
            }
        }
    }
}
/*
5
3
3 3 2
7 0 5 1 3 0
3 2 2 0 2 0
9 3 0 0 2 2
2 2 2 1 2 1
4 0 3 0 3 2
*/