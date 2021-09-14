import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.In;

public class DepthFirstPaths {
	private boolean[] marked;
	private int[] edgeTo; // parent link
	private final int s; // source

	// construct the tree rooted at s
	public DepthFirstPaths(Graph G, int s) {
		this.s = s;
		marked = new boolean[G.V()]; // false
		edgeTo = new int[G.V()]; // 0
		dfs(G, s);
	}

	private void dfs(Graph G, int v) {
		marked[v] = true;
		for (int w : G.adj(v))
			if (!marked[w]) {
				edgeTo[w] = v;
				dfs(G, w);
			}
	}

	public boolean hasPathTo(int v) {
		return marked[v];
	}

	public Iterable<Integer> PathTo(int v) {
		if (!hasPathTo(v))
			return null;
		var path = new Stack<Integer>();
		for (int x = v; x != s; x = edgeTo[x])
			path.push(x);
		path.push(s);
		return path;
	}

	public static void main(String[] args) {
		Graph G = new Graph(new In(args[0]));
		int s = Integer.parseInt(args[1]);
		var search= new DepthFirstPaths(G, s);
		for (int v = 0; v < G.V(); v++) {
			System.out.print(s + " to " + v + ": ");
			if (search.hasPathTo(v)) {
				for (int x : search.PathTo(v)) {
					if (x == s)
						System.out.print(x);
					else
						System.out.print("-" + x);
				}
			}
			System.out.println();
		}
	}
}
