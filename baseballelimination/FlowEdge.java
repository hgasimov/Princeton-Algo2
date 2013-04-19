/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseballelimination;

/**
 *
 * @author huseyngasimov
 */
public class FlowEdge {
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
    
    @Override
    public String toString() {
        return from() + " -> " + to() + " (" + flow + "/" + capacity() + ")"; 
    }
}
