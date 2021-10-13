import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;

public class EdgeWeightedGraph {
	private final int V;
	private int E;
	private Bag<Edge>[] adj;
	
	public EdgeWeightedGraph(int v) {
		this.V = v;
		this.E = 0;
		adj = (Bag<Edge>[]) new Bag[v];
		for (int i = 0; i < V; i++)
			adj[i] = new Bag<Edge>();
	}
	
	public EdgeWeightedGraph(In in) {
		this(in.readInt());
		int E = in.readInt();
		for (int i = 0; i < E; i++) {
			int v = in.readInt();
			int w = in.readInt();
			double weight = in.readDouble();
			Edge e = new Edge(v, w, weight);
			addEdge(e);
		}
	}
	
	public int V() {
		return V;
	}
	public int E() {
		return E;
	}
	
	private void addEdge(Edge e) {
		int v = e.either();
		int w = e.other(v);
		adj[v].add(e);
		adj[w].add(e);
		E++;
	}
	
	public Iterable<Edge> adj(int v) {
		return adj[v];
	}
	public Iterable<Edge> edges() {
		Bag<Edge> edges = new Bag<Edge>();
		for (int v = 0; v < V; v++) {
			for (Edge e : adj[v]) {
				if (e.other(v) > v)
					edges.add(e);
			}
		}
		return edges;
	}
	
	public static void main(String[] args) {
		In in = new In(args[0]);
		var G = new EdgeWeightedGraph(in);
		System.out.println(G.V());
		System.out.println(G.E());
		for (var e : G.edges()) {
			System.out.println(e);
		}
		System.out.println("success");
	}
	
	
	
	
	
	
	
}
