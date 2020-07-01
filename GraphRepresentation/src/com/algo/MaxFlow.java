package com.algo;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class MaxFlow {
    static int V = 0;
    public String allPaths;
    public String totalFlow;
    private ArrayList<String>vertices;
        private boolean directed=false;
        private Graph<String, Edge> minGraph = new SparseMultigraph<>();
    ArrayList<String>myPaths=new ArrayList<>();

    ArrayList<Edge> edges;

    public MaxFlow(Matrices matrices) {
        this.vertices = matrices.getVertices();
        ArrayList<Edge> edges;
        V=vertices.size();
        edges = matrices.getEdges();
        directed=edges.get(0).isDirected;
        allPaths="";
        Collections.sort(edges);
        int[][] graph = new int[vertices.size()][vertices.size()];
        int indexFrom;
        int indexTo;
        for (Edge edge : edges) {
            indexFrom = vertices.indexOf(edge.from);
            indexTo = vertices.indexOf(edge.to);
            graph[indexFrom][indexTo] = edge.weight;
            if(edge.isDirected){
                graph[indexTo][indexFrom] = 0;
            }
            if(!edge.isDirected)
            {
                graph[indexTo][indexFrom] = edge.weight;
            }
        }
        fordFulkerson(graph,vertices.get(0),vertices.get(vertices.size()-1));
    }
        boolean bfs(int[][] rGraph, String s, String t, int[] parent)
        {
            boolean[] visited = new boolean[V];
            for(int i=0; i<V; ++i)
                visited[i]=false;
            LinkedList<String> queue = new LinkedList<String>();
            queue.add(s);
            visited[vertices.indexOf(s)] = true;
            parent[vertices.indexOf(s)]=-1;

            while (queue.size()!=0)
            {
                String u = queue.poll();
                for (int v=0; v<V; v++)
                {
                    if (!visited[v] && rGraph[vertices.indexOf(u)][v] > 0)
                    {
                        queue.add(vertices.get(v));
                        parent[v] = vertices.indexOf(u);
                        visited[v] = true;
                    }
                }
            }
            return (visited[vertices.indexOf(t)]);
        }

        int fordFulkerson(int[][] graph, String s, String t)
        {
            int u, v;
            int[][] rGraph = new int[V][V];
            for (u = 0; u < V; u++)
                for (v = 0; v < V; v++)
                    rGraph[u][v] = graph[u][v];

            int[] parent = new int[V];

            int max_flow = 0;
            int noSteps=1;
            while (bfs(rGraph, s, t, parent))
            {
                int path_flow = Integer.MAX_VALUE;
                for (v=vertices.indexOf(t); v!=vertices.indexOf(s); v=parent[v])
                {
                    u = parent[v];
                    path_flow = Math.min(path_flow, rGraph[u][v]);
                }
                allPaths+="Path "+noSteps+" : "+t+"->";
                String temp="Path "+noSteps+" : "+t+"->";
                for (v=vertices.indexOf(t); v != vertices.indexOf(s); v=parent[v])
                {
                    u = parent[v];
                    rGraph[u][v] -= path_flow;
                    rGraph[v][u] += path_flow;
                    allPaths+=vertices.get(u)+"->";
                    temp+=vertices.get(u)+"->";

                }
                myPaths.add(temp);
                allPaths+= "max flow :"+path_flow+" , ";
                max_flow += path_flow;
                noSteps++;
            }
            for (int i=0;i<V;i++){

                for (int j=0;j<V;j++){
                    if(graph[i][j]!=0) {
                        Edge e=new Edge(vertices.get(i),vertices.get(j),directed,graph[i][j]-rGraph[i][j]+"/"+graph[i][j]);
                        if(directed) minGraph.addEdge(e,vertices.get(i),vertices.get(j), EdgeType.DIRECTED);
                        else minGraph.addEdge(e,vertices.get(i),vertices.get(j));
                    }

                }

            }
            totalFlow=Integer.toString(max_flow);
            return max_flow;
        }
        public Graph<String, Edge> getMinGraph() {

                return minGraph;
        }

    }

