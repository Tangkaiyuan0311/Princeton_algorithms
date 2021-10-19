import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;

public class EdgeWeightedDigraph {
	private final int V;
	private int E;
	private Bag<DirectedEdge>[] adj;
	
	public EdgeWeightedDigraph(int v) {
		this.V = v;
		this.E = 0;
		adj = (Bag<DirectedEdge>[]) new Bag[v];
		for (int i = 0; i < V; i++)
			adj[i] = new Bag<DirectedEdge>();
	}
	
	public EdgeWeightedDigraph(In in) {
		this(in.readInt());
		int E = in.readInt();
		for (int i = 0; i < E; i++) {
			int v = in.readInt();
			int w = in.readInt();
			double weight = in.readDouble();
			DirectedEdge e = new DirectedEdge(v, w, weight);
			addEdge(e);
		}
	}
	
	public int V() {
		return V;
	}
	public int E() {
		return E;
	}
	
	private void addEdge(DirectedEdge e) {
		int v = e.from();
		int w = e.to();
		adj[v].add(e);
		E++;
	}
	
	public Iterable<DirectedEdge> adj(int v) {
		return adj[v];
	}
	public Iterable<DirectedEdge> edges() {
		Bag<DirectedEdge> edges = new Bag<DirectedEdge>();
		for (int v = 0; v < V; v++) {
			for (DirectedEdge e : adj[v]) {
				edges.add(e);
			}
		}
		return edges;
	}
	
	public static void main(String[] args) {
		In in = new In(args[0]);
		var G = new EdgeWeightedDigraph(in);
		System.out.println(G.V());
		System.out.println(G.E());
		for (var e : G.edges()) {
			System.out.println(e);
		}
		System.out.println("success");
	}
}
