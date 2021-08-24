import java.util.Comparator;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.In;

public class Solver {

	private MinPQ<Node> pq;
	// private int moves;
	private Node goal; // termination node
	private boolean solvable;
	
	// search node data type
	private class Node {
		public static hamming_cmp = new hamming_order();
		public static manhattan_cmp = new manhattan_order();
		public final Board board;
		public final int moves;
		public final Node prev;
		public final int hamming;
		public final int manhattan;
		public Node(Board board, int moves, Node prev) {
			this.board = board;
			this.moves = moves;
			this.prev = prev;
			this.hamming = board.hamming();
			this.manhattan = board.manhattan();
		}
		private class hamming_order implements Comparator<Node> {
			public int compare(Node x, Node y) {
				return (x.hamming+x.moves) - (y.hamming+y.moves);
			}
		}
		private class manhattan_order implements Comparator<Node> {
            public int compare(Node x, Node y) {
                return (x.manhattan+x.moves) - (y.manhattan+y.moves);
            }
        }
	}

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial)

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
		return solvable;
	}

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
		if (isSolvable())
			return goal.moves;
		else
			return -1;
	}

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution()

    // test client (see below) 
    public static void main(String[] args) {
		// create initial board from file
		In in = new In(args[0]);
		int n = in.readInt();
		int[][] tiles = new int[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				tiles[i][j] = in.readInt();
		Board initial = new Board(tiles);
	
		// solve the puzzle
		Solver solver = new Solver(initial);

		// print solution to standard output
		if (!solver.isSolvable())
			StdOut.println("No solution possible");
		else {
			StdOut.println("Minimum number of moves = " + solver.moves());
			for (Board board : solver.solution())
				StdOut.println(board);
		}
	}

}
