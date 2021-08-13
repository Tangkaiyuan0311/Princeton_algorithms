public class MergeBU {
    private static Comparable[] aux;

    public static void sort(Comparable[] a) {
		aux = new Comparable[a.length];
		int N = a.length;
        for (int sz = 1; sz < N; sz*=2) {
            // subarrays of sz is sorted
            // merge into subarrays of size 2sz
            for (int lo = 0; lo < N - sz; lo += 2*sz) {
                merge(a, lo, lo+sz-1, Math.min(lo+sz+sz-1, N-1));
                // satisfy that: lo <= mid < hi
            }
        }
    }
    
    
	private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    private static void merge (Comparable[] a, int lo, int mid, int hi) {
		for (int k = lo; k < hi+1; k++)
            aux[k] = a[k];
        // assume lo <= mid < hi
        int i = lo, j = mid+1; // scanner
        for (int k = lo; k < hi+1; k++) {
            if (i > mid)
                a[k] = aux[j++];
            else if (j > hi)
                a[k] = aux[i++];
            else if (less(aux[j], aux[i]))
                a[k] = aux[j++];
            else // aux[i] <= aux[j], stable sorting
                a[k] = aux[i++];
        }
    }
}
