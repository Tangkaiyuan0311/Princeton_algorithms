import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {

	private Node first;
	private Node last;
	private int N;

	private class Node {
		Item item;
		Node next;
		Node prev;
	}

    // construct an empty deque
    public Deque() {
		N = 0;
		first = null;
		last = null;
	}

    // is the deque empty?
    public boolean isEmpty() {
		return N == 0;
	}

    // return the number of items on the deque
    public int size() {
		return N;
	}

    // add the item to the front
    public void addFirst(Item item) {
		if (item == null) {
			throw new IllegalArgumentException("null object reference");
		}
		Node old_first = first;
		first = new Node();
		first.item = item;
		first.next = old_first;
		first.prev = null;
		if (old_first == null) // empty deque
			last = first;
		N++;
	}

    // add the item to the back
    public void addLast(Item item) {
		if (item == null) {
            throw new IllegalArgumentException("null object reference");
        }
		Node old_last = last;
		last = new Node();
		last.item = item;
		last.next = null;
		last.prev = old_last;
		if (old_last == null) {
			first = last;
		}
		else
			old_last.next = last;
		N++;
	}

    // remove and return the item from the front
    public Item removeFirst() {
		if (isEmpty())
			throw new java.util.NoSuchElementException("empty deque");
		Item item = first.item;
		first = first.next;
		if (first == null) // empty deque
			last = null;
		else
			first.prev = null;
		N--;
		return item;
	}

    // remove and return the item from the back
    public Item removeLast() {
		if (isEmpty())
            throw new java.util.NoSuchElementException("empty deque");
		Item item = last.item;
		last = last.prev;
		if (last == null)
			first = null;
		else
			last.next = null;
		N--;
		return item;
	}

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
		return new DequeIterator();
	}
	
	private class DequeIterator implements Iterator<Item> {
        private Node current = first;
        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
			if (current == null)
				throw new java.util.NoSuchElementException("no more elements");
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
			throw new UnsupportedOperationException();
		}
    }


    // unit testing (required)
    public static void main(String[] args) {
		var queue = new Deque<String>();
		System.out.println("empty deque? " + queue.isEmpty());
		queue.addFirst("a");
		queue.addLast("b");
		queue.addFirst("c");
		queue.addLast("d");
		System.out.println("expected output: " + "c a b d");
		for (String s : queue) {
			System.out.print(s + " ");
		}
		System.out.println();
		System.out.println("size = " + queue.size());
		String a;
		a = queue.removeFirst();
		assert(a.equals("c"));
		a = queue.removeLast();
		assert(a.equals("d"));
		System.out.println("expected output: " + "a b");
        for (String m : queue) {
            System.out.print(m);
        }
		System.out.println();
        System.out.println("size = " + queue.size());

		while (true) {
			queue.removeLast();
		}

	}

}
