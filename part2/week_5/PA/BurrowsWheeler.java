import java.util.Arrays;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int R = 256;
    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output 
    public static void transform() {
        String input = BinaryStdIn.readString();
        int N = input.length();
        CircularSuffixArray sa = new CircularSuffixArray(input);
        int first = -1;
        StringBuilder builder = new StringBuilder();
        int offset;
        for (int i = 0; i < N; i++) {
            offset = sa.index(i);
            if (offset == 0)
                first = i;
            builder.append(input.charAt((offset+N-1)%N));
        }
        BinaryStdOut.write(first);
        BinaryStdOut.write(builder.toString());
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        char[] t = BinaryStdIn.readString().toCharArray();
        char[] h = Arrays.copyOf(t, t.length);
        Arrays.sort(h);
        int[] next = new int[t.length];

        int[][] range = new int[R][2]; // char -> (start, end)
        // scan h
        char old = h[0];
        range[old][0] = 0;
        for (int i = 1; i < h.length; i++) {
            if (h[i] != old) {
                range[old][1] = i-1;
                old = h[i];
                range[h[i]][0] = i;
            }
        }
        range[old][1] = h.length-1;

        // scan t
        // range[char][0] is the next index to be filled
        for (int i = 0; i < t.length; i++) {
            next[range[t[i]][0]++] = i;
        }

        int i = first;
        int cnt = 0;
        while (true) {
            BinaryStdOut.write(h[i]);
            cnt++;
            if (cnt < t.length)
                i = next[i];
            else
                break;
        }
        BinaryStdOut.close();
        
        

    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-"))
            transform();
        if (args[0].equals("+"))
            inverseTransform();

    }

}
