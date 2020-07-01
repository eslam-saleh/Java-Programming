package com.algo;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;



public class Result extends JFrame {
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JPanel graphPanel;
    private JTable adjResultTable;
    private JTable repResultTable;
    private JTable incResultTable;
    private JTable adjListResultTable;
    private JPanel minGraphPannel;
    private JPanel eulerGraph;
    private JPanel graphColoring;
    private JLabel msg;
    private JPanel hamiltonGraph;
    private JPanel minHamiltonGraph;
    private JPanel DijkstraTab;
    private JPanel Dijkstra;
    private JPanel MaxFlowTab;
    private JPanel maxFlow;
    private JLabel maxFlowValue;
    private JLabel allPathesMaxFlow;
    private JButton nextEdgeInMaxFlow;
    private JButton nextEdgeInDij;
    private JTextField allPaths;
    private JTextField totalFlow;
    private DefaultTableModel adjTableModel;
    private DefaultTableModel repTableModel;
    private DefaultTableModel incTableModel;
    private DefaultTableModel adjListModel;
    private Matrices matrices;
    int dijCount =1;
    int maxFlowCount=1;
    Result(Matrices matrices) {
        this.matrices = matrices;
        adjTableModel = new DefaultTableModel();
        repTableModel = new DefaultTableModel();
        incTableModel = new DefaultTableModel();
        adjListModel = new DefaultTableModel();
        adjResultTable.setModel(adjTableModel);
        repResultTable.setModel(repTableModel);
        incResultTable.setModel(incTableModel);
        adjListResultTable.setModel(adjListModel);
        adjResultTable.setEnabled(false);
        repResultTable.setEnabled(false);
        incResultTable.setEnabled(false);
        adjListResultTable.setEnabled(false);
        setTitle("ResultForm");
        setSize(800, 600);
        add(panel1);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMatricesIntoTables();
        setGraph();
        setDijGraph();
        setMaxFlowGraph();
        setMinGraph();
        setEulerGraph();
        setGraphColoring();
        setHamiltonGraph();
        setMinHamiltonGraph();
    }

