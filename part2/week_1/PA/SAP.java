import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Queue;

public class SAP {
	
	private final Digraph G;
	// software cache
	int[] cache_s; // v, w, sa, sa_len  vw is exchangeable
	Object[] cache_m; // iterable<Integer> (v, w), sa, sa_len

	// constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph G) {
		// SAP should be immutable, a deep copy constructor, since Digraph is mutable
		this.G = new Digraph(G);
		this.cache_s = new int[]{-1, -1, -1, -1};
		this.cache_m = new Object[]{null, null, null, null};

	}

	private void sap(int v, int w) {
		
		int sa_len = Integer.MAX_VALUE; // shortest ancestor path lenth
        int sa = -1; // shortest ancestor

		if (v == w) {
			sa_len = 0;
			sa = v;
			//cache
			cache_s[0] = v;
			cache_s[1] = w;
			cache_s[2] = sa;
			cache_s[3] = sa_len;
			return;
		}

		// auxilary data structure
		Queue<Integer> qv = new Queue<Integer>();
		Queue<Integer> qw = new Queue<Integer>();
		int[] dv = new int[G.V()];
		int[] dw = new int[G.V()];
		boolean[] markv = new boolean[G.V()];
		boolean[] markw = new boolean[G.V()];
		SET<Integer> setv = new SET<Integer>();
		SET<Integer> setw = new SET<Integer>();
		
		// initailiztion
		qv.enqueue(v);
		dv[v] = 0;
		markv[v] = true;
		setv.add(v);
		
		qw.enqueue(w);
		dw[w] = 0;
		markw[w] = true;
		setw.add(w);


		// two bfs in lockstep
		while (!qv.isEmpty() || !qw.isEmpty()) {
			if (!qv.isEmpty()) {
				// bfs on v
				int x = qv.dequeue();
				if (dv[x]+1 >= sa_len) // next distance is longer than current shortest path, no need to move on
					break;
				for (int i : G.adj(x)) 
					if (!markv[i]) {
						markv[i] = true;
						dv[i] = dv[x] + 1;
						qv.enqueue(i);
						setv.add(i);
						if (setw.contains(i) && (dv[i]+dw[i] < sa_len)) {
							sa_len = dv[i]+dw[i];
							sa = i;
						}
					}
			}
			if (!qw.isEmpty()) {
				// bfs on w
				int y = qw.dequeue();
				if (dw[y]+1 >= sa_len) // next distance is longer than current shortest path, no need to move on
					break;
				for (int i : G.adj(y)) 
					if (!markw[i]) {
						markw[i] = true;
						dw[i] = dw[y] + 1;
						qw.enqueue(i);
						setw.add(i);
						if (setv.contains(i) && (dv[i]+dw[i] < sa_len)) {
							sa_len = dv[i]+dw[i];
							sa = i;
						}
					}
			}
		}

		// sa == -1 if no common ancestor found
		if (sa == -1) {
			assert (sa_len == Integer.MAX_VALUE);
			sa_len = -1;
		}

		// cache
		cache_s[0] = v;
        cache_s[1] = w;
        cache_s[2] = sa;
        cache_s[3] = sa_len;
		return;

	}
	
	
	private void sap(Iterable<Integer> v, Iterable<Integer> w) {
		int sa_len = Integer.MAX_VALUE; // shortest ancestor path lenth
        int sa = -1; // shortest ancestor

		sa = intersects(v, w);
        if (sa != -1) { // these two sets intersects
            sa_len = 0;
            //cache
			cache_m[0] = v;
			cache_m[1] = w;
			cache_m[2] = sa;
			cache_m[3] = sa_len;
            return;
        }

        // auxilary data structure
        Queue<Integer> qv = new Queue<Integer>();
        Queue<Integer> qw = new Queue<Integer>();
        int[] dv = new int[G.V()];
        int[] dw = new int[G.V()];
        boolean[] markv = new boolean[G.V()];
        boolean[] markw = new boolean[G.V()];
        SET<Integer> setv = new SET<Integer>();
        SET<Integer> setw = new SET<Integer>();

        // initailiztion
		for (int i : v) {
			qv.enqueue(i);
			dv[i] = 0;
			markv[i] = true;
			setv.add(i);
		}
		for (int i : w) {
			qw.enqueue(i);
			dw[i] = 0;
			markw[i] = true;
			setw.add(i);
		}

		// two bfs in lockstep
        while (!qv.isEmpty() || !qw.isEmpty()) {
            if (!qv.isEmpty()) {
                // bfs on v
                int x = qv.dequeue();
                if (dv[x]+1 >= sa_len) // next distance is longer than current shortest path, no need to move on
                    break;
                for (int i : G.adj(x))
                    if (!markv[i]) {
                        markv[i] = true;
                        dv[i] = dv[x] + 1;
                        qv.enqueue(i);
                        setv.add(i);
                        if (setw.contains(i) && (dv[i]+dw[i] < sa_len)) {
                            sa_len = dv[i]+dw[i];
                            sa = i;
                        }
                    }
            }
            if (!qw.isEmpty()) {
                // bfs on w
                int y = qw.dequeue();
                if (dw[y]+1 >= sa_len) // next distance is longer than current shortest path, no need to move on
                    break;
                for (int i : G.adj(y))
                    if (!markw[i]) {
                        markw[i] = true;
                        dw[i] = dw[y] + 1;
                        qw.enqueue(i);
                        setw.add(i);
                        if (setv.contains(i) && (dv[i]+dw[i] < sa_len)) {
                            sa_len = dv[i]+dw[i];
                            sa = i;
                        }
                    }
            }
        }
		// sa == -1 if no common ancestor found
        if (sa == -1) {
            assert (sa_len == Integer.MAX_VALUE);
            sa_len = -1;
        }

        // cache
        cache_m[0] = v;
        cache_m[1] = w;
        cache_m[2] = sa;
        cache_m[3] = sa_len;
        return;
	}

	// chaeck whether two collections contain the same elements
	private boolean same(Iterable<Integer> v, Iterable<Integer> w) {
		if ((v == null) || (w == null))
			return false;
		SET<Integer> set = new SET<Integer>();
		for (int i : v)
			set.add(i);
		for (int i : w)
			if (!set.contains(i))
				return false;

		set = new SET<Integer>();
        for (int i : w)
            set.add(i);
        for (int i : v)
            if (!set.contains(i))
                return false;
		return true;
	}

	private int intersects(Iterable<Integer> v, Iterable<Integer> w) {
		SET<Integer> set = new SET<Integer>();
		for (int i : v)
            set.add(i);
        for (int i : w)
            if (set.contains(i)) {
				System.out.println("intersects: " + i);
                return i;
			}
		return -1;
	}

	// length of shortest ancestral path between v and w; -1 if no such path
	public int length(int v, int w) {
		//check cache
		if (((v == cache_s[0]) && (w == cache_s[1])) || ((v == cache_s[1]) && (w == cache_s[0])))
			return cache_s[3];
		else {
			sap(v, w);
			return cache_s[3];
		}
	}

	// a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
	public int ancestor(int v, int w) {
		if (((v == cache_s[0]) && (w == cache_s[1])) || ((v == cache_s[1]) && (w == cache_s[0])))
            return cache_s[2];
        else {
            sap(v, w);
            return cache_s[2];
        }
	}


	// length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		if ((same(v, (Iterable<Integer>)cache_m[0]) && same(w, (Iterable<Integer>)cache_m[1])) || (same(v, (Iterable<Integer>)cache_m[1]) && same(w, (Iterable<Integer>)cache_m[0])))
            return (int)cache_m[3];
        else {
            sap(v, w);
            return (int)cache_m[3];
        }
	}

	// a common ancestor that participates in shortest ancestral path; -1 if no such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		if ((same(v, (Iterable<Integer>)cache_m[0]) && same(w, (Iterable<Integer>)cache_m[1])) || (same(v, (Iterable<Integer>)cache_m[1]) && same(w, (Iterable<Integer>)cache_m[0])))
            return (int)cache_m[2];
        else {
            sap(v, w);
            return (int)cache_m[2];
        }
	}


	// do unit testing of this class
	public static void main(String[] args) {
		In in = new In(args[0]);
		Digraph G = new Digraph(in);
		SAP sap = new SAP(G);
		/*
		SET<Integer> v = new SET<Integer>();
		v.add(0);
		//v.add(2);
		SET<Integer> w = new SET<Integer>();
		w.add(8);
		w.add(9);
		int length   = sap.length(v, w);
        int ancestor = sap.ancestor(v, w);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
		*/
		/*
		while (!StdIn.isEmpty()) {
			int v = StdIn.readInt();
			int w = StdIn.readInt();
			int length   = sap.length(v, w);
			int ancestor = sap.ancestor(v, w);
			StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
		}
		*/
	}
}
