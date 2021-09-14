import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;

public class Graph {
	private final int V; // number of vertices
	private int E; // number of edges
	private Bag<Integer>[] adj; // adjacency lists

	// V vertices, no edges
	public Graph(int V) {
		this.V = V;
		this.E = 0;
		adj = (Bag<Integer>[])new Bag[V];
		for (int v = 0; v < V; v++)
			adj[v] = new Bag<Integer>();
	}

	// construct from input files
	public Graph(In in) {
		this(in.readInt());
		int E = in.readInt();
		for (int i = 0; i < E; i++) {
			int v = in.readInt();
			int w = in.readInt();
			addEdge(v, w);
		}
	}

	public void addEdge(int v, int w) {
		E++;
		adj[v].add(w);
		adj[w].add(v);
	}

	public Iterable<Integer> adj(int v) {
		return adj[v];
	}

	public String toString() {
		String s = V + " vertices " + E + " edges\n";
		for (int v = 0; v < V; v++) {
			s += v + " :";
			for (int w : this.adj[v])
				s += w + " ";
			s += "\n";
		}
		return s;
	}

	public int V() {
		return V;
	}
	public int E() {
		return E;
	}


	public static void main(String[] args) {
		In in = new In(args[0]);
		Graph G = new Graph(in);
		System.out.println(G);
	}
}
