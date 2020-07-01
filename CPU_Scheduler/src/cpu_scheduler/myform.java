/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpu_scheduler;

import java.awt.Color;
import java.awt.Paint;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Scanner;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.GanttRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.chart.plot.CategoryPlot;

/**
 *
 * @author ESLAM
 */
public class myform extends javax.swing.JFrame {

    TaskSeriesCollection dataset = new TaskSeriesCollection();

    public myform() {
        initComponents();
    }
    private ArrayList<Process> Q = new ArrayList<>();
    private static final long serialVersionUID = 1L;

    /**
     * Creates new form myform
     */
    public myform(String applicationTitle, String chartTitle) {
        super(applicationTitle);
        Scanner in = new Scanner(System.in);
        int result = 0;
        System.out.println("1-SJF\t2-SRTF\t3-Priority\t4-AG");
        result = in.nextInt();
        ArrayList<Process> Q = new ArrayList<Process>();
        if (result == 1) {
            Algorithms SJF = new Algorithms(1);
            System.out.println();
            Q = SJF.scheduling();
        } else if (result == 2) {
            Algorithms SRTF = new Algorithms(2);
            System.out.println();
            Q = SRTF.scheduling();
        } else if (result == 3) {
            Algorithms priority = new Algorithms(3);
            System.out.println();
            Q = priority.scheduling();
        } else if (result == 4) {
            Algorithms AG = new Algorithms(4);
            System.out.println();
            Q = AG.scheduling();
        }

        JFreeChart chart = ChartFactory.createGanttChart(chartTitle, "Processes", "Burst_Time", createDataset(Q),
                false, true, false);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        DateAxis range = (DateAxis) plot.getRangeAxis();
        range.setDateFormatOverride(new SimpleDateFormat("SS"));
        int max = Q.stream().mapToInt(Process -> Process.getBurst_time()).sum();
        range.setMaximumDate(new Date(max + 20));

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        chartPanel.setDomainZoomable(true);
        setContentPane(chartPanel);

        MyRenderer renderer = new MyRenderer(Q);
        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.BLACK);
    }

    class MyRenderer extends GanttRenderer {

        private static final long serialVersionUID = 1L;
        private TaskSeriesCollection model;
        ArrayList<Process> q = new ArrayList<Process>();
        ArrayList<Process> qq = new ArrayList<Process>();

        public MyRenderer(ArrayList<Process> q) {
            super();
            this.q = q;

            for (int i = 0; i <  q.size(); i++) {
                boolean isDone = false;
                for (Process pr : qq) {
                    if (pr.name.equals(q.get(i).name)) {
                        q.remove(i);
                        i--;
                        isDone = true;
                        break;
                    }
                }
                if (isDone) {
                    continue;
                }
                qq.add(q.get(i));
            }
        }

        @Override
        public Paint getItemPaint(int row, int col) {
            for (int i = 0; i < q.size(); i++) {
                if (col == i) {
                    return q.get(i).color;
                }
            }
            return super.getItemPaint(row, col);
        }
    }

    private IntervalCategoryDataset createDataset(ArrayList<Process> Q) {
        ArrayList<Process> Wait = new ArrayList<Process>();
        ArrayList<Task> tasks = new ArrayList<Task>();
        Process P;
        for (int i = 0; i < Q.size(); i++) {
            String str = Q.get(i).name;
            boolean isContion = false;
            for (Process p : Wait) {
                if (p.name.equals(Q.get(i).name)) {
                    isContion = true;
                    break;
                }
            }
            final Task task = new Task(str, new SimpleTimePeriod(Q.get(i).start_time, Q.get(i).end_time));

            if (isContion) {
                for (Task t : tasks) {
                    if (t.getDescription().equals(task.getDescription())) {
                        t.addSubtask(new Task(str, new SimpleTimePeriod(Q.get(i).start_time, Q.get(i).end_time)));
                        break;
                    }
                }
            } else {
                task.addSubtask(new Task(str, new SimpleTimePeriod(Q.get(i).start_time, Q.get(i).end_time)));
                tasks.add(task);
            }
            Process pp = new Process(Q.get(i));
            Wait.add(pp);
        }
        for (int i = 0; i < tasks.size(); i++) {
            TaskSeries graph = new TaskSeries(tasks.get(i).getDescription());
            graph.add(tasks.get(i));
            dataset.add(graph);
        }
        return dataset;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(myform.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(myform.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(myform.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(myform.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                myform chart = new myform("Operating System", "CPU_Scheduler");
                chart.pack();
                chart.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
