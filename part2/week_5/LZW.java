import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.TST;

public class LZW {
    private static final int R = 256; // input character
    private static final int L = 4096; // 2^12 number of distinct codewords
    private static final int W = 12; // codeword length

    public static void compress() {
        String input = BinaryStdIn.readString();
        TST<Integer> st = new TST<Integer>(); // character string -> codeword
        int code; // next available codeword value

        // initialization with single character key
        for (int r = 0; r < R; r++) {
            st.put(""+(char)r, r);
        }
        code = R+1;

        // scan input characters
        while (input.length() > 0) {
            String s = st.longestPrefixOf(input); // match
            BinaryStdOut.write(st.get(s), W);
            int h = s.length();
            if (h < input.length() && code < L) {
                st.put(input.substring(0, h+1), code++);
            }
            input = input.substring(h);
        }
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    }

    public static void expand() {
        String[] st = new String[L]; // inverse codeword table
        int code;

        // initialization of codetable
        for (int r = 0; r < R; r++) {
            st[r] = "" + (char)r;
        }
        code = R+1;

        // scan input
        int codeword = BinaryStdIn.readInt(W);
        String val = st[codeword];

        // current st state: just before adding entry val+c 
        while (true) {
            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(W);
            String s = st[codeword];
            if (codeword == R)
                break;
            if (code == L) 
                return; // error
            if (codeword == code) { // s is invalid
                s = val + val.charAt(0);
                st[code++] = s;
            }
            else { // s is valid
                st[code++] = val + s.charAt(0);
            }
            val = s;
        }

        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-"))
            compress();
        if (args[0].equals("+"))
            expand();
    }
}

