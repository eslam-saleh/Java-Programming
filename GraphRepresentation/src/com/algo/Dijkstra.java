package com.algo;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.event.GraphEvent;
import edu.uci.ics.jung.graph.util.EdgeType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Dijkstra {
    private ArrayList<String> vertices;
    private Graph<String, Edge> minGraph = new SparseMultigraph<>();
    private boolean directed=false;
    public Dijkstra(Matrices matrices) {
        this.vertices = matrices.getVertices();
        ArrayList<Edge> edges;
        edges = matrices.getEdges();
        directed=edges.get(0).isDirected;
        Collections.sort(edges,Collections.reverseOrder());
        int[][] graph = new int[vertices.size()][vertices.size()];
        int indexFrom;
        int indexTo;
        int count=0;
        for (Edge edge : edges) {
            indexFrom = vertices.indexOf(edge.from);
            indexTo = vertices.indexOf(edge.to);
            graph[indexFrom][indexTo] = edge.weight;
            if(!edge.isDirected)
            {
                graph[indexTo][indexFrom] = edge.weight;
            }
            System.out.println(edge.weight);
        }
        dijkstra(graph);
    }
    public Graph<String, Edge> getMinGraph() {
        return minGraph;
    }


    int minDistance(int []dist, Boolean []sptSet) {
        int min = Integer.MAX_VALUE, min_index = -1;

        for (int v = 0; v < vertices.size(); v++)
            if (!sptSet[v] && dist[v] <= min) {
                min = dist[v];
                min_index = v;
            }

        return min_index;
    }


    void dijkstra(int[][] graph) {
        int[] dist = new int[vertices.size()];
        Boolean[] sptSet = new Boolean[vertices.size()];
        for (int i = 0; i < vertices.size(); i++) {
            dist[i] = Integer.MAX_VALUE;
            sptSet[i] = false;
        }
        dist[0] = 0;
        for (int count = 0; count < vertices.size() - 1; count++) {
            int u = minDistance(dist, sptSet);
            boolean changedBefore=false;
            sptSet[u] = true;
            for (int v = 0; v < vertices.size(); v++)
                if (!sptSet[v] && graph[u][v] != 0 && dist[u] != Integer.MAX_VALUE && dist[u] + graph[u][v] < dist[v]){
                    if(dist[v]<2000000)
                        changedBefore=true;
                    dist[v] = dist[u] + graph[u][v];
                    if(changedBefore){
                        minGraph.removeEdge(getIndex(v));
                        minGraph.removeVertex(getvertix(vertices.get(v)));
                    }
                    Edge e=new Edge(vertices.get(u)+" / cost : "+dist[u],vertices.get(v)+" / cost : "+dist[v],directed,graph[u][v],dist[v]);
                    if(directed)
                        minGraph.addEdge(e,vertices.get(u)+" / cost : "+dist[u],vertices.get(v)+" / cost : "+dist[v], EdgeType.DIRECTED);

                    else
                        minGraph.addEdge(e,vertices.get(u)+" / cost : "+dist[u],vertices.get(v)+" / cost : "+dist[v]);
                    minGraph.addVertex(vertices.get(v)+" / cost : "+dist[v]);
                }
        }


    }

    private String getvertix(String s) {
        for(String v : minGraph.getVertices()){
            if(v.charAt(0)==s.charAt(0))return v;
        }
        return "";
    }

    private Edge getIndex(int v) {
        for(Edge edge : minGraph.getEdges()){
            if(edge.to.equals(vertices.get(v)))return edge;
        }
        return null;
    }
}
