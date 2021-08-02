import java.util.*;
public class Shell {
	public static void sort(Comparable[] a) {
		int N = a.length;
		// find the h value
		int h = 1;
		while (h < N/3)
			h = 3 * h + 1;
		for (; h >= 1; h /= 3) {
			// h sort the array
			for (int i = h; i < N; i++) {
				for (int j = i; j >= h; j -= h) {
					if (less(a[j], a[j-h]))
						exch(a, j, j-h);
				}
			}
		}
	}

	private static boolean less(Comparable v, Comparable w) {
		return v.compareTo(w) < 0;
	}

	private static void exch(Comparable[] a, int i, int j) {
		Comparable tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
	}

	private static void print(Comparable[] a) {
		for (int i = 0; i < a.length; i++)
			System.out.print(a[i] + " ");
		System.out.println();
	}

	public static boolean isSorted(Comparable[] a) {
		for (int i = 1; i < a.length; i++) {
			if (less(a[i], a[i-1]))
				return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		List<Integer> list = new ArrayList<Integer>();
		while (scanner.hasNextInt())
			list.add(scanner.nextInt());
		Integer[] arr = list.toArray(new Integer[0]);
		sort(arr);
		print(arr);
		System.out.println("sorted? : " + isSorted(arr));
	}
}
