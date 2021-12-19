import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;

public class TST<Value> {
    private class Node {
        Value val;
        char c;
        Node left; // < val
        Node right; // > val
        Node mid; // == val
    }
    private Node root;

    // search and return substring d of key in x
    // null if the end node doesn't exist
    // assume: d <= key.length()-1
    private Node get(Node x, String key, int d) {
        if (x == null)
            return null;
        if (d == key.length()) // key may be empty string
            return null;
        assert((x != null) && (d <= key.length()-1));
        char c = key.charAt(d);
        if (c == x.c) {
            if (d == key.length()-1)
                return x;
            else
                return get(x.mid, key, d+1);
        }
        else if (c < x.c)
            return get(x.left, key, d);
        else // (c > x.c)
            return get(x.right, key, d);
        
    }
    public Value get(String key) {
        Node x = get(root, key, 0);
        if (x.val == null)
            return null;
        else
            return x.val;
    }

    // insert substring d of s into the sub-TST at x
    // x can be null
    // assume: d <= s.length-1
    private Node put(Node x, String s, Value val, int d) {
        char c = s.charAt(d);
        if (x == null) {
            x = new Node();
            x.c = c;
        }
        assert(x != null);
        if (c < x.c) {
            x.left = put(x.left, s, val, d);
        }
        else if (c > x.c) {
            x.right = put(x.right, s, val, d);
        }
        else { // c == x.c
            if (d == s.length()-1) { // end characters
                x.val = val;
                return x;
            }
            else
                x.mid = put(x.mid, s, val, d+1);
        }
        return x;
    }

    public void put(String s, Value val) {
        root = put(root, s, val, 0);
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        TST<Integer> table = new TST<Integer>();
        while (in.hasNextLine()) {
            String key = in.readString();
            int val = in.readInt();
            table.put(key, val);
        }
        while (StdIn.hasNextLine()) {
            String key = StdIn.readString();
            System.out.println(table.get(key));
        }
    }
}
