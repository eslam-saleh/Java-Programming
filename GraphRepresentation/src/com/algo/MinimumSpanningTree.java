package com.algo;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class MinimumSpanningTree {
    private ArrayList<String> vertices;
    private ArrayList<Edge> edges;
    private int[] parents;
    private Graph<String, Edge> minGraph = new SparseMultigraph<>();
    private ArrayList<Edge> minEdges;

    public MinimumSpanningTree(Matrices matrices) {
        this.vertices = matrices.getVertices();
        this.edges = new ArrayList<>();
        this.edges = matrices.getEdges();
        Collections.sort(this.edges);
        parents = new int[vertices.size()];
        Arrays.fill(parents, -1);
        if (edges.size() != 0) {
            setMinSpanningParents();

            setMinGraph();

        }

    }

    public Graph<String, Edge> getMinGraph() {
        return minGraph;
    }

    private void setMinGraph() {
        for (String v : vertices)
            minGraph.addVertex(v);
        for (Edge edge : minEdges) {
            if (edge.isDirected) {
                minGraph.addEdge(edge, edge.from, edge.to, EdgeType.DIRECTED);
            } else {
                minGraph.addEdge(edge, edge.from, edge.to);
            }
        }
    }

    private int getParent(int i) {
        if (parents[i] < 0) return i;
        return getParent(parents[i]);
    }

    private void performUnion(int xParent, int yParent) {
        if (parents[xParent] <= parents[yParent]) {
            int tmp = parents[yParent];
            parents[yParent] = xParent;
            parents[xParent] += tmp;
        } else {
            int tmp = parents[xParent];
            parents[xParent] = yParent;
            parents[yParent] += tmp;
        }
    }

    private boolean checkVisited(ArrayList<Edge> edges, Edge edge) {
        for (Edge tmp : edges) {
            if (tmp.from.equals(edge.from) && tmp.to.equals(edge.to)) return true;
        }
        return false;
    }

    private void setMinSpanningParents() {
        int VSize = vertices.size();
        minEdges = new ArrayList<>();
        if(!edges.get(0).isDirected){
            int totalEdgesTook = 0;
            for (int i = 0; i < edges.size(); i++) {
                int xParent = getParent(vertices.indexOf(edges.get(i).from));
                int yParent = getParent(vertices.indexOf(edges.get(i).to));
                if (totalEdgesTook == (VSize - 1)) break;
                if (xParent == yParent)//is cycle
                    continue;
                totalEdgesTook++;
                minEdges.add(edges.get(i));
                performUnion(xParent, yParent);
            }
        }

    }

}
