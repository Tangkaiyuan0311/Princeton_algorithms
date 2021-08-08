import edu.princeton.cs.algs4.StdIn;

public class Permutation {
	public static void main(String[] args) {
		int k = Integer.parseInt(args[0]);
		if (k == 0) {
            return;
        }
		var queue = new RandomizedQueue<String>();
		while (!StdIn.isEmpty()) {
			String a = StdIn.readString();
			queue.enqueue(a);
		}
		
		int i = 0;
		for (String s : queue) {
			System.out.println(s);
			i++;
			if (i == k)
				break;
		}
	}
}
