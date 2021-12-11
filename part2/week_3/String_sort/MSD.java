import edu.princeton.cs.algs4.Alphabet;
import edu.princeton.cs.algs4.In;

public class MSD {
    private final Alphabet alph;
    private final int R;
    private final int M = 3;
    private String[] aux;
    MSD(Alphabet alph) {
        this.alph = alph;
        R = alph.radix();
    }

    private int charAt(String s, int d) {
        if (d < s.length())
            return alph.toIndex(s.charAt(d)); // 0~R-1
        else
            return -1; // -1 on end of string
    }
    public void sort(String[] a) {
        int N = a.length;
        aux = new String[N];
        sort(a, 0, N-1, 0);
    }
    private void exch(String[] a, int i , int j) {
        String s = a[i];
        a[i] = a[j];
        a[j] = s;
    }
    private boolean less(String s1, String s2, int d) {
        return s1.substring(d).compareTo(s2.substring(d)) < 0;
    }
    private void insertion_sort(String[] a, int lo, int hi, int d) {
        for (int i = lo+1; i <= hi; i++) {
            // insert a[i] into ordered a[lo]~a[i-1]
            for (int j = i; j > lo && less(a[j], a[j-1], d); j--)
                exch(a, j , j-1);
        }
    }
    // sort a[lo]~a[hi] starting from char at position d, includes empty string
    private void sort(String[] a, int lo, int hi, int d) {
        if (hi <= lo+M) {
            insertion_sort(a, lo, hi, d);
            return;
        }
        // allocate count array
        int[] count = new int[R+2];

        // frequency counting
        // -1, 0~R-1 ---> count[1]~count[R+1]
        for (int i = lo; i <= hi; i++)
            count[charAt(a[i], d)+2]++;
        
        // convert to starting index(0 started)
        // -1, 0~R-1 ---> count[0]~count[R]
        for (int r = 1; r <= R; r++)
            count[r] += count[r-1];
        
        // distribution
        for (int i = lo; i <= hi; i++)
            aux[count[charAt(a[i], d)+1]++] = a[i];

        // copy back
        for (int i = lo; i <= hi; i++)
            a[i] = aux[i-lo];

        // now count[0] is the starting index of char 0 and so forth
        // do not recurse into the empty string
        for (int r = 0; r < R; r++) {
            sort(a, lo+count[r], lo+count[r+1]-1, d+1);
        }
        
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt(); // number of String
        String[] strings = new String[N];
        for (int i = 0; i < N; i++)
            strings[i] = in.readString();
        MSD msd = new MSD(Alphabet.LOWERCASE);
        msd.sort(strings);
        for (int i = 0; i < N; i++)
            System.out.println(strings[i]);
    }
}
