import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.In;

public class PrimMST {
	private Edge[] edgeTo; // shortest edge from growing tree
	private double[] distTo; // distTo[w] = edgeTo[w].weight()
	private boolean[] marked; // on the tree ?
	private IndexMinPQ<Double> pq; // eligible crossing edge between current tree
	
	// build the mst]
	public PrimMST(EdgeWeightedGraph G) {
		edgeTo = new Edge[G.V()]; // null initialized
		distTo = new double[G.V()];
		marked = new boolean[G.V()];
		pq = new IndexMinPQ<Double>(G.V());
		for (int v = 0; v < G.V(); v++) {
			distTo[v] = Double.POSITIVE_INFINITY;
		}
		// grow tree from 0 vetex
		edgeTo[0] = null;
		distTo[0] = 0.0;
		pq.insert(0, 0.0);
		while (!pq.isEmpty())
			// grow one edge/vertex
			visit(G, pq.delMin());
	}

	// add v to the tree, update data structure
	private void visit(EdgeWeightedGraph G, int v) {
		marked[v] = true;
		for (Edge e : G.adj(v)) {
			int w = e.other(v); // e: v-w
			if (marked[w]) // tree edge
				continue;
			if (e.weight() < distTo[w]) {
				edgeTo[w] = e;
				distTo[w] = e.weight();
				if (pq.contains(w))
					pq.change(w, distTo[w]);
				else
					pq.insert(w, distTo[w]);
			}
		}
	}

	private Iterable<Edge> edges() {
		Queue<Edge> mst = new Queue<Edge>();
		for (int v = 1; v < marked.length; v++)
			if (edgeTo[v] != null)
				mst.enqueue(edgeTo[v]);
		return mst;
	}
	private double weight() {
		double weight = 0;
		for (Edge e : edges())
			weight += e.weight();
		return weight;
	}

	public static void main(String[] args) {
		EdgeWeightedGraph G = new EdgeWeightedGraph(new In(args[0]));
		PrimMST mst = new PrimMST(G);
		System.out.println("minimum total weights: " + mst.weight());
		for (Edge e : mst.edges())
			System.out.println(e);
		System.out.println("success");
	}

}
