import java.util.Iterator;
public class Stack<Item> implements Iterable<Item> {
	private Node first = null; // stack top
	private int N = 0; // intem counter

	private class Node {
		Item item;
		Node next;
	}

	public boolean isEmpty() {
		return first == null;
	}

	public int size() {
		return N;
	}

	public void push(Item item) {
		Node old_first = first;
		first = new Node();
		first.item = item;
		first.next = old_first;
		N++;
	}

	public Item pop() {
		Item item = first.item;
		first = first.next;
		N--;
		return item;
		//old first Node will be reclaimed
	}

	public Iterator<Item> iterator() {
		return new StackIterator();
	}

	private class StackIterator implements Iterator<Item> {
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


