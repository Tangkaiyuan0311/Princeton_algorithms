import java.util.*;
public class Insertion {
	public static void sort(Comparable[] a) {
		int N = a.length;
		for (int i = 1; i < N; i++) { // 0 ~ i-1 already in order
			for (int j = i; j > 0; j--) {
				if (less(a[j], a[j-1]))
					exch(a, j, j-1);
				else
					break;
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
