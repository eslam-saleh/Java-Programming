package com.algo;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

import java.util.ArrayList;

class Hamilton {
    private int V;
    private ArrayList<String> vertices;
    //private ArrayList<Edge> Output=new ArrayList<>();
    private ArrayList<Edge> edges;
    //private Graph<String, Edge> hamiltonGraph = new SparseMultigraph<>();
    private String Output="";

    public String getOutput() {
        return Output;
    }


    Hamilton(ArrayList<String> vertices, int[][] adj, ArrayList<Edge> ed) {
        this.vertices = vertices;
        V = vertices.size();
        int[][] graph;
        graph = adj;
        edges=ed;
        test(graph);
    }



    private void test(int[][] graph) {
        hamCycle(graph);
    }

    private boolean isSafe(int v, int[][] graph, int[] path, int pos) {
        if (graph[path[pos - 1]][v] == 0)
            return false;

        for (int i = 0; i < pos; i++)
            if (path[i] == v)
                return false;

        return true;
    }

    private boolean hamCycleUtil(int[][] graph, int[] path, int pos) {
        if (pos == V) {
            return graph[path[pos - 1]][path[0]] == 1;
        }
        for (int v = 1; v < V; v++) {
            if (isSafe(v, graph, path, pos)) {
                path[pos] = v;
                if (hamCycleUtil(graph, path, pos + 1))
                    return true;
                path[pos] = -1;
            }
        }
        return false;
    }

    private void hamCycle(int[][] graph) {
        int[] path = new int[V];
        for (int i = 0; i < V; i++)
            path[i] = -1;

        path[0] = 0;
        if (!hamCycleUtil(graph, path, 1)) {
            System.out.println("\nSolution does not exist");
            return;
        }
        printSolution(path);
    }

    private Edge get(String s1,String s2,ArrayList<Edge>edges)
    {
        for (int i=0;i<edges.size();i++) {
            if ((edges.get(i).to.equals(s1) && edges.get(i).from.equals(s2)) || (edges.get(i).to.equals(s2) && edges.get(i).from.equals(s1)))
                return edges.get(i);
        }
        return null;
    }

    private void printSolution(int[] path) {
        System.out.println("Hamiltonian Cycle::");
        //a b c d f
        for (int i = 0; i < V; i++){
            Output+=vertices.get(path[i]) + " -> ";
        }

        Output+=vertices.get(path[0]);

        /*System.out.println("Hamiltonian Path::");
        for (int i = 0; i < V; i++)
            System.out.print(" " + vertices.get(path[i]) + " ");
        System.out.println();*/
    }
}
