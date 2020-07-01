package com.algo;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

public class Matrices {
    private ArrayList<String> vertices;

    public ArrayList<String> getVertices() {
        return vertices;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    private ArrayList<Edge> edges;


    private int[][] adj;
    private int[][] rep;
    private int[][] inc;



    private  LinkedList<String>adjLists[];
    private Graph<String, Edge> repGraph = new SparseMultigraph<>();

    public Graph<String, Edge> getColoredGraph() {
        return coloredGraph;
    }

    private Graph<String, Edge> coloredGraph = new SparseMultigraph<>();
    public Matrices(ArrayList<String> vertices, ArrayList<Edge> edges) {
        this.vertices = vertices;
        this.edges = edges;
        generateAdjMatrix();
        generateRepMatrix();
        generateIncMatrix();
        setAdjLists();
        generateAdjLists();
        setRepGraph();
    }
    private void setAdjLists(){
        adjLists = new LinkedList[vertices.size()];
        for(int i = 0; i < vertices.size(); i++) {
            adjLists[vertices.indexOf(vertices.get(i))] = new LinkedList();
        }
    }
    private void generateAdjLists(){
        for (int i = 0; i < edges.size(); i++){
            if(edges.get(i).isDirected){
                adjLists[vertices.indexOf(edges.get(i).from)].add(edges.get(i).to);
            }else {
                adjLists[vertices.indexOf(edges.get(i).from)].add(edges.get(i).to);
                if(!edges.get(i).from.equals(edges.get(i).to))
                    adjLists[vertices.indexOf(edges.get(i).to)].add(edges.get(i).from);
            }

        }
        //printAdjLists();
    }

    private void setRepGraph() {
        int id = 0;
        for (String v : vertices){
            repGraph.addVertex(v);
            coloredGraph.addVertex(v);
        }

        for (Edge edge : edges) {
            if (edge.isDirected) {
                repGraph.addEdge(edge, edge.from, edge.to, EdgeType.DIRECTED);
                coloredGraph.addEdge(edge, edge.from, edge.to, EdgeType.DIRECTED);
            } else {
                repGraph.addEdge(edge, edge.from, edge.to);
                coloredGraph.addEdge(edge, edge.from, edge.to);
            }
            id++;
        }
    }

    public Graph<String, Edge> getRepGraph() {
        return repGraph;
    }

    private void generateRepMatrix() {
        rep = new int[vertices.size()][vertices.size()];
        for (Edge edge : edges) {
            if(edge.isDirected)
                rep[vertices.indexOf(edge.from)][vertices.indexOf(edge.to)] ++;
            else{
                rep[vertices.indexOf(edge.from)][vertices.indexOf(edge.to)] ++;
                if(!edge.from.equals(edge.to))
                    rep[vertices.indexOf(edge.to)][vertices.indexOf(edge.from)] ++;
            }
        }
        //printRep();
    }

    public int[][] getAdj() {
        return adj;
    }

    public int[][] getRep() {
        return rep;
    }

    public int[][] getInc() {
        return inc;
    }

    public LinkedList<String>[] getAdjLists() {
        return adjLists;
    }

    private void generateAdjMatrix() {
        adj = new int[vertices.size()][vertices.size()];
        for (Edge edge : edges) {
            if(edge.isDirected){
                adj[vertices.indexOf(edge.from)][vertices.indexOf(edge.to)] = 1;
            }

            else{
                adj[vertices.indexOf(edge.from)][vertices.indexOf(edge.to)] = 1;
                adj[vertices.indexOf(edge.to)][vertices.indexOf(edge.from)] = 1;


            }
        }
        printAdj();
    }
    private void printAdj(){
        for (int i=0;i<adj.length;i++){
            for(int j=0;j<adj[0].length;j++){
                System.out.print(adj[i][j]+"  ");
            }
            System.out.println();
        }
    }
    private void generateIncMatrix() {
        inc = new int[vertices.size()][edges.size()];
        for (int i=0;i<edges.size();i++) {
            Edge edge=edges.get(i);
            inc[vertices.indexOf(edge.from)][i] ++;
            inc[vertices.indexOf(edge.to)][i] ++;
        }
        //printInc();
    }


    public void setIncTable(DefaultTableModel incTableModel) {
        incTableModel.addColumn("V\\E Name");
        for(int i=0;i<edges.size();i++)incTableModel.addColumn("E"+i);
        for (int i = 0; i < vertices.size(); i++) {
            Vector<String>row=new Vector<>();
            row.add(vertices.get(i));
            for (int j = 0; j < edges.size(); j++) {
                row.add(String.valueOf(inc[i][j]));
            }
            incTableModel.addRow(row);
        }
    }

    public void setAdjTable(DefaultTableModel adjTableModel) {
        adjTableModel.addColumn("V\\V Name");
        for(String v:vertices)adjTableModel.addColumn(v);
        for (int i = 0; i < vertices.size(); i++) {
            Vector<String>row=new Vector<>();
            row.add(vertices.get(i));
            for (int j = 0; j < vertices.size(); j++) {
                row.add(String.valueOf(adj[i][j]));
            }
            adjTableModel.addRow(row);
        }
    }

    public void setRepTable(DefaultTableModel repTableModel) {
        repTableModel.addColumn("V\\V Name");
        for(String v:vertices)repTableModel.addColumn(v);
        for (int i = 0; i < vertices.size(); i++) {
            Vector<String>row=new Vector<>();
            row.add(vertices.get(i));
            for (int j = 0; j < vertices.size(); j++) {
                row.add(String.valueOf(rep[i][j]));
            }
            repTableModel.addRow(row);
        }
    }

    public void setAdjListsTable(DefaultTableModel adjListsTableModel){
        adjListsTableModel.addColumn("Initial Vertex");
        adjListsTableModel.addColumn("Terminal Vertices");
        for(int i = 0; i < vertices.size(); i++){
            Vector<String>row=new Vector<>();
            //System.out.print("Vertex "+ vertices.get(i) + " is connected to:");
            row.add(vertices.get(i));
            int siz = adjLists[vertices.indexOf(vertices.get(i))].size();
            String connectedTo="";
            for (int j = 0; j < siz; j++){
                //System.out.print(adjLists[vertices.indexOf(vertices.get(i))].get(j) + " ");
                if(j<siz-1)
                    connectedTo+=adjLists[vertices.indexOf(vertices.get(i))].get(j) + ", ";
                else
                    connectedTo+=adjLists[vertices.indexOf(vertices.get(i))].get(j) ;
            }
            row.add(connectedTo);
            adjListsTableModel.addRow(row);
        }
    }
}
