import java.util.Iterator;

public class Queue<Item> implements Iterable<Item> {
	private Node first;
	private Node last;
	private int N;

	private class Node {
		Item item;
		Node next;
	}

	public boolean isEmpty() {
		return N == 0;
	}

	public int size() {
		return N;
	}

	public void enqueue(Item item) {
		Node old_last = last;
		last = new Node();
		last.item = item;
		last.next = null;
		//old_last.next = last;
		if (isEmpty()) // enqueue an empty queue
			first = last;
		else
			old_last.next = last;
		N++;
	}

	public Item dequeue() {
		Item item = first.item;
		first = first.next;
		N--; // isEmpty() check N, so we must update N
		if (isEmpty()) // dequeue a one-element queue
			last = null;
		return item;
	}

	public Iterator<Item> iterator() {
		return new QueueIterator();
	}

	private class QueueIterator implements Iterator<Item> {
		private Node current = first;
		public boolean hasNext() {
			return current != null;
		}

		public Item next() {
			Item item = current.item;
			current = current.next;
			return item;
		}

		public void remove() {}
	}
}

