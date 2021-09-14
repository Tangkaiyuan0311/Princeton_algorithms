import edu.princeton.cs.algs4.StdRandom;
// -classpath /home/tky/algs4:.
public class Quick {
	public static void sort(Comparable[] a) {
		StdRandom.shuffle(a);
		sort(a, 0, a.length-1);
	}

	private static void sort(Comparable[] a, int lo, int hi) {
		if (lo >= hi) // base
			return;
		int j = partition(a, lo, hi);
		sort(a, lo, j-1);
		sort(a, j+1, hi);
	}

	private static boolean less(Comparable v, Comparable w) {
		return v.compareTo(w) < 0;
	}

	private static void exch(Comparable[] a, int i, int j) {
		Comparable tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
	}

	private static int partition(Comparable[] a, int lo, int hi) {
		Comparable v = a[lo]; // partition element
		int i = lo, j = hi+1; // scanner
		while (true) {
			while (less(a[++i], v)) {
				if (i == hi)
					break;
			} // reach here: a[i] >= v || i == hi
			while (less(v, a[--j])) {
				if (j == lo)
					break;
			} // reach here: a[j] <= v || j == lo

			if (i >= j)
				break;
			exch(a, i, j);
		}

		exch(a, lo, j);
		return j;
	}
}

