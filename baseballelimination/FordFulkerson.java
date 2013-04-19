/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseballelimination;

import java.util.Stack;


/**
 *
 * @author huseyngasimov
 */
public class FordFulkerson {
    
}

class FlowEdge {
    private int v, w; // from and to respectively
    private double capacity;
    private int flow;
    
    public FlowEdge(int v, int w, double capacity) {
        this.v = v;
        this.w = w;
        this.capacity = capacity;
    }
    
    public int from() {return v;}
    public int to() {return w;}
    public int other(int i) {return (i == v ? w:v);}
    public double capacity() {return capacity;}    
    public double flow() {return flow;}
    
    public double residualCapacityTo(int i) {        
        if (i == w) return capacity - flow;
        else if (i == v) return flow;
        else throw new IllegalArgumentException();
    }
    
    public void addResidualFlowTo(int i, double delta) {
        if (i == v) flow -= delta;
        else if (i == w) flow += delta;
        else throw new IllegalArgumentException();
    }
}

class FlowNetwork {
    private int V; // number of vertices
    private Stack<FlowEdge>[] adj; // adjacency list
    
    public FlowNetwork(int V) {
        this.V = V;
        adj = (Stack<FlowEdge>[]) new Stack[V];
        for (int i = 0; i < V; i++)
            adj[i] = new Stack<FlowEdge>();
    }
    
    public void addEdge(FlowEdge e) {
        int v = e.from();
        int w = e.to();
        adj[v].add(e);
        adj[w].add(e);
    }
    
    public Iterable<FlowEdge> adj(int i) {
        return adj[i];
    }        
}