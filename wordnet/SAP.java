package wordnet;

import algs4.Digraph;
import algs4.In;
import algs4.StdIn;
import algs4.StdOut;
import java.util.LinkedList;
import java.util.Stack;


/**
 *
 * @author huseyngasimov
 */
public class SAP {
    private Digraph g;
    private int[] cache = null;
    
    public SAP(Digraph diGraph) { 
        g = new Digraph(diGraph); // copy the digraph beucase SAP class should be immutable
        cache = new int[5]; // cache[0] - chace type: 1 for Integer, 2 for Iterable<Integer>
                            // cache[1] - source 1 (int or hash of Iterable<Object>)
                            // cache[2] - source 2 (int or hash of Iterable<Object>)
                            // cache[3] - length
                            // cache[4] - ancestor                            
    }
    
    
    
    public int length(int v, int w) throws IndexOutOfBoundsException { 
        // if this querry exists in the cache, return the cached result
        int cacheType = 1;
        if (cache[0] == cacheType && 
                ((cache[1] == v && cache[2] == w) || (cache[1] == w && cache[2] == v))) {
            //System.out.println("Cahced");
            return cache[3];
        }
        
        Stack<Integer> st1 = new Stack<Integer>(); st1.push(v);        
        Stack<Integer> st2 = new Stack<Integer>(); st2.push(w);        
                    
        int[] res = sap(st1, st2);
        
        cache[0] = cacheType;
        cache[1] = v;
        cache[2] = w;
        
        if (res == null) {
            cache[3] = -1;
            cache[4] = -1;
            return -1;
        }
        else {
            cache[3] = res[0];
            cache[4] = res[1];
            return res[0];        
        }
    }    
    
    public int ancestor(int v, int w) { 
        // if this querry exists in the cache, return the cached result
        int cacheType = 1;
        if (cache[0] == cacheType && 
                ((cache[1] == v && cache[2] == w) || (cache[1] == w && cache[2] == v))) {
            //System.out.println("Cahced");
            return cache[4];
        }
        
        Stack<Integer> st1 = new Stack<Integer>(); st1.push(v);        
        Stack<Integer> st2 = new Stack<Integer>(); st2.push(w);                
        
        int[] res = sap(st1, st2);
        
        cache[0] = cacheType;
        cache[1] = v;
        cache[2] = w;
        
        if (res == null) {
            cache[3] = -1;
            cache[4] = -1;
            return -1;
        }
        else {
            cache[3] = res[0];
            cache[4] = res[1];
            return res[1];
        }
    }
    
    public int length(Iterable<Integer> iv, Iterable<Integer> iw) { 
        // if this querry exists in the cache, return the cached result
        int cacheType = 2;
        if (cache[0] == cacheType && 
                ((cache[1] == iv.hashCode() && cache[2] == iw.hashCode()) || (cache[1] == iw.hashCode() && cache[2] == iv.hashCode()))) {
            //System.out.println("Cahced");
            return cache[3];
        }
        
        int[] res = sap(iv, iw);
        
        cache[0] = cacheType;
        cache[1] = iv.hashCode();
        cache[2] = iw.hashCode(); 
        
        if (res == null) {
            cache[3] = -1;
            cache[4] = -1;
            return -1;
        }
        else {
            cache[3] = res[0];
            cache[4] = res[1];
            return res[0];        
        }          
    }
    
    public int ancestor(Iterable<Integer> iv, Iterable<Integer> iw) { 
        // if this querry exists in the cache, return the cached result
        int cacheType = 2;
        if (cache[0] == cacheType && 
                ((cache[1] == iv.hashCode() && cache[2] == iw.hashCode()) || (cache[1] == iw.hashCode() && cache[2] == iv.hashCode()))) {
            //System.out.println("Cahced");
            return cache[4];
        }
        
        int[] res = sap(iv, iw);
        
        cache[0] = cacheType;
        cache[1] = iv.hashCode();
        cache[2] = iw.hashCode();
        
        if (res == null) {
            cache[3] = -1;
            cache[4] = -1;
            return -1;
        }
        else {
            cache[3] = res[0];
            cache[4] = res[1];
            return res[1];
        } 
    }
    
