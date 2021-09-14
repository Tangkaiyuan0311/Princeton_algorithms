public class MaxPQ<Key extends Comparable<Key>> {
	private Key[] pq;
	private int N = 0;

	public MaxPQ(int maxN) {
		pq = (Key[]) new Comparable[maxN+1];
	}

	public static void sort(Comparable[] a) {
		int N = a.length;
		for (int i = N/2; i >= 1; i--) {
			sink(a, i, N);
		}
		while (N > 1) {
			exch(a, 1, N--);
			sink(a, 1, N);
		}
	}

	private static void sink(Comparable[] a, int k, int N) {
		int j;
        while (2 * k <= N) {// at least one child
            // select bigger child as j
            if (2 * k + 1 > N || less(a, 2 * k + 1, 2 * k)) {
                j = 2 * k;
            }
            else {
                j = 2 * k + 1;
            }
            // check heap property
            if (less(a, k, j)) {
                exch(a, k, j);
                k = j;
            }
            else
                break;
        }
	}

	// k is from [1~N]
	private static void swim(Comparable[] a, int k) {
		while (k > 1 && less(a, k/2, k)) {
            exch(a, k, k/2);
            k = k/2;
        }
    }
	// i, j is from [1~N]
	private static void exch(Comparable[] a, int i, int j) {
		Comparable tmp = a[i-1];
		a[i-1] = a[j-1];
		a[j-1] = tmp;
	}

	private static boolean less(Comparable[] a, int i, int j) {
		return (a[i-1].compareTo(a[j-1]) < 0);
	}

	public boolean isEmpty() {
		return N == 0;
	}

	public int size() {
		return N;
	}

	private boolean less(int i, int j) {
		return (pq[i].compareTo(pq[j]) < 0);
	}
	private void exch(int i, int j) {
		 Key tmp = pq[i];
		 pq[i] = pq[j];
		 pq[j] = tmp;
	}
	private void swim(int k) {
		while (k > 1 && less(k/2, k)) {
			exch(k, k/2);
			k = k/2;
		}
	}
	private void sink(int k) {
		// 2k, 2k+1
		int j;
		while (2 * k <= N) {// at least one child
			// select bigger child as j
			if (2 * k + 1 > N || less(2 * k + 1, 2 * k)) {
				j = 2 * k;
			}
			else {
				j = 2 * k + 1;
			}
			// check heap property
			if (less(k, j)) {
				exch(k, j);
				k = j;
			}
			else
				break;
		}
	}

	public void insert(Key key) {
		pq[++N] = key;
		swim(N);
	}

	public Key delMax() {
		Key ret = pq[1];
		pq[1] = pq[N];
		pq[N--] = null;
		sink(1);
		return ret;
	}

	public static void main(String[] args) {
		var pq = new MaxPQ<Integer>(1024);
		for (int i = 0; i < 10; i++) {
			pq.insert(i);
		}
		for (int i = 0; i < 10; i++)
			System.out.print(pq.delMax() + " ");
	}
}
