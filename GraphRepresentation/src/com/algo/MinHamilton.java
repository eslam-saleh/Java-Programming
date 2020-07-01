package com.algo;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

import java.util.ArrayList;

public class MinHamilton {
    private int V;
    private ArrayList<String> vertices;
    private int[][] Cost;
    private ArrayList<Edge> edges;
    private String output="";

    public String getOutput() {
        return output;
    }



    MinHamilton(ArrayList<String> vertices, int[][] adj, ArrayList<Edge> ed) {
        this.vertices = vertices;
        V = vertices.size();
        int[][] graph;
        graph = adj;
        edges = ed;
        Cost = new int[V][V];
        for (int i = 0; i < V; i++)
            for (int j = 0; j < V; j++)
                Cost[i][j] = 0;
        int to, from;
        for (int i = 0; i < edges.size(); i++) {
            to = getV(edges.get(i).to, vertices);
            from = getV(edges.get(i).from, vertices);
            Cost[to][from] = edges.get(i).weight;
            Cost[from][to] = edges.get(i).weight;
        }
        test(graph);
    }



    public static int getV(String v, ArrayList<String> vertices) {
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).equals(v))
                return i;
        }
        return 0;
    }

    private void test(int[][] graph) {
        hamCycle(graph);
    }

    private boolean isMin(int v, int[][] graph, int[] path, int pos) {
        if (graph[path[pos - 1]][v] == 0)
            return false;
        int min = v;
        for (int i = 0; i < V; i++) {
            if ((graph[path[pos - 1]][i] != 0) && (Cost[path[pos - 1]][i] < Cost[path[pos - 1]][min]) && !Exist(i, path))
                return false;
        }
        for (int i = 0; i < pos; i++) {
            if (path[i] == min)
                return false;
        }
        return true;
    }

    public static boolean Exist(int num, int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == num)
                return true;
        }
        return false;
    }

    private boolean hamCycleUtil(int[][] graph, int[] path, int pos) {
        if (pos == V) {
            return graph[path[pos - 1]][path[0]] == 1;
        }
        int MinV = -1;
        for (int v = 1; v < V; v++) {
            if (isMin(v, graph, path, pos)) {
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



    private Edge get(String s1, String s2, ArrayList<Edge> edges) {
        for (int i = 0; i < edges.size(); i++) {
            if ((edges.get(i).to.equals(s1) && edges.get(i).from.equals(s2)) || (edges.get(i).to.equals(s2) && edges.get(i).from.equals(s1)))
                return edges.get(i);
        }
        return null;
    }

    private void printSolution(int[] path) {
        System.out.println("Hamiltonian Cycle::");
        //a b c d f
        for (int i = 0; i < V; i++){
            output+=vertices.get(path[i]) + " -> ";
        }

        output+=vertices.get(path[0]);

    }
}
