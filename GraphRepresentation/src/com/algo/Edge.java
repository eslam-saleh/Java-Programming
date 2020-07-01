package com.algo;

public class Edge implements Comparable<Edge>{
    String from;
    String to;
    boolean isDirected;
    int weight;
    String flow;
    int totalCost;
    public Edge(String from, String to, boolean isDirected, int weight, int totalCost) {
        this.from = from;
        this.to = to;
        this.isDirected = isDirected;
        this.weight = weight;
        this.totalCost = totalCost;
    }


    public Edge(String from, String to, boolean isDirected,int weight) {
        //this.id = id;
        this.from = from;
        this.to = to;
        this.isDirected = isDirected;
        this.weight=weight;
    }
    public Edge(String from, String to, boolean isDirected,String flow) {
        //this.id = id;
        this.from = from;
        this.to = to;
        this.isDirected = isDirected;
        this.flow=flow;
    }

    @Override
    public int compareTo(Edge edge) {
        return this.weight - edge.weight;
    }

}
