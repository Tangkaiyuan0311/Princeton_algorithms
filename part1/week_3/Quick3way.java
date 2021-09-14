import edu.princeton.cs.algs4.StdRandom;
// -classpath /home/tky/algs4:.
public class Quick3way {
	public static void sort(Comparable[] a) {
		StdRandom.shuffle(a);
		sort(a, 0, a.length-1);
	}

	private static void sort(Comparable[] a, int lo, int hi) {
		if (lo >= hi)
			return;
		Comparable v = a[lo];
		int lt = lo;
		int i = lo+1;
		int gt = hi;
		while (i <= gt) {
			if (less(a[i], v)) 
				exch(a, i++, lt++);
			
			else if (less(v, a[i])) 
				exch(a, i, gt--);
			else
				i++;
		}
		sort(a, lo, lt-1);
		sort(a, gt+1, hi);
	}

	private static boolean less(Comparable v, Comparable w) {
		return v.compareTo(w) < 0;
	}

	private static void exch(Comparable[] a, int i, int j) {
		Comparable tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
	}

}
