import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;



public class Percolation {

    private int[] sites; // indexed by site number, indicate open or blocked
    private WeightedQuickUnionUF uf = null; // disjoint-set forest representation
    private int grid_size; // site grid size
    private int open_sites; // number of open sites

    // compute the index of given site coordinate, considering 2 virtual site and row major storage 
    private static int coord2index(int row, int col, int grid_size) {	
	if ((row < 1 || row > grid_size) || (col < 1 || col > grid_size)) {
	    throw new IllegalArgumentException("coordinate is not between 0 and " + grid_size);
	}
	int index = (row - 1) * grid_size + col;
	return index;
    }
 
    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
	if (n <= 0) {
	    throw new IllegalArgumentException("argument is invalid");
	}
	int N = n * n; // number of concrete sites
	N += 2; // add 2 virtual sites, represent input and output
	
	// initialize instance fields
	open_sites = 0;
	grid_size = n;
	sites = new int[N];
	uf = new WeightedQuickUnionUF(N);
	for (int i = 0; i < N; i++) {
	    sites[i] = 0;
	}
	//set virtual sites
	sites[0] = 1;
	sites[N-1] = 1;
    }

    // return the up site index, -1 if not exit
    private int hasUp(int row, int col) {
	if (row > 1) {
	    return coord2index(row-1, col, grid_size);
	}
	else // site from first row has virtual top as its up site
	    return 0;
    }

     // return the left site index, -1 if not exit
    private int hasLeft(int row, int col) {
	if (col > 1) {
	    return coord2index(row, col-1, grid_size);
	}
	else // site from first col has no left site
	    return -1;
    }

     // return the right site index, -1 if not exit
    private int hasRight(int row, int col) {
	if (col < grid_size) {
	    return coord2index(row, col+1, grid_size);
	}
	else // site from last col has not right site
	    return -1;
    }

     // return the down site index, -1 if not exit
    private int hasDown(int row, int col) {
	if (row < grid_size) {
	    return coord2index(row+1, col, grid_size);
	}
	else // site from last row has virtual top as its up site
	    return (sites.length-1);
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
	    sites[id] = 1;
	    int up, left, right, down;
	    // union adjacent open sites
	    if (((up = hasUp(row, col)) != -1) && (sites[up] == 1)) {
		uf.union(id, up);
	    }
	    if (((left = hasLeft(row, col)) != -1) && (sites[left] == 1)) {
		uf.union(id, left);
	    }
	    if (((right = hasRight(row, col)) != -1) && (sites[right] == 1)) {
		uf.union(id, right);
	    }
	    if (((down = hasDown(row, col)) != -1) && (sites[down] == 1)) {
		uf.union(id, down);
	    }
	    /* 
	    if (row == grid_size) {
		uf.union(id, sites.length-1);
	    }
	    */
	}
    }
	    

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
	int index = coord2index(row, col, grid_size);
	return (sites[index] == 1);
    }
			

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
	int index = coord2index(row, col, grid_size);
	//return uf.connected(0, index);
	return uf.find(0) == uf.find(index);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
	return open_sites;
    }

    // does the system percolate?
    public boolean percolates() {
	//return uf.connected(0, sites.length-1);
	return uf.find(0) == uf.find(sites.length-1);
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
	if (peco.percolates())
	{    System.out.println("percolates!"); return;}
	System.out.println("do not percolate");
    }
	
	
}
