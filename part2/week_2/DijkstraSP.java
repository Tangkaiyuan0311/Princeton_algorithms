import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.Stack;

public class DijkstraSP {
	private DirectedEdge[] edgeTo;
	private double[] distTo;
	private IndexMinPQ<Double> pq; //non-tree vertices with known path

	public DijkstraSP(EdgeWeightedDigraph G, int s) {
		edgeTo = new DirectedEdge[G.V()];
		distTo = new double[G.V()];
		pq = new IndexMinPQ<Double>(G.V());

		for (int v = 0; v < G.V(); v++)
			distTo[v] = Double.POSITIVE_INFINITY;
		distTo[s] = 0.0;
		pq.insert(s, 0.0);
		while (!pq.isEmpty())
			relax(G, pq.delMin());
	}
	// sp(s->v) has been found, relax v
	private void relax(EdgeWeightedDigraph G, int v) {
		for (DirectedEdge e : G.adj(v)) {
			int w = e.to(); //v->w
			if (distTo[w] > distTo[v] + e.weight()) {
				distTo[w] = distTo[v] + e.weight();
				edgeTo[w] = e;
				if (pq.contains(w))
					pq.changeKey(w, distTo[w]);
				else
					pq.insert(w, distTo[w]);
			}

		}
	}

	public double distTo(int v) {
		return distTo[v];
	}
	public boolean hasPathTo(int v) {
		return distTo[v] < Double.POSITIVE_INFINITY;
	}
	public Iterable<DirectedEdge> pathTo(int v) {
		if (hasPathTo(v)) {
			Stack<DirectedEdge> s = new Stack<DirectedEdge>();
			for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
				s.push(e);
			}
			return s;
		}
		else
			return null;
	}

	public static void main(String[] args) {
		In in = new In(args[0]);
		var G = new EdgeWeightedDigraph(in);
		var sp = new DijkstraSP(G, 0);
		for (int v = 1; v < G.V(); v++) {
			if (sp.hasPathTo(v)) {
				System.out.print("0->" + v + ": ");
				for (var edge : sp.pathTo(v))
					System.out.print(edge + " ");
				System.out.println();
			}
		}
		System.out.println("success");
	}

}
