import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;

public class BoyerMoore {
    private static int R = 256;
    private String pat;
    private int[] right;
    // right[c]: right most position of c in pat, -1 if not exist
    BoyerMoore(String pat) {
        this.pat = pat;
        right = new int[R];
        for (int i = 0; i < R; i++)
            right[i] = -1;
        int M = pat.length();
        for (int i = 0; i < M; i++)
            right[pat.charAt(i)] = i;
    }
    
    public int search(String txt) {
        int M = pat.length();
        int N = txt.length();
        int skip, i, j;
        for (i = 0; i <= N-M; i += skip) {
            skip = 0;
            for (j = M-1; j >= 0; j--) {
                if (txt.charAt(i+j) != pat.charAt(j)) {
                    skip = j - right[txt.charAt(i+j)];
                    if (skip < 1)
                        skip = 1;
                    break;
                }
            }
            if (j < 0) // match
                return i;
        }
        return N;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String txt = in.readAll();
        while (StdIn.hasNextLine()) {
            String pat = StdIn.readString();
            BoyerMoore bm = new BoyerMoore(pat);
            int position = bm.search(txt);
            System.out.println(pat + " is at postion: " + position);
        }
    }
}
