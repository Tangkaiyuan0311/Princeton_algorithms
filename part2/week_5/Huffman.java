import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.MinPQ;

public class Huffman {
    private static int R = 256; // extented ascii
    private static class Node implements Comparable<Node>{
        private char ch; // '\0' for internal node
        private int freq; // sum of frequemcy count of leaves
        private final Node left, right;

        Node(char ch, int freq, Node left, Node right) {
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }
        public boolean isLeaf() {
            return left == null && right == null;
        }
        public int compareTo(Node that) {
            return freq-that.freq;
        }
    }

    private static Node buildTrie(int[] freq) {
        MinPQ<Node> pq = new MinPQ<Node>();

        // initialize with singleton trees
        for (char c = 0; c < R; c++) {
            if (freq[c] > 0)
                pq.insert(new Node(c, freq[c], null, null));
        }
        while(pq.size() >= 2) {
            Node x = pq.delMin();
            Node y = pq.delMin();
            pq.insert(new Node('\0', x.freq+y.freq, x, y));
        }
        // return the one single trie
        return pq.delMin();
    }

    // isert all character under x into st
    // s: root~x, character sring 0100101...
    private static void buildCode(String[] st, Node x, String s) {
        if (x.isLeaf()) {
            st[x.ch] = s;
            return;
        }
        else {
            // no 1 degree node
            buildCode(st, x.left, s+'0');
            buildCode(st, x.right, s+'1');
        }
    }

    private static void writeTrie(Node x) {
        // write trie x into standard output
        // preorder tranversal
        if (x.isLeaf()) {
            BinaryStdOut.write(true);
            BinaryStdOut.write(x.ch);
        }
        else {
            BinaryStdOut.write(false);
            writeTrie(x.left);
            writeTrie(x.right);
        }
    }
    public static void compress() {
        // Read input bits stream, interpret as a String
        String s = BinaryStdIn.readString();
        char[] input = s.toCharArray();

        // tabulate frequency counts
        int[] freq = new int[R];
        for (int i = 0; i < input.length; i++)
            freq[input[i]]++;
        
        // create huffman trie using freq
        Node root = buildTrie(freq);

        // build codetable
        String[] st = new String[R];
        buildCode(st, root, "");

        // write trie
        writeTrie(root);

        // write number of bytes in the orginal input
        BinaryStdOut.write(input.length);

        // encode input
        for (int i = 0; i < input.length; i++) {
            String code = st[input[i]];
            for (int j = 0; j < code.length(); j++) {
                if (code.charAt(j) == '1')
                    BinaryStdOut.write(true);
                else
                    BinaryStdOut.write(false);
            }

        }
        BinaryStdOut.close();
    }

    private static Node readTrie() {
        if (BinaryStdIn.readBoolean()) {
            char c = BinaryStdIn.readChar();
            return new Node(c, 0, null, null);
        }
        else {
            return new Node('\0', 0, readTrie(), readTrie());
        }
    }
    
    // follow the standard input, guided by root, return thw character
    private static char read(Node root) {
        Node x = root;
        while (!x.isLeaf()) {
            if (!BinaryStdIn.readBoolean())
                x = x.left;
            else    
                x = x.right;
        }
        return x.ch;
    }
    public static void expand() {
        Node root = readTrie();
        int N = BinaryStdIn.readInt();
        for (int i = 0; i < N; i++) {
            BinaryStdOut.write(read(root));
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
