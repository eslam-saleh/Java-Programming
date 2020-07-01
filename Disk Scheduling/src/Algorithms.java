
import java.util.*;

public class Algorithms {

    private int result, initial_start, THM = 0, start = 0, end = 200;
    private String[] q;
    ArrayList<Integer> queue = new ArrayList<Integer>();

    public Algorithms(int result, int initial_start, String seq) {
        this.result = result;
        this.initial_start = initial_start;
        seq = seq.replaceAll("\\s", "");
        q = seq.split(",");
        for (String i : q)
            queue.add(Integer.parseInt(i));
        queue.add(0, initial_start);
    }

    public void scheduling() {
        if (result == 1) {
            for (int i = 1; i < queue.size(); i++)
                THM += Math.abs(queue.get(i) - queue.get(i - 1));
            print();
        }
        if (result == 2) {
            int count1 = initial_start, count2 = initial_start;
            ArrayList<Integer> tmqQ = new ArrayList<Integer>();
            tmqQ.add(initial_start);
            while (tmqQ.size() != queue.size()) {
                count1++;
                count2--;
                if (queue.contains(count1) && !tmqQ.contains(count1)) {
                    tmqQ.add(count1);
                    count2 = count1;
                    continue;
                } else if (queue.contains(count2) && !tmqQ.contains(count2)) {
                    tmqQ.add(count2);
                    count1 = count2;
                    continue;
                }
            }
            queue = tmqQ;
            for (int i = 1; i < queue.size(); i++)
                THM += Math.abs(queue.get(i) - queue.get(i - 1));
            print();
        }
        if (result == 3) {
            queue.add(0, start);
            ArrayList<Integer> fstHalf = new ArrayList<Integer>();
            ArrayList<Integer> sndHalf = new ArrayList<Integer>();
            for (int i : queue)
                if (i <= initial_start)
                    fstHalf.add(i);
                else
                    sndHalf.add(i);
            Collections.sort(fstHalf);
            Collections.reverse(fstHalf);
            Collections.sort(sndHalf);
            queue.clear();
            queue.addAll(fstHalf);
            queue.addAll(sndHalf);
            for (int i = 1; i < queue.size(); i++)
                THM += Math.abs(queue.get(i) - queue.get(i - 1));
            print();
        }

        if (result == 4) {
            queue.add(0, start);
            queue.add(queue.size() - 1, end);
            ArrayList<Integer> fstHalf = new ArrayList<Integer>();
            ArrayList<Integer> sndHalf = new ArrayList<Integer>();
            for (int i : queue)
                if (i <= initial_start)
                    fstHalf.add(i);
                else
                    sndHalf.add(i);
            Collections.sort(fstHalf);
            Collections.reverse(fstHalf);
            Collections.sort(sndHalf);
            Collections.reverse(sndHalf);
            queue.clear();
            queue.addAll(fstHalf);
            queue.addAll(sndHalf);
            for (int i = 1; i < queue.size(); i++)
                THM += Math.abs(queue.get(i) - queue.get(i - 1));
            print();
        }
        if (result == 5) {
            ArrayList<Integer> fstHalf = new ArrayList<Integer>();
            ArrayList<Integer> sndHalf = new ArrayList<Integer>();
            for (int i : queue)
                if (i <= initial_start)
                    fstHalf.add(i);
                else
                    sndHalf.add(i);
            Collections.sort(fstHalf);
            Collections.reverse(fstHalf);
            Collections.sort(sndHalf);
            Collections.reverse(sndHalf);
            queue.clear();
            queue.addAll(fstHalf);
            queue.addAll(sndHalf);
            for (int i = 1; i < queue.size(); i++)
                THM += Math.abs(queue.get(i) - queue.get(i - 1));
            print();
        }
        if (result == 6) {
            Collections.sort(queue);
            queue.add(0, start);
            for (int i = 1; i < queue.size(); i++)
                THM += Math.abs(queue.get(i) - queue.get(i - 1));
            print();
        }
        /**
         * Obviously our new algorithm is the best it cases always shortest seek time
         * which returns mainly to 2 points:
         * first the same distance between any two points reached just once, so
         * no repetition occurs never ,second reaching cylinders in ascending way
         * is much more easier to the disk.
         * **/
    }

    public void print() {
        for (int i : queue)
            System.out.print(i + " -> ");
        System.out.println("End\nTotal head movement = " + THM);
    }
}