// directed in itself
public class FlowEdge {
    private final int v; // source vertex
    private final int w; // dst vertex
    private final double capacity;
    private double flow;

    public FlowEdge(int v, int w, double cap) {
        this.v = v;
        this.w = w;
        this.capacity = cap;
        this.flow = 0.0;
    }
    public int from() {
        return v;
    }
    public int to() {
        return w;
    }
    public double capacity() {
        return capacity;
    }
    public double flow() {
        return flow;
    }
    public int other (int vertex) {
        if (vertex == v)
            return w;
        else
            return v;
    }
    // original edge v->w
    // implements abstract residual network

    // yield edge info of corresponding residual edges
    public double residualCapacityTo(int vertex) {
        if (vertex == w) // forward edge
            return capacity - flow;
        else // backedge
            return flow;
    }
    public void addResidualFlowTo(int vertex, double delta) {
        // add flow to forward edge
        if (vertex == w)
            flow += delta;
        else // add flow to back edge
            flow -= delta;
    }
}