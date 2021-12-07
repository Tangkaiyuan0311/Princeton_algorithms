import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;

public class FlowNetwork {

    // adjancy list representation
    private Bag<FlowEdge> [] adj;
    private final int V;
    private int E;

    // v vertex no edge
    public FlowNetwork(int v) {
        V = v;
        E = 0;
        adj = (Bag<FlowEdge> []) new Bag[V];
        for (int i = 0; i < V; i++) {
            adj[i] = new Bag<FlowEdge>();
        }
    }
    public FlowNetwork(In in) {
        this(in.readInt());
        int E = in.readInt();
        if (E < 0) throw new IllegalArgumentException("number of edges must be non-negative");
        for (int i = 0; i < E; i++) {
            int v = in.readInt();
            int w = in.readInt();
            double capacity = in.readDouble();
            addEdge(new FlowEdge(v, w, capacity));
        }
    }

    public int V() {
        return V;
    }
    public int E() {
        return E;
    }
    void addEdge(FlowEdge e) {
        E++;
        int v = e.from();
        int w = e.to();
        adj[v].add(e);
        adj[w].add(e);
    }
    Iterable<FlowEdge> adj(int v) {
        return adj[v];
    }
}
