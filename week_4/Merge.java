public class Merge {
	private static Comparable[] aux;

	public static void sort(Comparable[] a) {
		aux = new Comparable[a.length];
		sort(a, 0, a.length-1);
	}

	private static void sort(Comparable[] a, int lo, int hi) {
		// base case
		if (hi <= lo)
			return;
		int mid = lo + (hi-lo)/2;
		sort(a, lo, mid);
		sort(a, mid+1, hi);
		merge(a, lo, mid, hi); // lo <= mid < hi
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
