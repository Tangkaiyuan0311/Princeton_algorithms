import java.util.*;
public class SortCompare {
	public static double time(String alg, Double[] a) {
		long start = System.currentTimeMillis();
		if (alg.equals("Insertion"))	Insertion.sort(a);
		if (alg.equals("Selection"))    Selection.sort(a);
		if (alg.equals("Shell"))        Shell.sort(a);
		if (alg.equals("Merge"))        Merge.sort(a);
		if (alg.equals("MergeBU"))      MergeBU.sort(a);
		if (alg.equals("Quick"))        Quick.sort(a);
		if (alg.equals("Quick3way"))    Quick3way.sort(a);
		//if (alg.equals("Heap"))         Heap.sort(a);
		long now = System.currentTimeMillis();
        return (now - start) / 1000.0;
	}

	public static double timeRandomInput(String alg, int N, int T) {
		// perform T trials of N-length array
		// return total time needed
		double total = 0.0;
		Double[] a = new Double[N];
		var generator = new Random();
		for (int i = 0; i < T; i++) {
			for (int j = 0; j < N; j++)
				a[j] = generator.nextDouble();
			total += time(alg, a);
		}
		return total;
	}

	public static void main(String[] args) {
		//System.out.println("alg1, alg2, N, T :");
		String alg1 = args[0];
		String alg2 = args[1];
		int N = Integer.parseInt(args[2]);
		int T = Integer.parseInt(args[3]);
		double t1 = timeRandomInput(alg1, N, T);
		double t2 = timeRandomInput(alg2, N, T);
		System.out.println(alg1 + ": " + t1 + " seconds");
		System.out.println(alg2 + ": " + t2 + " seconds");
	}
}

