import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.UF;

public class KruskalMST {
    private Queue<Edge> mst;

    public KruskalMST(EdgeWeightedGraph G) {
        mst = new Queue<Edge>();
        Edge[] edges = new Edge[G.E()];
        int i = 0;
        for (var edge : G.edges())
            edges[i++] = edge;
        MinPQ<Edge> pq = new MinPQ<Edge>(edges);
        UF uf = new UF(G.V());

        // add one edge once
        Edge edge;
        while (!pq.isEmpty() && mst.size() < G.V()-1) {
            edge = pq.delMin();
            int v = edge.either();
            int w = edge.other(v);
            if (uf.find(v) != uf.find(w)) {// different tree
                mst.enqueue(edge);
                uf.union(v, w);
            }
        }

    }
    public Iterable<Edge> edges() {
        return mst;
    }

    public double weight() {
        double weight = 0;
        for (var e : edges())
            weight += e.weight();
        return weight;
    }

    public static void main(String[] args) {
        EdgeWeightedGraph G = new EdgeWeightedGraph(new In(args[0]));
		KruskalMST mst = new KruskalMST(G);
		System.out.println("minimum total weights: " + mst.weight());
		for (Edge e : mst.edges())
			System.out.println(e);
		System.out.println("success");
    }

}