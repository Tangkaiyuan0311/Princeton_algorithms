import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;

public class KMP {
    private String pat;
    // deterministic finite-state automaton
    // j' = dfa[c][j]: current text (~i-1) is in state j
    // comparing text[i]=c to pat[j], change to state j'
    private int[][] dfa;
    private static int R = 256;


    public KMP(String pat) {
        this.pat = pat;
        int M = pat.length();
        // construct dfa of pat
        dfa = new int[R][M];
        dfa[pat.charAt(0)][0] = 1; // dfa for state 0 is all but one 0
        // construct transition for state j
        for(int j = 1, x = 0; j < M; j++) {
            // x is the restart state on a mismatch at j
            for (int c = 0; c < R; c++)
                dfa[c][j] = dfa[c][x];
            // update match transition
            dfa[pat.charAt(j)][j] = j+1;
            // update x for j+1
            x = dfa[pat.charAt(j)][x];
        }
    }

    public int search(String txt) {
        int N = txt.length();
        int M = pat.length();
        int i = 0, j = 0;
        for (; i < N && j < M; i++)
            j = dfa[txt.charAt(i)][j];
        if (j == M) // final state, match
            return i-M;
        else
            return N;

    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String txt = in.readAll();
        while (StdIn.hasNextLine()) {
            String pat = StdIn.readString();
            KMP kmp = new KMP(pat);
            int position = kmp.search(txt);
            System.out.println(pat + " is at postion: " + position);
        }
    }
}