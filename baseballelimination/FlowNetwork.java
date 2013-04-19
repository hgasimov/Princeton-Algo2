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
    
    public int V() {
        return V;
    }
}