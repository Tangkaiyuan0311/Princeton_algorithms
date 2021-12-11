import edu.princeton.cs.algs4.In;

public class Quick3string {
    private static int charAt(String s, int d) {
        if (d < s.length())
            return s.charAt(d);
        else
            return -1;
    }

    private static void exch(String[] s, int i, int j) {
        String tmp = s[i];
        s[i] = s[j];
        s[j] = tmp;
    }
    // 3-way quick sort a[lo]~a[hi] W.R.T chars starting at position d
    private static void sort(String[] a, int lo, int hi, int d) {
        if (hi <= lo)
            return;
        // set up partition element
        int v = charAt(a[lo], d);
        
        // invariant:
        // < v : a[lo]~a[lt-1]
        // = v : a[lt]~a[i-1]
        // to be examined: a[i]~a[gt]
        // > v : a[gt+1]~a[hi]
        int lt = lo;
        int gt = hi;
        int i = lo+1;
        // here we start
        while (i <= gt) { // not fully examines
            if (charAt(a[i], d) < v)
                exch(a, lt++, i++);
            else if (charAt(a[i], d) > v)
                exch(a, gt--, i++);    
            else 
                i++;
        }
        // lo~lt-1, lt~gt, gt+1~hi
        sort(a, lo, lt-1, d);
        if (v >= 0)
            sort(a, lt, gt, d+1);
        sort(a, gt+1, hi, d);

    }
    public static void sort(String[] a) {
        // possible shuffle;
        sort(a, 0, a.length-1, 0);
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt(); // number of String
        String[] strings = new String[N];
        for (int i = 0; i < N; i++)
            strings[i] = in.readString();
        Quick3string.sort(strings);
        for (int i = 0; i < N; i++)
            System.out.println(strings[i]);
    }
}
