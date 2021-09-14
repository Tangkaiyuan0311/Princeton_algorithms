import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {


	private int head;
	private int tail;
	private int N;
	private Item[] a;

    // construct an empty randomized queue
    public RandomizedQueue() {
		head = 0;
		tail = 0;
		N = 0;
		a = (Item[]) new Object[1];
	}

	/*
	public RandomizedQueue(int size) {
		head = 0;
        tail = 0;
        N = 0;
        a = (Item[]) new Object[size];
    }
	*/

    // is the randomized queue empty?
    public boolean isEmpty() {
		return N == 0;
	}

    // return the number of items on the randomized queue
    public int size() {
		return N;
	}

	private void swap(Item[] a, int i, int j) {
		Item temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}
	

	// copy current queue elements delimited by head and tail into a new array of length MAX, reset the index
	private void resize(int MAX) {
		Item[] tmp = (Item[]) new Object[MAX];
		int capacity = a.length;
		int current = head;
		// element copy
		for (int i = 0; i < N; i++) {
			tmp[i] = a[current];
			current = (current + 1) % capacity;
		}
		// update instance fields
		head = 0;
		tail = N;
		a = tmp;

	}

    // add the item
    public void enqueue(Item item) {
		if (item == null)
			throw new IllegalArgumentException("null argument");
		if (N == a.length)
			resize(2 * a.length);
		int old_tail = tail; // the index of the enqueued element
		a[tail] = item;
		N++;
		tail = (tail + 1) % a.length;

		// exchange newly inserted element with a random queue element
		int random_item = StdRandom.uniform(N); // get a random queue item between [0, N-1]
		int random_id = (head + random_item) % a.length;
		swap(a, old_tail, random_id);
	}

    // remove and return a random item
    public Item dequeue() {
		if (N == 0)
			throw new java.util.NoSuchElementException("empty queue");
		Item tmp = a[head];
		a[head] = null; // prevent loitering
		head = (head + 1) % a.length;
		N--;
		if (N > 0 && N == a.length/4)
			resize(a.length/2);
		return tmp;
	}

    // return a random item (but do not remove it)
    public Item sample() {
		if (N == 0)
            throw new java.util.NoSuchElementException("empty queue");
		int random_id = (head + StdRandom.uniform(N)) % a.length;
		return a[random_id];
	}

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
		return new QueueIterator();
	}

	private class QueueIterator implements Iterator<Item> {
		// should be random
		// need linear amount of extra memory

		private final Item[] arr;
		private int id;

		public QueueIterator() {
			arr = (Item[]) new Object[N];
			id = 0;
            // local variable
			int i = 0;
			int current = head;
			int random_id;
			for (i = 0; i < N; i++) {
				arr[i] = a[current];
				// exchange arr[i] randomly
				random_id = StdRandom.uniform(i+1); // get a random queue item between [0, i]
				swap(arr, random_id, i);
				
				current = (current + 1) % a.length;
			}
		}

		public boolean hasNext() {
			return id < N;
		}

		public Item next() {
			if (id == N)
                throw new java.util.NoSuchElementException("no more items");
			return arr[id++];
		}
		/*
		private int current = head;
		private int cnt = 0;

		public boolean hasNext() {
			return cnt < N; 
		}

		public Item next() {
			if (cnt == N)
				throw new java.util.NoSuchElementException("no more items");
			Item tmp = a[current];
			current = (current + 1) % a.length;
			cnt++;
			return tmp;
		}
		*/
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

    // unit testing (required)
    public static void main(String[] args) {
		var queue = new RandomizedQueue<Integer>();
		assert(queue.isEmpty());
		for (int i = 0; i < 10; i++) {
			queue.enqueue(i);
			System.out.println("iterat the queue...");
			for (int j : queue) {
				System.out.print(j + " ");
			}
			System.out.println();
		}
		assert(queue.size() == 10);
		System.out.println("iterat the queue...");
		for (int j : queue) {
			System.out.print(j + " ");
		}
		System.out.println();
		System.out.println("sample 10 times...");
		for (int m = 0; m < 10; m++)
			System.out.print(queue.sample() + " ");
		System.out.println();
		System.out.println("deque 5 elements...");
		for (int k = 0; k < 5; k++)
			System.out.print(queue.dequeue() + " ");
		assert(queue.size() == 5);
		System.out.println("iterat the queue...");
        for (int l : queue) 
            System.out.print(l + " ");
		System.out.println("success!");
	}

}
