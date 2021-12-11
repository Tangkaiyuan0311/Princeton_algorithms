import edu.princeton.cs.algs4.In;

public class LSD {
    // sort a[] on leading w characters
    public static void sort(String[] a, int w) {
        int N = a.length;
        int R = 256;
        int[] count;
        String[] aux = new String[N];
        for (int d = w-1; d >= 0; d--) {
            count = new int[R+1];

            // frequency count, count of r is stored in count[r+1]
            for (int i = 0; i < N; i++)
                count[a[i].charAt(d)+1]++; 
            
            // frequency to index
            for (int r = 1; r < R; r++) // get starting index of r
                // count[0] == 0
                count[r] = count[r-1] + count[r];
            
            // distribute the data
            for (int i = 0; i < N; i++)
                aux[count[a[i].charAt(d)]++] = a[i];
            // copy back
            for (int i = 0; i < N; i++)
                a[i] = aux[i];
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int num = in.readInt();
        int l = in.readInt(); // num of chars per string
        String[] strings = new String[num];
        for (int i = 0; i < num; i++)
            strings[i] = in.readString();
        LSD.sort(strings, l);
        for (int i = 0; i < num; i++)
            System.out.println(strings[i]);
    }
}