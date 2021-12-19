import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;

// symbol table: String -> Value
public class TrieST<Value> {
    private static int R = 256; // radix, extended ascii
    private static class Node {
        Object value; // null or val of key
        Node[] next = new Node[R]; // reference for each char value
        // can not create array of generic object, so node has to be static
    }
    private Node root; // null initialized
    
    // insert substring d of s into one of subtree of x, return updated x
    // x can be null
    private Node put(Node x, String s, Value val, int d) {
        if (x == null)
            x = new Node();
        assert(x != null);
        if (d == s.length()) { // x is the last char
            x.value = val;
            return x;
        }
        else {
            int c = s.charAt(d);
            x.next[c] = put(x.next[c], s, val, d+1);
            return x;
        }

    }
    public void put(String key, Value val) {
        root = put(root, key, val, 0);
    }

    // search the substring d of s in the subtrees of x, return the Node of last char or null
    // x can be null
    private Node get(Node x, String s, int d) {
        if (x == null) // no subtrees
            return null;
        assert(x != null);
        if (d == s.length()) // no more characters to be examined, search end at x
            return x;
        else {
            int c = s.charAt(d);
            return get(x.next[c], s, d+1);
        }
    }

    public Value get(String key) {
        Node x = get(root, key, 0);
        if (x == null)
            return null; // search failes
        if (x.value == null)
            return null; // no key
        else
            return (Value)x.value;
    }

    // delete substring d of key from one of subtree of x
    // return updated x
    // x can be null
    private Node delete(Node x, String key, int d) {
        if (x == null)
            return null;
        assert(x != null);
        if (d == key.length()) // x is the last character of key
            x.value = null; // delete its value
        else { // delete recursively
            int c = key.charAt(d);
            x.next[c] = delete(x.next[c], key, d+1);
        }
        // check if we can release x
        if (x.value == null) {
            for (int c = 0; c < R-1; c++) {
                if (x.next[c] != null)
                    return x;
            }
            return null; // x is released
        }
        else // another key end at x
            return x;

        
    }
    public void delete(String key) {
        root = delete(root, key, 0);
    }

    // insert all keys in subtrie x into key
    // pre: root~x
    // x can be null
    private void collect(Node x, String pre, Queue<String> q) {
        if (x == null)
            return;
        assert(x != null);
        if (x.value != null)
            q.enqueue(pre);
        for (char c = 0; c < R; c++)
            collect(x.next[c], pre+c, q);
    }

    // search for prefix of s under x, return the max length
    // assume: length is the max length so far
    // assume: d is the char index to be examined
    // assume: d <= s.length
    // stop necessarily
    private int search(Node x, String s, int d, int length) {
        if (x == null)
            return length;
        if (x.value != null) // found
            length = d;
        if (d == s.length()) // all chars of s has been examined
            return length;
        else {
            char c = s.charAt(d);
            return search(x.next[c], s, d+1, length);
        }
        
    }
    // longest key that is a prefix of s
    public String longestPrefixOf(String s) {
        int length = search(root, s, 0, 0);
        return s.substring(0, length);
    }
    
    // keys with s as prefix
    public Iterable<String> keysWithPrefix(String s) {
        // search the s in the trie
        Node x = get(root, s, 0);
        // x can be null
        Queue<String> q = new Queue<String>();
        collect(x, s, q);
        return q; // q can be empty
    }

    // collect all keys under subtrie x that matches pat into q
    // . in pat match any character
    // x can be null
    // stop when pre is longer than pat
    // assume: pre: root~x, match pat
    private void collect(Node x, String pre, String pat, Queue<String> q) {
        if (x == null)
            return;
        assert(x != null);
        if (x.value != null) // key at x
            q.enqueue(pre);
        if (pre.length() == pat.length()) // stop searching
            return;
        int d = pre.length();
        for (char c = 0; c < R; c++) {
            if (pat.charAt(d) == '.' || c == pat.charAt(d))
            collect(x.next[c], pre+c, pat, q);
        }
        
    }
    // . matches any character
    public Iterable<String> keysThatMatch(String s) {
        Queue<String> q = new Queue<String>();
        collect(root, "", s, q);
        return q;
    }
    // return the number of keys in x
    // x can be null
    private int size(Node x) {
        if (x == null)
            return 0;
        int sum = 0;
        for (int c = 0; c < R; c++)
            sum += size(x.next[c]);
        if (x.value != null)
            sum += 1;
        return sum;
    }
    // number of keys
    // a lazy approach
    public int size() {
        return size(root);
    }
    public Iterable<String> keys() {
        Queue<String> q = new Queue<String>();
        collect(root, "", q);
        return q;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        TrieST<Integer> table = new TrieST<Integer>();
        while (in.hasNextLine()) {
            String key = in.readString();
            int val = in.readInt();
            table.put(key, val);
        }
        System.out.println(table.size() + " keys:");
        for (String key : table.keys()) {
            System.out.println(key + ": " + table.get(key));
        }
        table.delete("shore");
        System.out.println("Delete shore");
        System.out.println(table.size() + " keys:");
        for (String key : table.keys()) {
            System.out.println(key + ": " + table.get(key));
        }
        System.out.println("Input the prefix:");
        while (StdIn.hasNextLine()) {
            String pre = StdIn.readString();
            if (pre.equals("break"))
                break;
            for (String key : table.keysWithPrefix(pre))
                System.out.println(key);
        }
        System.out.println("Input the complete string:");
        while (StdIn.hasNextLine()) {
            String s = StdIn.readString();
            if (s.equals("break"))
                break;
            System.out.println(table.longestPrefixOf(s));
        }
        System.out.println("Input the pattern:");
        while (StdIn.hasNextLine()) {
            String pat = StdIn.readString();
            if (pat.equals("break"))
                break;
            for (String key : table.keysThatMatch(pat))
                System.out.println(key);
        }
        /*
        while (StdIn.hasNextLine()) {
            String key = StdIn.readString();
            System.out.println(table.get(key));
        }
        */

        /*
        for (String key : table.keys()) {
            System.out.println(key + ": " + table.get(key));
        }
        */
    }
}