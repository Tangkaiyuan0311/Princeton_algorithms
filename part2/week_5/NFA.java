import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedDFS;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class NFA {
    private char[] re; // match transition and regular expression
    private Digraph G; // epsilon transitions
    private int M; // number of states

    public NFA(String regexp) {
        // create the NFA

        // index of | and (
        Stack<Integer> ops = new Stack<Integer>();
        // pattern character for quick indexing
        re = regexp.toCharArray();
        M = re.length;
        G = new Digraph(M+1); // include accept state

        for (int i = 0; i < M; i++) {
            // examine re[i]
            int lp = i;
            if (re[i] == '(' || re[i] == '|')
                ops.push(i); // save index
            else if (re[i] == ')') {
                int or = ops.pop();
                if (re[or] == '|') {
                    // ( | )
                    lp = ops.pop();
                    G.addEdge(lp, or+1);
                    G.addEdge(or, i);
                }
                else // ( )
                    lp = or;
            }
            if (i < M-1 && re[i+1] == '*') { // closure
                G.addEdge(lp, i+1);
                G.addEdge(i+1, lp);
            }
            if (re[i] == '(' || re[i] == ')' || re[i] == '*')
                G.addEdge(i, i+1);
        }

    }

    public boolean recognize(String txt) {
        int N = txt.length();
        SET<Integer> pc = new SET<Integer>(); // possible states
        SET<Integer> match; // match transition after examing current input
        DirectedDFS dfs = new DirectedDFS(G, 0);

        // initialization
        for (int s = 0; s < G.V(); s++)
            if (dfs.marked(s))
                pc.add(s);
        // process
        for (int i = 0; i < N; i++) {
            match = new SET<Integer>();
            for (int s : pc) {
                if (s < M)
                    if (re[s] == '.' || txt.charAt(i) == re[s])
                        // match
                        match.add(s+1);    
            }
            pc = new SET<Integer>();
            if (match.isEmpty())
                return false; // stuck
            dfs = new DirectedDFS(G, match);
            for (int s = 0; s < G.V(); s++)
                if (dfs.marked(s))
                    pc.add(s);
        }

        // check
        if (pc.contains(M))
            return true;
        return false;
    }

    public static void main(String[] args) {
        NFA nfa = new NFA("((A*B|AC)D)");
        while (StdIn.hasNextLine()) {
            String txt = StdIn.readString();
            if (nfa.recognize(txt))
                StdOut.println("Recognize!");
            else
                StdOut.println("NOT Recognize!");
        }

    }

}