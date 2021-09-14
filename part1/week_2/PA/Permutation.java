import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
	public static void main(String[] args) {
		int k = Integer.parseInt(args[0]);
		if (k == 0) {
            return;
        }
		/*
		String[] arr = new String[k];
		int random_id;
		for (int i = 0; !StdIn.isEmpty(); i++) {
			String a = StdIn.readString();
			if (i < k)
				arr[i] = a;
			else {
				random_id = StdRandom.uniform(i+1); // [0, 1, ...i]
				if (random_id < k)
					arr[random_id] = a;
			}
		}
		for (String s : arr)
			System.out.println(s);
		*/
		
		var queue = new RandomizedQueue<String>();
		int i = 0;
		int random_id;
		while (!StdIn.isEmpty()) {
			String a = StdIn.readString();
			if (i < k) {
				queue.enqueue(a);
			}
			else {
				random_id = StdRandom.uniform(i+1); // [0, 1, ...i]
                if (random_id < k) {
					queue.dequeue();
					queue.enqueue(a);
				}
			}
			i++;
		}
		
		for (String s : queue) {
			System.out.println(s);
		}
	}
}