    private void setMaxFlowGraph() {
        this.maxFlow.setLayout(new BorderLayout());
        if (!matrices.getEdges().get(0).isDirected) {
            JLabel label2 = new JLabel("<html><h1>cannot be generate because it's undirected graph</h1></html>");
            this.maxFlow.add(label2);
            return;
        }
        MaxFlow maxFlowObj = new MaxFlow(matrices);
        allPathesMaxFlow.setText("All Paths: "+maxFlowObj.allPaths);
        maxFlowValue.setText("Total Flow:  "+maxFlowObj.totalFlow);
       int noPaths= maxFlowObj.myPaths.size();
        String[] V = new String[maxFlowObj.getMinGraph().getVertexCount()];
        Edge[] E = new Edge[maxFlowObj.getMinGraph().getEdgeCount()];
        maxFlowObj.getMinGraph().getVertices().toArray(V);
        maxFlowObj.getMinGraph().getEdges().toArray(E);
        Arrays.sort(E,Comparator.comparing(s->s.flow));
        Graph<String, Edge> tempGraph = new SparseMultigraph<>();
        tempGraph.addVertex(V[0]);
        tempGraph.addVertex(V[V.length-1]);
        Layout<String, Edge> layout3 = new CircleLayout<>(tempGraph);
        VisualizationViewer<String, Edge> vv3 = new VisualizationViewer<>(layout3);
        vv3.getRenderContext().setVertexLabelTransformer(String::valueOf);
        vv3.getRenderContext().setEdgeLabelTransformer(s -> String.valueOf(s.flow));
        final DefaultModalGraphMouse<String, Number> graphMouse3 = new DefaultModalGraphMouse<>();
        vv3.setGraphMouse(graphMouse3);
        graphMouse3.setMode(ModalGraphMouse.Mode.PICKING);
        maxFlow.add(vv3, BorderLayout.NORTH);
        nextEdgeInMaxFlow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maxFlow.removeAll();
                revalidate();
                repaint();
                if(maxFlowCount<noPaths+1)
                {
                    for (int i=0 ; i< maxFlowObj.myPaths.size();i++) System.out.println(maxFlowObj.myPaths.get(i));
                    String[] arrOfV=maxFlowObj.myPaths.get(maxFlowCount-1).substring(9).split("->");

                    for (int i = arrOfV.length - 1; i > 0; i--) {
                            tempGraph.addVertex(arrOfV[i]);
                            if(tempGraph.findEdge(arrOfV[i],arrOfV[i-1])!=null) continue;
                            Edge edge = new Edge(arrOfV[i],arrOfV[i-1],true,maxFlowObj.getMinGraph().findEdge(arrOfV[i],arrOfV[i-1]).flow);
                            tempGraph.addEdge(edge, arrOfV[i], arrOfV[i-1], EdgeType.DIRECTED);
                    }
                    maxFlowCount++;
                }
                else {
                    for (Edge edge : E) {
                        if (edge.flow.charAt(0) == '0') tempGraph.addEdge(edge, edge.from, edge.to, EdgeType.DIRECTED);
                    }
                    nextEdgeInMaxFlow.setEnabled(false);
                }
                Layout<String, Edge> layout3 = new CircleLayout<>(tempGraph);
                VisualizationViewer<String, Edge> vv3 = new VisualizationViewer<>(layout3);
                vv3.getRenderContext().setVertexLabelTransformer(String::valueOf);
                vv3.getRenderContext().setEdgeLabelTransformer(s -> String.valueOf(s.flow));
                final DefaultModalGraphMouse<String, Number> graphMouse3 = new DefaultModalGraphMouse<>();
                vv3.setGraphMouse(graphMouse3);
                graphMouse3.setMode(ModalGraphMouse.Mode.PICKING);
                maxFlow.add(vv3, BorderLayout.NORTH);
            }
        });
    }

    private void setDijGraph() {
        Dijkstra dijkstraObj = new Dijkstra(matrices);
        this.Dijkstra.setLayout(new BorderLayout());
        String[] V = new String[dijkstraObj.getMinGraph().getVertexCount()];
        dijkstraObj.getMinGraph().getVertices().toArray(V);
        ArrayList<Edge> E = new ArrayList<>(dijkstraObj.getMinGraph().getEdges());
        E.sort(Comparator.comparing(s -> s.totalCost));
        Graph<String, Edge> tempGraph = new SparseMultigraph<>();
        tempGraph.addVertex(V[0]);
        Layout<String, Edge> layout3 = new CircleLayout<>(tempGraph);
        VisualizationViewer<String, Edge> vv3 = new VisualizationViewer<>(layout3);
        vv3.getRenderContext().setVertexLabelTransformer(String::valueOf);
        vv3.getRenderContext().setEdgeLabelTransformer(s -> String.valueOf(s.weight));
        final DefaultModalGraphMouse<String, Number> graphMouse3 = new DefaultModalGraphMouse<>();
        vv3.setGraphMouse(graphMouse3);
        graphMouse3.setMode(ModalGraphMouse.Mode.PICKING);
        Dijkstra.add(vv3, BorderLayout.NORTH);
        nextEdgeInDij.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dijkstra.removeAll();
                revalidate();
                repaint();
                //tempGraph.addVertex(V[dijCount]);
                if (matrices.getEdges().get(0).isDirected) tempGraph.addEdge(E.get(dijCount - 1), E.get(dijCount - 1).from, E.get(dijCount - 1).to,EdgeType.DIRECTED);
                else tempGraph.addEdge(E.get(dijCount - 1), E.get(dijCount - 1).from, E.get(dijCount - 1).to);
                dijCount++;
                Layout<String, Edge> layout3 = new CircleLayout<>(tempGraph);
                VisualizationViewer<String, Edge> vv3 = new VisualizationViewer<>(layout3);
                vv3.getRenderContext().setVertexLabelTransformer(String::valueOf);
                vv3.getRenderContext().setEdgeLabelTransformer(s -> String.valueOf(s.weight));
                final DefaultModalGraphMouse<String, Number> graphMouse3 = new DefaultModalGraphMouse<>();
                vv3.setGraphMouse(graphMouse3);
                graphMouse3.setMode(ModalGraphMouse.Mode.PICKING);
                Dijkstra.add(vv3, BorderLayout.NORTH);
                if(dijCount ==V.length)
                    nextEdgeInDij.setEnabled(false);
            }
        });
    }

    private void setHamiltonGraph() {

        hamiltonGraph.setLayout(new BorderLayout());
        if (matrices.getEdges().get(0).isDirected) {
            JLabel label2 = new JLabel("<html><h1>cannot be generate because it's directed graph</h1></html>");
            hamiltonGraph.add(label2);
            return;
        }
        Hamilton hamilton = new Hamilton(matrices.getVertices(), matrices.getAdj(), matrices.getEdges());
        JLabel label2 = new JLabel("<html><h3>" + hamilton.getOutput() + "</h1></html>");
        hamiltonGraph.add(label2);

    }

    private void setMinHamiltonGraph() {
        minHamiltonGraph.setLayout(new BorderLayout());
        if (matrices.getEdges().get(0).isDirected) {
            JLabel label2 = new JLabel("<html><h1>cannot be generate because it's directed graph</h1></html>");
            minHamiltonGraph.add(label2);
            return;
        }
        MinHamilton minHamilton = new MinHamilton(matrices.getVertices(), matrices.getAdj(), matrices.getEdges());
        JLabel label2 = new JLabel("<html><h3>" + minHamilton.getOutput() + "</h1></html>");
        minHamiltonGraph.add(label2);
    }

    private void setGraphColoring() {
        GraphColoring graphColoring = new GraphColoring(matrices);
        Layout<String, Edge> layout3 = new CircleLayout<>(matrices.getColoredGraph());
        VisualizationViewer<String, Edge> vv3 = new VisualizationViewer<>(layout3);
        Transformer<String, Paint> vertexPaint1 = new Transformer<String, Paint>() {

            @Override
            public Paint transform(String s) {
                return graphColoring.getResult().get(matrices.getVertices().indexOf(s));
            }
        };
        vv3.getRenderContext().setVertexFillPaintTransformer(vertexPaint1);
        //layout3.setSize(new Dimension(770, 570));
        //vv3.setPreferredSize(new Dimension(350, 350));

        vv3.getRenderContext().setVertexLabelTransformer(String::valueOf);
        vv3.getRenderContext().setEdgeLabelTransformer(s -> String.valueOf(s.weight));
        final DefaultModalGraphMouse<String, Number> graphMouse3 = new DefaultModalGraphMouse<>();
        vv3.setGraphMouse(graphMouse3);
        graphMouse3.setMode(ModalGraphMouse.Mode.PICKING);

        this.graphColoring.setLayout(new BorderLayout());
        this.graphColoring.add(vv3, BorderLayout.NORTH);
    }

    private void setEulerGraph() {
        eulerGraph.setLayout(new BorderLayout());
        EulerUndirected eulerUndirected = new EulerUndirected(matrices.getVertices(), matrices.getEdges());
        JLabel label2;
        if(matrices.getEdges().get(0).isDirected){
            label2 = new JLabel("<html><h1>cannot be generate because it's directed graph</h1></html>");
            eulerGraph.add(label2);
            return;
        }

        switch (eulerUndirected.getCurrPath()) {
            case 0:
                label2 = new JLabel("<html><h1>graph is not Eulerian</h1></html>");
                eulerGraph.add(label2);
                return;
            case 1:
                label2 = new JLabel("<html><h2>graph has an Euler path</h2>" +
                        "<h3>"+eulerUndirected.getOutput()+"</h3></html>");
                eulerGraph.add(label2);
                break;
            case 2:
                label2 = new JLabel("<html><h2>graph has an Euler cycle</h2>" +
                        "<h3>"+eulerUndirected.getOutput()+"</h3></html>");
                eulerGraph.add(label2);
                break;
        }
        //JLabel label3=new JLabel("<html><h3>"+eulerUndirected.getOutput()+"</h3></html>");
       // eulerGraph.add(label3);


    }

    private void setMinGraph() {
        /***/
        MinimumSpanningTree minimumSpanningTree = new MinimumSpanningTree(matrices);
        minGraphPannel.setLayout(new BorderLayout());
        if (matrices.getEdges().get(0).isDirected) {
            JLabel label2 = new JLabel("<html><h1>cannot be generate because it's directed graph</h1></html>");
            minGraphPannel.add(label2);
            return;
        }


        Layout<String, Edge> layout3 = new CircleLayout<>(minimumSpanningTree.getMinGraph());
        VisualizationViewer<String, Edge> vv3 = new VisualizationViewer<>(layout3);

        //layout3.setSize(new Dimension(770, 570));
        //vv3.setPreferredSize(new Dimension(350, 350));

        vv3.getRenderContext().setVertexLabelTransformer(String::valueOf);
        vv3.getRenderContext().setEdgeLabelTransformer(s -> String.valueOf(s.weight));
        final DefaultModalGraphMouse<String, Number> graphMouse3 = new DefaultModalGraphMouse<>();
        vv3.setGraphMouse(graphMouse3);
        graphMouse3.setMode(ModalGraphMouse.Mode.PICKING);


        minGraphPannel.add(vv3, BorderLayout.NORTH);
    }

    private void setMatricesIntoTables() {
        matrices.setAdjTable(adjTableModel);
        matrices.setRepTable(repTableModel);
        matrices.setIncTable(incTableModel);
        matrices.setAdjListsTable(adjListModel);
    }

    private void setGraph() {

        Layout<String, Edge> layout3 = new CircleLayout<>(matrices.getRepGraph());
        VisualizationViewer<String, Edge> vv3 = new VisualizationViewer<>(layout3);

        //layout3.setSize(new Dimension(770, 570));
        //vv3.setPreferredSize(new Dimension(350, 350));

        vv3.getRenderContext().setVertexLabelTransformer(String::valueOf);
        vv3.getRenderContext().setEdgeLabelTransformer(s -> String.valueOf(s.weight));
        final DefaultModalGraphMouse<String, Number> graphMouse3 = new DefaultModalGraphMouse<>();
        vv3.setGraphMouse(graphMouse3);
        graphMouse3.setMode(ModalGraphMouse.Mode.PICKING);

        graphPanel.setLayout(new BorderLayout());
        graphPanel.add(vv3, BorderLayout.NORTH);
    }
}