    public static void main(String[] args1) {         
        String[] args = {"/Users/huseyngasimov/NetBeansProjects/PrincentonAlgo2/wordnet/digraph1.txt"};
        Digraph g = args.length > 0 ? new Digraph(new In(args[0])) :  sampleGraph2();                
                
        SAP sap = new SAP(g);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int ancestor = 0, length;
            try { ancestor = sap.ancestor(v, w); } catch(Exception ex) { ancestor = -1; }
            try { length = sap.length(v, w); } catch(Exception ex) { length = -1; }
            
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
        
        
        /*
        Stack<Integer> st1 = new Stack<Integer>();
        st1.push(15);
        st1.push(13);
        
        Stack<Integer> st2 = new Stack<Integer>();
        st1.push(2);
        st2.push(12);   
        
        //System.out.println("length = " + sap.length(st1, st2));
        //System.out.println("ancestor = " + sap.ancestor(st1, st2));
        int v = 13;
        int w = 12;
         
        System.out.println("length = " + sap.length(v, w));
        System.out.println("ancestor = " + sap.ancestor(v, w));  
        * 
        */
    }
    
    private static Digraph sampleGraph() {
        Digraph g = new Digraph(17);
        g.addEdge(1, 0);
        g.addEdge(2, 0);
        g.addEdge(3, 1);
        g.addEdge(4, 1);
        g.addEdge(5, 1);
        g.addEdge(7, 3);
        g.addEdge(8, 3);
        g.addEdge(9, 5);
        g.addEdge(10, 5);
        g.addEdge(14, 9);
        g.addEdge(14, 9);
        g.addEdge(11, 10);
        g.addEdge(12, 10);
        g.addEdge(13, 11);
        g.addEdge(13, 9);
        g.addEdge(15, 14);
        return g;
    }
    
    private static Digraph sampleGraph2() {
        Digraph g = new Digraph(5);
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 3);
        g.addEdge(3, 4);
        //g.addEdge(5, 1);
        return g;
    }
    
    private int[] sap(Iterable<Integer> iv, Iterable<Integer> iw) {
        /**
         * j - source id
         * i - traveled vertex
         * travel[j][i][0] - traveled from which vertex
         * travel[j][i][1] - distance to its source
         */
        int[][][] travel = new int[2][g.V()][2];
        
        Object[] queues =  new Object[2];
        
        LinkedList<Integer> queue = new LinkedList<Integer>();        
        for(int i: iv) {
            if (!isValid(i)) throw new IndexOutOfBoundsException();
            travel[0][i][0] = -1;
            travel[0][i][1] = 0;            
            queue.add(i);
        }
        queues[0] = queue;   
        
        queue = new LinkedList<Integer>();        
        for(int i: iw) {        
            if (!isValid(i)) throw new IndexOutOfBoundsException();
            if (!(travel[0][i][0] == 0 && travel[0][i][1] == 0)) {
                int[] res = {0, i};
                return res; // if this item exists in another iterable object
            }
            travel[1][i][0] = -1;
            travel[1][i][1] = 0;
            queue.add(i);        
        }        
        queues[1] = queue;        
                        
        boolean finished = false;
        int minLength = g.E() + 1;
        int curLevel = 0;
        int[] res = null;        
        // continue, even if you find a solution, until it's impossible to find a better one
        while(!finished && curLevel < minLength) {
            finished = true;
            for(int j = 0; j < queues.length; j++) {                
                queue = (LinkedList<Integer>) queues[j];
                if (queue.isEmpty()) continue;
                                
                curLevel = travel[j][queue.peek()][1]; // current level
                if (curLevel > minLength - 2) continue;
                while(!queue.isEmpty() && travel[j][queue.peek()][1] == curLevel) {
                    int next = queue.poll();          
                    Iterable<Integer> newTravList = g.adj(next);                    
                    for(int i: newTravList) { // adjacency list of 'next'
                        if (travel[j][i][0] == 0 && travel[j][i][1] == 0) { // not travelled yet
                            travel[j][i][0] = next; // travelled from
                            travel[j][i][1] = curLevel + 1; // travelled distance
                            queue.add(i); 
                        }
                             
                        int otherJ = (j+1)%2;
                        if (!(travel[otherJ][i][0] == 0 && travel[otherJ][i][1] == 0)) { // if this vertex has already been travelled by a different   
                            if (res == null) {
                                res = new int[2];
                                res[0] = travel[otherJ][i][1] + curLevel + 1;
                                res[1] = i;
                                minLength = res[0];
                                //return res;
                            }
                            else if (travel[otherJ][i][1] + curLevel + 1 < minLength) {
                                res[0] = travel[otherJ][i][1] + curLevel + 1;
                                res[1] = i;
                                minLength = res[0];
                            }
                        }
                    }
                }
                
                if(!queue.isEmpty()) finished = false;                        
            }
        }
        
        return res; 
    }
    
    private boolean isValid(int i) {
        return i > -1 && i < g.V();
    }
    
}
