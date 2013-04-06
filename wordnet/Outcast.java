/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wordnet;

import algs4.In;
import algs4.StdOut;

/**
 *
 * @author huseyngasimov
 */
public class Outcast {
    private WordNet wn;
    private static String datadir = "/Users/huseyngasimov/NetBeansProjects/PrincentonAlgo2/wordnet/";
    
    public Outcast(WordNet wordnet) {
        wn = wordnet;
    }
    
    public String outcast(String[] nouns) {
        //for (String s: nouns) System.out.print(s);
        
        int N = nouns.length;
        int[][] dist = new int[N][N];
        
        // calculate all distance combinations and store them
        for(int i = 0; i < N-1; i++) {
            dist[i][i] = 0;
            for(int j = i+1; j < N; j++) {
                dist[i][j] = wn.distance(nouns[i], nouns[j]);
                dist[j][i] = dist[i][j];
            }            
        }
        
        // calculate sum of colums for each row and report the row with max sum
        int maxSum = -1;
        int maxID = -1;        
        for (int i = 0; i < N; i++) {
            int sum = 0;
            for (int j = 0; j < N; j++) {
                sum += dist[i][j];
            }
            
            if (sum > maxSum) {
                maxSum = sum;
                maxID = i;
            }            
        }
        
        return nouns[maxID];
    }
    
    public static void main(String[] args2) {
        String fSynsets = datadir + "synsets.txt";
        String fHypernyms = datadir + "hypernyms.txt";
        String fOutcast1 = datadir + "outcast5.txt";
        String fOutcast2 = datadir + "outcast8.txt";
        String fOutcast3 = datadir + "outcast11.txt";        
        
        String[] args = {fSynsets, fHypernyms, fOutcast1, fOutcast2, fOutcast3};
        WordNet wordnet = new WordNet(args[0], args[1]);                        
        //System.out.println(wordnet.distance("horse", "table"));
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            String[] nouns = In.readStrings(args[t]);
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }     
}
