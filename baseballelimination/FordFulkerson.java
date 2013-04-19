/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseballelimination;

import java.util.LinkedList;


/**
 *
 * @author huseyngasimov
 */
public class FordFulkerson {
    private boolean[] marked;
    private FlowEdge[] edgeTo;
    private double value;
    
    public FordFulkerson(FlowNetwork G, int s, int t) {
        value = 0;
        while(hasAugmentedPath(G, s, t)) {
            double min = Double.MAX_VALUE;
            for (int n = t; n != s; n = edgeTo[n].other(n)) {
                min = Math.min(min, edgeTo[n].residualCapacityTo(n));                
            }
            
            for (int n = t; n != s; n = edgeTo[n].other(n)) {
                edgeTo[n].addResidualFlowTo(n, min);
            }            
            
            value += min;                        
        }        
    }
    
    
    private boolean hasAugmentedPath(FlowNetwork G, int s, int t) {
        marked = new boolean[G.V()];
        edgeTo = new FlowEdge[G.V()];
        
        marked[s] = true;
        
        LinkedList<Integer> queue = new LinkedList<Integer>();        
        int n = s; // current node
        queue.add(s);
        
        while(!queue.isEmpty()) {
            n = queue.pop();
            
            for (FlowEdge e: G.adj(n)) {
                int i = e.other(n);
                if (!marked[i] && e.residualCapacityTo(i) > 0) {
                    queue.add(i); // add to the tail
                    marked[i] = true;
                    edgeTo[i] = e;
                    if (i == t) return true;
                }                        
            }
        }        
        
        return false;
    }
    
    public double value() {return value;}
    
    public boolean inMinCut(int i) {return marked[i];}
    
    public static void main(String[] args) {                
        FlowNetwork G = sampleNetwork1();
        FordFulkerson ff = new FordFulkerson(G, 0, 3);
        System.out.println("maxflow = " + ff.value());
        
        for (int i = 0; i < G.V(); i++) 
            if (ff.inMinCut(i))
                System.out.println(i);       
    }

    
    private static FlowNetwork sampleNetwork1() {
        FlowNetwork G = new FlowNetwork(8);
        
        G.addEdge(new FlowEdge(0, 1, 10));
        G.addEdge(new FlowEdge(0, 4, 5));
        G.addEdge(new FlowEdge(0, 6, 15));
        
        G.addEdge(new FlowEdge(1, 2, 9));
        G.addEdge(new FlowEdge(1, 4, 4));
        G.addEdge(new FlowEdge(1, 5, 15));        

        G.addEdge(new FlowEdge(2, 3, 10));
        G.addEdge(new FlowEdge(2, 5, 15));        
        
        G.addEdge(new FlowEdge(4, 6, 4));
        G.addEdge(new FlowEdge(4, 5, 8));
        
        G.addEdge(new FlowEdge(5, 3, 10));
        G.addEdge(new FlowEdge(5, 3, 10));
        G.addEdge(new FlowEdge(5, 7, 15));
        
        G.addEdge(new FlowEdge(6, 7, 16));
        
        G.addEdge(new FlowEdge(7, 4, 6));
        G.addEdge(new FlowEdge(7, 3, 10));
        
        return G;
    }
}



