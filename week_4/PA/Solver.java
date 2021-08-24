import java.util.Comparator;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;

public class Solver {

	private MinPQ<Node> pq;
	// private int moves;
	private Node goal; // termination node
	private boolean solvable;
	
	// search node data type
	private class Node {
		// public final hamming_order hamming_cmp = new hamming_order();
		// public final manhattan_order manhattan_cmp = new manhattan_order();
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

	private int process(MinPQ<Node> pq) {
		Node current_node = pq.delMin();
		if (current_node.board.isGoal()) {
			goal = current_node;
			return 1;
		}
		else
			for (var neighbor : current_node.board.neighbors()) {
				if ((current_node.prev != null) && (neighbor.equals(current_node.prev.board)))
					continue;
				else
					pq.insert(new Node(neighbor, current_node.moves+1, current_node));
			}
		return 0;
	}
	
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
		goal = null;
		solvable = false;
		var pq = new MinPQ<Node>(new hamming_order());
		var pq_twin = new MinPQ<Node>(new hamming_order());
		pq.insert(new Node(initial, 0, null));
		pq_twin.insert(new Node(initial, 0, null));

		int flag;
		while (true) {
			if (!pq.isEmpty()) {
				flag = process(pq);
				if (flag == 1) {
					solvable = true;
					break;
				}
			}
			if (!pq_twin.isEmpty()) {
                flag = process(pq_twin);
                if (flag == 1) {
                    solvable = false;
                    break;
                }
            }
		}
	}

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
    public Iterable<Board> solution() {
		if (solvable) {
			var s = new Stack<Board>();
			Node x = goal;
			while (x != null) {
				s.push(x.board);
				x = x.prev;
			}
			return s;
		}
		else
			return null;
	}

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
