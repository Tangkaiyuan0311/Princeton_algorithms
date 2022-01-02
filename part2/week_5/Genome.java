import edu.princeton.cs.algs4.Alphabet;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.StdOut;

public class Genome {
    
    public static void compress() {
        Alphabet DNA = new Alphabet("ACTG");
        // read in the bits stream and convert it into a string of ascii character
        String txt = BinaryStdIn.readString();
        int N = txt.length();
        BinaryStdOut.write(N); // assist decoding in case last bit is not byte aligned
        for (int i = 0; i < N; i++) {
            int d = DNA.toIndex(txt.charAt(i));
            BinaryStdOut.write(d, DNA.lgR()); // last to bits in this case
        }
        BinaryStdOut.close();
    }

    public static void expand() {
        Alphabet DNA = new Alphabet("ACTG");
        // read first 32 bits and intepret as an int
        int N = BinaryStdIn.readInt();
        for (int i = 0; i < N; i++) {
            char c = BinaryStdIn.readChar(DNA.lgR()); // 0~3
            BinaryStdOut.write(DNA.toChar(c));
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
