import java.util.Iterator; // iterator interface

public class ResizingArrayStack<Item> implements Iterable<Item> {
	private Item[] a = (Item[]) new Object[1]; // stack array
	private int N = 0; // items count, a[0] ~ a[N-1]

	public boolean isEmpty() {
		return (N == 0);
	}
	public int size() {
		return N;
	}

	private void resize(int max) {
		Item[] tmp = (Item[]) new Object[max];
		for (int i = 0; i < N; i++) {
			tmp[i] = a[i]; // reference copy
		}
		a = tmp; // old array a of Item reference will be reclaimed afterwards
	}

	public void push(Item item) {
		if (N == a.length) // full stack already
			resize(2*a.length);
		a[N++] =item;
	}
	public Item pop() {
		Item item = a[--N];
		a[N] = null; // prevent loitering
		if ((N > 0) && (N == a.length/4))
			resize(a.length/2);
		return item;
	}

	public Iterator<Item> iterator() { // public interface Iterable<Item>
		return new stackIterator();
	}

	private class stackIterator implements Iterator<Item> {
		private int cnt = N;
		public boolean hasNext() {
			return cnt > 0;
		}
		public Item next() {
			return a[--cnt];
		}
		public void remove() {}
	}
}
