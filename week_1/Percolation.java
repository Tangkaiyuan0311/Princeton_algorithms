// import edu.princeton.cs.algs4.StdRandom;
// import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;



public class Percolation {

    //private int[] sites; // indexed by site number, indicate open or blocked
	// byte aByte = (byte)0b00100001;
	private byte[] status; // status(top, bottom, open) associated with each site(keep track of its connected-component) 
    private final WeightedQuickUnionUF uf; // disjoint-set forest representation
    private final int grid_size; // site grid size
    private int open_sites; // number of open sites
	private boolean percolates; // system percolation status

    // compute the index of given site coordinate, (row, col)->[0, n*n-1]
    private static int coord2index(int row, int col, int grid_size) {	
		if ((row < 1 || row > grid_size) || (col < 1 || col > grid_size)) {
			throw new IllegalArgumentException("coordinate is not between 1 and " + grid_size);
		}
		int index = (row - 1) * grid_size + col - 1;
		return index;
    }
 
    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException("argument is invalid");
		}
		int N = n * n; // number of concrete sites
		// N += 2; // add 2 virtual sites, represent input and output
	
		// initialize instance fields
		percolates = false;
		open_sites = 0;
		grid_size = n;
		status = new byte[N];
		uf = new WeightedQuickUnionUF(N);
		for (int i = 0; i < N; i++) {
			status[i] = 0b000; // considering corner case: 1*1 or 2*2
			if ((i <= grid_size-1) && (i >= 0)) // top site
				status[i] |= 0b100; // should not use assignment
			if ((i <= grid_size*grid_size-1) & (i >= grid_size*grid_size-grid_size)) //  bottom site
				status[i] |= 0b010;
				// inner site
			// status[i] |= 0b000;
		}
    }

    // return the up site index, -1 if not exit
    private int Up(int row, int col) {
		if (row > 1) {
			return coord2index(row-1, col, grid_size);
		}
		else // site from first row has no up site
			return -1;
		}

		// return the left site index, -1 if not exit
		private int Left(int row, int col) {
		if (col > 1) {
			return coord2index(row, col-1, grid_size);
		}
		else // site from first col has no left site
			return -1;
		}

	// return the right site index, -1 if not exit
	private int Right(int row, int col) {
		if (col < grid_size) {
			return coord2index(row, col+1, grid_size);
		}
		else // site from last col has not right site
			return -1;
		}

    // return the down site index, -1 if not exit
    private int Down(int row, int col) {
		if (row < grid_size) {
			return coord2index(row+1, col, grid_size);
		}
		else // site from bottom row has no down site
			return -1;
		}

    

    // opens the site (row, col) if it is not open already
    // perform the union and the instance updation 
    public void open(int row, int col) {
		if (isOpen(row, col)) // already open
			return;
		else { // union if necessary
			// check for union condition
			// consider 5 case
			open_sites++;
			int id = coord2index(row, col, grid_size);
			status[id] |= 0b001; // set open bit
			int up, left, right, down; // adjacent site index
			up = Up(row, col);
			left = Left(row, col);
			right = Right(row, col);
			down = Down(row, col);
			// byte status_id = status[uf.find(id)];
			byte status_id;
			// union adjacent open sites
			if ((up != -1) && ((status[up] & 0b001) == 1)) {
				byte status_up = status[uf.find(up)];
				status_id = status[uf.find(id)];
				uf.union(id, up);
				status[uf.find(id)] = (byte)(status_id | status_up);
			}
			if ((left != -1) && ((status[left] & 0b001) == 1)) {
				byte status_left = status[uf.find(left)];
				status_id = status[uf.find(id)];
                uf.union(id, left);
                status[uf.find(id)] = (byte)(status_id | status_left);
			}
			if ((right != -1) && ((status[right] & 0b001) == 1)) {
				byte status_right = status[uf.find(right)];
				status_id = status[uf.find(id)];
                uf.union(id, right);
                status[uf.find(id)] = (byte)(status_id | status_right);
			}
			if ((down != -1) && ((status[down] & 0b001) == 1)) {
				byte status_down = status[uf.find(down)];
				status_id = status[uf.find(id)];
                uf.union(id, down);
                status[uf.find(id)] = (byte)(status_id | status_down);
			}
			if ((status[uf.find(id)] & 0b111) == 0b111) // check for top and bottom bits
				percolates = true;
		}
	}
	    

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
		int index = coord2index(row, col, grid_size);
			return ((status[index] & 0b001) == 0b001); // check open bit
    }
			

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
		int index = coord2index(row, col, grid_size);
			return ((status[uf.find(index)] & 0b101) == 0b101); 
			// check top bit and open bit of its root, notice that open bit must be checked
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
		return open_sites;
    }

    // does the system percolate?
    public boolean percolates() {
		return percolates;
    }

    // test client (optional)
    public static void main(String[] args) {
		var peco = new Percolation(4);
		peco.open(3, 1);
		peco.open(3, 2);
		peco.open(2, 2);
		peco.open(2, 3);
		peco.open(2, 4);
		peco.open(3, 4);
		peco.open(4, 4);
		peco.open(1, 1);
		peco.open(2, 1);
		if (peco.percolates()) {
			System.out.println("percolates!"); 
			return;
		}
		System.out.println("do not percolate");
    }
}
