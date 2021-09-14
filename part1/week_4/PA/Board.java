import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;


public class Board {

	private final int[][] tiles; // tiles
	private final int N; // dimension

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
		N = tiles.length;
		this.tiles = new int [N][N];
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				this.tiles[i][j] = tiles[i][j];
	}
                                 
	/*
    // string representation of this board
    public String toString() {
		String ret = String.valueOf(N);
		for (int i = 0; i < N; i++) {
			ret += "\n";
			for(int j = 0; j < N; j++)
				ret += (String.valueOf(tiles[i][j]) + " ");
		}
		return ret;
	}
	*/

	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(N + "\n");
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				s.append(String.format("%2d ", tiles[i][j]));
			}
			s.append("\n");
		}
		return s.toString();
	}

    // board dimension n
    public int dimension() {
		return N;
	}

    // number of tiles out of place
    public int hamming() {
		int cnt = 0; // count of tiles out of place
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++) {
				if (tiles[i][j] != (i*N+j+1))
					cnt++;
			}
		return (cnt-1);
	}

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
		int x;
		int dis = 0;
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++) {
				x = tiles[i][j];
				if (x != 0)
					dis = dis + Math.abs(i - ((x-1)/N)) + Math.abs(j - ((x-1)%N));
			}
		return dis;
	}
	
    // is this board the goal board?
    public boolean isGoal() {
		return (hamming() == 0);
	}

    // does this board equal y?
    public boolean equals(Object y) {
		if (this == y)
			return true;
		if (y == null)
			return false;
		if (this.getClass() != y.getClass())
			return false;
		Board that = (Board)y;
		if (this.N != that.N)
			return false;
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				if (this.tiles[i][j] != that.tiles[i][j])
					return false;
		return true;
	}

	private void exch(int[][] a, int i1, int j1, int i2, int j2) {
		int tmp = a[i1][j1];
		a[i1][j1] = a[i2][j2];
		a[i2][j2] = tmp;
	}
    
	// all neighboring boards
    public Iterable<Board> neighbors() {
		var q = new Queue<Board>();
		var a = new int[N][N];
		int I = -1;
		int J = -1; // pos of blank tile

		for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
				a[i][j] = tiles[i][j];
				if (a[i][j] == 0) {
					I = i;
					J = j;
				}
			}
		// System.out.println("I = " + I + ", J = " + J);
		// exchange blank with left
		if (J > 0) {
			exch(a, I, J, I, J-1);
			q.enqueue(new Board(a));
			exch(a, I, J, I, J-1);
		}
		// exchange blank with right
		if (J < N-1) {
			exch(a, I, J, I, J+1);
            q.enqueue(new Board(a));
            exch(a, I, J, I, J+1);
		}
		// exchange blank with up
        if (I > 0) {
            exch(a, I, J, I-1, J);
            q.enqueue(new Board(a));
            exch(a, I, J, I-1, J);
        }
		// exchange blank with down
        if (I < N-1) {
            exch(a, I, J, I+1, J);
            q.enqueue(new Board(a));
            exch(a, I, J, I+1, J);
        }
		return q;
	}


    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
		var a = new int[N][N];
		int I = -1;
		int J = -1;
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++) {
				a[i][j] = tiles[i][j];
				if (tiles[i][j] == 0) {
					I = i;
					J = j;
				}
			}
		if (J == 0)
			exch(a, 0, 1, 1, 1);
		else
			exch(a, 0, 0, 1, 0);
		return new Board(a);

	}

	

    // unit testing (not graded)
    public static void main(String[] args) {
		// create initial board from file
		In in = new In(args[0]);
		int n = in.readInt();
		int[][] tiles = new int[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				tiles[i][j] = in.readInt();
		Board bd = new Board(tiles);
		System.out.println(bd);
		for (var b : bd.neighbors())
			System.out.println(b);
		System.out.println(bd.twin());
		/*
		System.out.println(bd);
		System.out.println("hamming = " + bd.hamming());
		System.out.println("manhattan = " + bd.manhattan());
		*/
		In in1 = new In(args[1]);
        n = in1.readInt();
        tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in1.readInt();
        Board bd1 = new Board(tiles);
        assert (bd.equals(bd1));
		// assert (!(bd.isGoal()));
		// assert (bd1.isGoal());
		System.out.println("success");
	}

}
