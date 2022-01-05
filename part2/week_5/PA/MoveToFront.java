import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256; // alphabet size
    private static final int W = 8; // codeword length
    private static class Node {
        char c;
        Node next;
    }


    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        //int[] seq = new int[R]; // char->index
        Node seq = new Node();
        // initialization
        Node x = seq;
        for (char c = 0; c < R-1; c++) {
            x.c = c;
            x.next = new Node();
            x = x.next;
        }
        x.c = R-1;

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            // find index of c
            int index = 0;
            Node prev = null;
            Node v;
            for (v = seq; v.next != null; v = v.next) {
                if (v.c == c)
                    break;
                else {
                    index++;
                    prev = v;
                }
            }
            BinaryStdOut.write(index, W);
            // move front
            if (prev != null) { // not the first node
                prev.next = v.next;
                v.next = seq;
                seq = v;
            }
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] seq = new char[R]; // index->char

        // initialization
        for (int c = 0; c < R; c++) {
            seq[c] = (char)c;
        }

        while (!BinaryStdIn.isEmpty()) {
            int index = BinaryStdIn.readInt(W);
            char c = seq[index];
            BinaryStdOut.write(c);
            // move front
            for (int i = index; i > 0; i--) {
                seq[i] = seq[i-1];
            }
            seq[0] = c;
        }
        BinaryStdOut.close();

    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-"))
            encode();
        if (args[0].equals("+"))
            decode();
    }

}