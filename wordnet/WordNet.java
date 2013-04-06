/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wordnet;

import algs4.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 *
 * @author huseyngasimov
 */
public class WordNet {
    private static String datadir = "/Users/huseyngasimov/NetBeansProjects/PrincentonAlgo2/wordnet/";
    
    private ArrayList<String> synsets; // list of string arrays
    private HashMap<String, Stack<Integer>> nounHash;
    private Digraph g;
    private SAP sap;
    private int initCap = 82192; // initial capacity for synsets, nounHash. Change to 82,192
    
    public WordNet(String fSynsets, String fHypernyms) {
        synsets = new ArrayList<String>(initCap);
        nounHash = new HashMap<String, Stack<Integer>>(initCap);
                
        boolean res = parseSynsets(fSynsets);        
        g = new Digraph(synsets.size());
        res = parseHypernyms(fHypernyms);
        if(!isDAG(g)) throw new IllegalArgumentException();
        sap = new SAP(g);
    }
    
    public Iterable<String> nouns() {
        return nounHash.keySet();
    }
    
    public boolean isNoun(String word) {
        return nounHash.containsKey(word);
    }
    
    public int distance(String nounA, String nounB) {
        Stack<Integer> stA = nounHash.get(nounA);        
        if (stA == null) throw new IllegalArgumentException();
        
        Stack<Integer> stB = nounHash.get(nounB);        
        if (stB == null) throw new IllegalArgumentException();
        
        return sap.length(stA, stB);
    }
    
    public String sap(String nounA, String nounB) {
        Stack<Integer> stA = nounHash.get(nounA);        
        if (stA == null) throw new IllegalArgumentException();
        
        Stack<Integer> stB = nounHash.get(nounB);        
        if (stB == null) throw new IllegalArgumentException();
        
        int ancestorID = sap.ancestor(stA, stB);
        return synsets.get(ancestorID);
    }
    
    private boolean parseSynsets(String fSynsets) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fSynsets));
            String line;
            while((line = br.readLine()) != null) {
                String[] sArr = line.split(",");
                int id = Integer.parseInt(sArr[0]);
                synsets.add(sArr[1]);
                
                String[] nouns = sArr[1].split(" ");                                
                for(String s: nouns) {
                    Stack<Integer> synList = nounHash.get(s);
                    if (synList == null) {
                        synList = new Stack<Integer>();
                        synList.add(id);
                        nounHash.put(s, synList);
                    }
                    else synList.add(id);
                }
            }             
            
            br.close();
            return true;
        }
        catch(Exception ex) {
            System.err.println(ex);
            return false;
        }        
    }
    
    private boolean parseHypernyms(String fHypernyms) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fHypernyms));
            String line;
            while((line = br.readLine()) != null) {
                String[] sArr = line.split(",");
                int id = Integer.parseInt(sArr[0]);
                
                for(int i = 1; i < sArr.length; i++) {
                    g.addEdge(id, Integer.parseInt(sArr[i]));
                }
                    
            }             
            
            br.close();
            return true;
        }
        catch(Exception ex) {
            System.err.print("parseHypernyms: ");
            System.err.println(ex);
            return false;
        }
    }
        
    private boolean isDAG(Digraph g) {
        int root = -1;
        for (int i = 0; i < g.V(); i++) {
            if (root == -1 && !g.adj(i).iterator().hasNext()) root = i; // if the vertex does not have outgoing edges
            else return false; // the graph is mutli-rooted
        }
        
        if (root == -1) return false; // the graph has cycles
        
        DirectedCycle finder = new DirectedCycle(g);
        return !finder.hasCycle();
    }

    private static Object[] dfs(Digraph G, int v) { 
        int[] cameFrom = new int[G.V()];
        for (int i = 0; i < G.V(); i++) cameFrom[i] = -1;
        cameFrom[v] = -2; // just to indicate v as a root
        
        int curVertex = v;
        int n = 1; // number of travelled vertices
        boolean forward;
        Stack<Integer> postReverse = new Stack<Integer>();
        do {
            forward = false;
            for (int w : G.adj(curVertex)) {
                if (cameFrom[w] == -1) { // if not travelled yet
                    cameFrom[w] = curVertex;
                    curVertex = w;         
                    forward = true;
                    break;
                }
            }
            
            
            if(!forward) {
                postReverse.push(curVertex);
                curVertex = cameFrom[curVertex];                
            }                       
        } while(curVertex > -2);
        
        
        Object[] res = new Object[2];
        res[0] = n;
        res[1] = postReverse;
        return res;
    }
   
    public static void main(String[] args2) {
        Digraph dg = new Digraph(new In(datadir + "digraph2.txt"));
        //System.out.println(isDAG(dg));
        
        Object[] dfsRes = dfs(dg.reverse(), 5);
        System.out.println("size = " + dfsRes[0]);
        System.out.println("Reverse post order: ");
        Stack<Integer> st = (Stack<Integer>) dfsRes[1];
        //for (int i: st) System.out.println(i);
        
        while(!st.empty()) System.out.println(st.pop());
        /* String fSynsets = datadir + "synsets.txt";
        String fHypernyms = datadir + "hypernyms.txt";
        String fOutcast = datadir + "outcast5.txt";
        
        String[] args = {fSynsets, fHypernyms, fOutcast};
        WordNet wordnet = new WordNet(args[0], args[1]);                        
        System.out.println(wordnet.distance("horse", "table")); */
    }
    
}
