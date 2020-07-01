package com.algo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Welcome extends JFrame {
    private JPanel panel1;
    private JButton ADDButton;
    private JButton removeButton;
    private JButton generateButton;
    private JTable edgeTable;
    private JButton ADDVetrexButton;
    private JButton REMOVEVertexButton;
    private JTable vertexTable;
    private JComboBox isDirected;
    private DefaultTableModel edgeTableModel;
    private DefaultTableModel vertexTableModel;

    Welcome() {
        edgeTableModel = new DefaultTableModel();
        vertexTableModel = new DefaultTableModel();
        setTitle("DataForm");
        setSize(800, 600);
        add(panel1);
        edgeTable.setModel(edgeTableModel);
        edgeTableModel.addColumn("From");
        edgeTableModel.addColumn("To");
        edgeTableModel.addColumn("Weight");
        String s1[]={"Un Directed Graph","Directed Graph"};
        isDirected.addItem("UnDirectedGraph");
        isDirected.addItem("DirectedGraph");
        isDirected.setEnabled(true);
        isDirected.setSelectedItem(0);
        vertexTable.setModel(vertexTableModel);
        vertexTableModel.addColumn("Vertex Name");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        btnActions();


    }

    private void btnActions() {
        ADDVetrexButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                vertexTableModel.insertRow(vertexTableModel.getRowCount(), new Object[]{""});
            }
        });
        REMOVEVertexButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int[] rows = vertexTable.getSelectedRows();
                for (int i = 0; i < rows.length; i++) {
                    vertexTableModel.removeRow(rows[i] - i);
                }
            }
        });
        ADDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                edgeTableModel.insertRow(edgeTableModel.getRowCount(), new Object[]{ "", "",""});
            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int[] rows = edgeTable.getSelectedRows();
                for (int i = 0; i < rows.length; i++) {
                    edgeTableModel.removeRow(rows[i] - i);
                }
            }
        });
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ArrayList<String> vertices = new ArrayList<>();
                for (int i = 0; i < vertexTable.getRowCount(); i++) {
                    String vert=vertexTableModel.getValueAt(i, 0).toString().trim();
                    if(vertices.contains(vert)){
                        JOptionPane.showMessageDialog(new JFrame(), "u put vertex '"+vert+"' more than one time", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if(vert.isEmpty()){
                        JOptionPane.showMessageDialog(new JFrame(), "u may forget to press enter or put empty vertex at index "+i+" in vertices table", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    vertices.add(vert);

                }
                ArrayList<Edge> edges = new ArrayList<>();
                for (int i = 0; i < edgeTable.getRowCount(); i++) {
                    int weight;
                    if(edgeTableModel.getValueAt(i,2).toString().trim().isEmpty())weight=0;
                    else{
                        try{
                            weight=Integer.parseInt(edgeTableModel.getValueAt(i,2).toString());
                        }catch (Exception e){
                            JOptionPane.showMessageDialog(new JFrame(), "weight mus be int value at index "+i+" in edges table", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                    }
                    if( edgeTableModel.getValueAt(i, 0).toString().trim().isEmpty()||edgeTableModel.getValueAt(i, 1).toString().trim().isEmpty()){
                        JOptionPane.showMessageDialog(new JFrame(), "edges shouldn't be empty at index "+i+" in edges table", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if( !vertices.contains(edgeTableModel.getValueAt(i, 0).toString().trim())||!vertices.contains(edgeTableModel.getValueAt(i, 1).toString().trim())){
                        JOptionPane.showMessageDialog(new JFrame(), "one or two vertices not found in vertices table at index "+i+" in edges table", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if(isDirected.getSelectedIndex()==0){
                        edges.add(new Edge(
                                edgeTableModel.getValueAt(i, 0).toString().trim(),
                                edgeTableModel.getValueAt(i, 1).toString().trim(),
                                false,
                                weight
                        ));
                    }else{
                        edges.add(new Edge(
                                edgeTableModel.getValueAt(i, 0).toString().trim(),
                                edgeTableModel.getValueAt(i, 1).toString().trim(),
                                true,
                                weight
                        ));
                    }
                }
                if(edges.isEmpty()){
                    JOptionPane.showMessageDialog(new JFrame(), "edges shouldn't be empty", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Matrices matrices = null;

                    matrices=new Matrices(vertices,edges);
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }
                Matrices finalMatrices = matrices;
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            Result result = new Result(finalMatrices);
                            result.setVisible(true);
                        }
                    });

                /*assert matrices != null;
                Hamilton ham = new Hamilton(vertices, matrices.getAdj());
                EulerUndirected euler = new EulerUndirected(vertices, edges);*/
            }
        });

    }

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Welcome welcome = new Welcome();
                welcome.setVisible(true);
            }
        });
    }
}
