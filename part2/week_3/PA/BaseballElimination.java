import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SequentialSearchST;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
    private final int N; // number of teams
    private final SequentialSearchST<String, Integer> teams_num;
    private final SequentialSearchST<Integer, String> teams_name;
    private final SequentialSearchST<Integer, Bag<String>> cache; // team number to elimination set
    private final int[] W; // win array
    private final int[] L; // lose array
    private final int[] R; // remaining array
    private final int[][] g; // remaing games

    private FlowNetwork G; // elimination network, updated for each isEliminated 
    // private Bag<Integer> S; // elimination set, updated for each isEliminated
    
    // sum of cap of source to each division vertex, check if all edges from source is full
    private int cut_cap; // updated for each isEliminated, compared to maxflow value

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        N = in.readInt();
        W = new int[N];
        L = new int[N];
        R = new int[N];
        g = new int[N][N];
        teams_num = new SequentialSearchST<String, Integer>();
        teams_name = new SequentialSearchST<Integer, String>();
        cache = new SequentialSearchST<Integer, Bag<String>>();
        G = null;
        // S = null;
        String name;
        for (int row = 0; row < N; row++) {
            // read info of each team
            name = in.readString();
            teams_num.put(name, row);
            teams_name.put(row, name);
            W[row] = in.readInt();
            L[row] = in.readInt();
            R[row] = in.readInt();
            for (int col = 0; col < N; col++) {
                g[row][col] = in.readInt();
            }
        }
    }
    
    // number of teams
    public int numberOfTeams() {
        return N;
    }
    
    // all teams
    public Iterable<String> teams() {
        return teams_num.keys();
    }
    
    // number of wins for given team
    public int wins(String team) {
        if (!teams_num.contains(team))
            throw new IllegalArgumentException("invalid team name");
        return W[teams_num.get(team)];
    }
    
    // number of losses for given team
    public int losses(String team) {
        if (!teams_num.contains(team))
            throw new IllegalArgumentException("invalid team name");
        return L[teams_num.get(team)];
    }
    
    // number of remaining games for given team
    public int remaining(String team) {
        if (!teams_num.contains(team))
            throw new IllegalArgumentException("invalid team name");
        return R[teams_num.get(team)];
    }
    
    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if ((!teams_num.contains(team1)) || (!teams_num.contains(team2)))
            throw new IllegalArgumentException("invalid team name");
        return g[teams_num.get(team1)][teams_num.get(team2)];
    }
    
    
    // is given team eliminated?
    // update FlowNetwork G, update S, update cache 
    public boolean isEliminated(String team) {
        // S = null; // reset before each inquire
        if (!teams_num.contains(team))
            throw new IllegalArgumentException("invalid team name");
        int teamnum = teams_num.get(team);
        // inquire the cache
        if (cache.contains(teamnum))
            return (cache.get(teamnum) != null);
        // check if trivially eliminated
        for (int i = 0; i < N; i++) {
            if (W[teamnum] + R[teamnum] < W[i]) {
                Bag<String> s = new Bag<String>();
                s.add(teams_name.get(i));
                cache.put(teamnum, s);
                return true;
            }
        }
        // create flow network
        cut_cap = 0; //reset
        G = build(teamnum);
        FordFulkerson flow = new FordFulkerson(G, 0, G.V()-1);
        if (flow.value() < cut_cap) {
            cache.put(teamnum, extract(flow, teamnum)); // update cache
            return true;
        }
        else {
            cache.put(teamnum, null);
            return false;
        }
    }

    // reset S
    // update cache
    // extract into S if one team is found nontrivially mathematically eliminated
    private Bag<String> extract(FordFulkerson flow, int team) {
        Bag<String> s = new Bag<String>();
        int v;
        for (int i = 0; i < N; i++) {
            if (i < team)
                v = (N * (N-1))/2 - N + 2 + i;
            else if (i > team)
                v = (N * (N-1))/2 - N + 1 + i;
            else 
                continue;
            if (flow.inCut(v))
                s.add(teams_name.get(i));
        }
        return s;

    }
    // build the elimination network of team of number x
    private FlowNetwork build(int x) {
        int sum_n = 2 + ((N) * (N-1)) / 2;
        FlowNetwork G = new FlowNetwork(sum_n);
        // build edges
        int cnt = 1, from = -1, to = -1;
        // traverse division vertex
        for (int i = 0; i < N; i++)
            for (int j = i+1; j < N; j++) {
                if (i != x && j != x) {
                    
                    from = 0;
                    to = cnt;
                    cut_cap += g[i][j];
                    G.addEdge(new FlowEdge(from, to, g[i][j]));

                    from = cnt;
                    if (i < x)
                        to = (N * (N-1))/2 - N + 2 + i;
                    else // i > x
                        to = (N * (N-1))/2 - N + 1 + i;
                    G.addEdge(new FlowEdge(from, to, Double.POSITIVE_INFINITY));
                    
                    
                    if (j < x)
                        to = (N * (N-1))/2 - N + 2 + j;
                    else // j > x
                        to = (N * (N-1))/2 - N + 1 + j;
                    G.addEdge(new FlowEdge(from, to, Double.POSITIVE_INFINITY));

                    cnt++;
                }
            }
        int cap;
        for (int i = 0; i < N; i++) {
            if (i < x)
                from = (N * (N-1))/2 - N + 2 + i;
            else if (i > x)
                from = (N * (N-1))/2 - N + 1 + i;
            else
                continue;
            to = sum_n - 1;
            cap = W[x] + R[x] - W[i];
            G.addEdge(new FlowEdge(from, to, cap));
        }
        return G;
    }
    
    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!teams_num.contains(team))
            throw new IllegalArgumentException("invalid team name");
        if (cache.contains(teams_num.get(team))) {
            return cache.get(teams_num.get(team));
        }
        else {
            isEliminated(team);
            return cache.get(teams_num.get(team));
        }
    }
    
    

    public static void main(String[] args) {
        /*
        BaseballElimination baseball = new BaseballElimination(args[0]);
        for (String team : baseball.teams()) {
            System.out.println(team + ": Wins: " + baseball.wins(team) +
            " Losses: " + baseball.losses(team) + " Remains: " + baseball.remaining(team));
        }
        for (String team1 : baseball.teams()) {
            for (String team2 : baseball.teams()) {
                System.out.print(team1 + " VS " + team2 + " : " + baseball.against(team1, team2) + ". ");
            }
            System.out.println();
        }
        */
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}