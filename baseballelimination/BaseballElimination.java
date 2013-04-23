/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseballelimination;

import algs4.FlowEdge;
import algs4.FlowNetwork;
import algs4.FordFulkerson;
import algs4.StdOut;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Stack;

/**
 *
 * @author huseyngasimov
 */
public class BaseballElimination {
    private Team[] teams;
    private HashMap<String, Integer> teamID; // team name -> team ID
    private int N; // number of teams
    private Stack<String>[] eliminated; 
    private int remaining; // remaining games in total
    
    public BaseballElimination(String filename) {
        if (!init(filename)) return;                
    }
    
    private boolean init(String filename) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            N = Integer.parseInt(br.readLine());
            teams = new Team[N];
            teamID = new HashMap<String, Integer>(N);
            eliminated = new Stack[N];
            remaining = 0;
            
            String s;
            int id = 0; // team id
            while((s = br.readLine()) != null) {
                String[] ss = parseLine(s);
                    
                Team t = new Team();
                t.id = id;
                t.name = ss[0];
                t.wins = Integer.parseInt(ss[1]);
                t.losses = Integer.parseInt(ss[2]);
                t.remaining = Integer.parseInt(ss[3]);
                remaining += t.remaining;

                int[] games = new int[ss.length - 4];
                for (int j = 4; j < ss.length; j++) {
                    games[j-4] = Integer.parseInt(ss[j]);
                }
                t.games = games;
                
                teams[id] = t;
                teamID.put(ss[0], id);
                id++;
            }
            
            br.close();  
            return true;
        }
        catch(Exception ex) {
            System.err.println("[init]: " + ex);
            return false;
        }
    }
    
    private String[] parseLine(String line) {
        StringBuilder sb = new StringBuilder();
        
        if (line.charAt(0) != ' ') sb.append(line.charAt(0));
        for (int i = 1; i < line.length(); i++)
            if (line.charAt(i) != ' ' || line.charAt(i-1) != ' ')
                sb.append(line.charAt(i));
        
        return sb.toString().split(" ");        
    }
    
    public int numberOfTeams() {return N;}
    
    public Iterable<String> teams() {
        Stack<String>  st = new Stack<String>();
        for (int i = 0; i < N; i++) {
            st.add(teams[i].name);
        }
        return st;
    }
    
    public int wins(String team) {
        Integer id = teamID.get(team);
        if (id == null) throw new IllegalArgumentException();
        return teams[id].wins;
    }
    
    public int losses(String team) {
        Integer id = teamID.get(team);
        if (id == null) throw new IllegalArgumentException();
        return teams[id].losses;
    }
    
    public int remaining(String team) {
        Integer id = teamID.get(team);
        if (id == null) throw new IllegalArgumentException();
        return teams[id].remaining;
    }
 
    public int against(String team1, String team2) {
        Integer id1 = teamID.get(team1);
        Integer id2 = teamID.get(team2);
        if (id1 == null || id2 == null) throw new IllegalArgumentException();
        return teams[id1].games[id2];
    }
    
    public boolean isEliminated(String team) {
        Integer id = teamID.get(team);
        if (id == null) throw new IllegalArgumentException();
        if (eliminated[id] == null) { // not calculated yet            
            eliminated[id] = new Stack<String>();
            int ng = (N-1)*(N-2)/2; // number of game nodes
            FlowNetwork net = new FlowNetwork(N + ng + 2); // 1 team node is additional            
            
            // --- Edges from the team vertices to the sink vertex ---
            for (int i = 0; i < N; i++) {
                if (i == id) continue;
                int limit = teams[id].wins + teams[id].remaining - teams[i].wins;                
                if (limit < 0) { // because of the trivial elimination: w[x] + r[x] < w[i]                    
                    eliminated[id].add(teams[i].name);                    
                    return true;
                }
                net.addEdge(new FlowEdge(ng + i + 1, ng + N + 1, limit)); // from team node to the sink node
            }
                     
            // --- Edges from the source vertex to the game vertices and from the game vertices to the team vertices ---
            int n = 1; // current game node
            for (int i = 0; i < N - 1; i++) {
                if (i == id) continue;
                int ni =  ng + i + 1; // node of the team i
                for (int j = i + 1; j < N; j++) {
                    if (j == id) continue;
                    int nj = ng + j + 1; // node of the team j
                    net.addEdge(new FlowEdge(0, n, teams[i].games[j])); // from source to the game node
                    net.addEdge(new FlowEdge(n, ni, Double.POSITIVE_INFINITY)); // from the game node to the team i
                    net.addEdge(new FlowEdge(n, nj, Double.POSITIVE_INFINITY)); // from the game node to the team j
                    n++;
                }
            }
                        
            FordFulkerson ff = new FordFulkerson(net, 0, N + ng + 1); // calculate maxFlow
            for (int i = 0; i < N; i++)
                    if (ff.inCut(ng + i + 1)) // if the team node i is in the min-cut
                        eliminated[id].add(teams[i].name);
            
            return (eliminated[id].size() != 0);
        }
        else if (eliminated[id].empty()) return false;
        else return true;        
    }
    
    public Iterable<String> certificateOfElimination(String team) {
        Integer id = teamID.get(team);
        if (id == null) throw new IllegalArgumentException();
        if (eliminated[id] == null) isEliminated(team); // calculate, if not calculated yet
        if (eliminated[id].empty()) return null; // if not eliminated, return null
        return eliminated[id];
    }
    
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team))
                    StdOut.print(t + " ");
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}

class Team {
    int id;
    String name;
    int wins, losses, remaining;
    int[] games;
}
