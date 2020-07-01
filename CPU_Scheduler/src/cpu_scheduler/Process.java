/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpu_scheduler;

import java.awt.Color;
import java.awt.Paint;
import java.text.DateFormat.Field;

public class Process implements Comparable<Process> {

    String name;
    int arrival_time, burst_time, priority, o_priority, Quantum, AG_Factor = 0;
    int start_time, end_time, wait_time = 0, ta_time = 0 ,Current = 0;
    boolean visited = false, done = false;
    Color color;

    public int getArrival_time() {
        return arrival_time;
    }

    public int getAG_Factor() {
        return AG_Factor;
    }

    public int getBurst_time() {
        return burst_time;
    }

    public int getQuantum() {
        return Quantum;
    }

    public int getStart_time() {
        return start_time;
    }

    public Process(Process p) {
        this.name = p.name;
        this.start_time = p.start_time;
        this.end_time = p.end_time;
    }

    public Process() {
    }

    public Process(String name, int arrival_time, int burst_time, int priority, int Quantum, String c) {
        this.name = name;
        this.arrival_time = arrival_time;
        this.burst_time = burst_time;
        this.Current = burst_time;
        this.o_priority = priority;
        this.priority = priority;
        this.Quantum = Quantum;
        AG_Factor = arrival_time + burst_time + priority;

        try {
            java.lang.reflect.Field field = Color.class.getField(c);
            color = (Color) field.get(null);
        } catch (Exception e) {
            color = null; // Not defined
        }
    }

    public void Incre_waiting() {
        this.wait_time += 1;
    }

    public void Dicre_Current() {
        this.Current -= 1;
    }

    public void Calc_Turnaround() {
        this.ta_time = this.wait_time + this.burst_time;
    }

    public int compareTo(Process o) {
        if (this.arrival_time < o.arrival_time) {
            return -1;
        } else if (this.arrival_time == o.arrival_time) {
            if (this.priority < o.priority) {
                return -1;
            } else if (this.priority == o.priority) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }
}
