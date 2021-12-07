import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;

public class FordFulkerson {
    private double value;
    private boolean[] marked;
    private FlowEdge[] edgeTo;
    // operate on a shared link to a FlowNetwork
    // attention: G is mutable
    public FordFulkerson(FlowNetwork G, int s, int t) {
        value = 0.0;
        double bottleneck = Double.POSITIVE_INFINITY;
        while (hasAugmentingPath(G, s, t)) {
            // find the bottleneck capacity
            for (int v = t; v != s; v = edgeTo[v].other(v)) {
                FlowEdge e = edgeTo[v];
                if (e.residualCapacityTo(v) < bottleneck)
                    bottleneck = e.residualCapacityTo(v);
            }
        
            // add bottleneck flow along the augmenting path
            for (int v = t; v != s; v = edgeTo[v].other(v)) {
                FlowEdge e = edgeTo[v];
                e.addResidualFlowTo(v, bottleneck);
            }
            value += bottleneck;
        }

    }
    private boolean hasAugmentingPath(FlowNetwork G, int s, int t) {
        // bfs search on G's residual network
        marked = new boolean[G.V()]; // false initialized
        edgeTo = new FlowEdge[G.V()]; // null initialized
        Queue<Integer> q = new Queue<Integer>();
        marked[s] = true;
        q.enqueue(s);
        while (!q.isEmpty()) {
            int v = q.dequeue(); // examine edges of v;
            for (FlowEdge e : G.adj(v)) {
                int other = e.other(v);
                if (e.residualCapacityTo(other) > 0 && !marked[other]) {
                    marked[other] = true;
                    edgeTo[other] = e;
                    q.enqueue(other);
                }
            }
        }
        return marked[t];
    }
    public double value() {
        return value;
    }

    public static void main(String[] args) {
        FlowNetwork G = new FlowNetwork(new In(args[0]));
        int s = 0, t = G.V() - 1;
        FordFulkerson maxflow = new FordFulkerson(G, s, t);
        System.out.println("Max flow = " + maxflow.value());
    }

}
