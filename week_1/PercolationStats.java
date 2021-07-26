import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import java.util.*;
//import Percolation;

public class PercolationStats {
	private double[] thresholds;
    private Percolation perco = null;
	
	// public boolean isOpen(int row, int col)
	// public int numberOfOpenSites()
    // public boolean percolates()
	// public void open(int row, int col)

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
		thresholds = new double[trials];
		int i;
        for (i = 0; i < trials; i++) {
			perco = new Percolation(n);
			int[] ids = StdRandom.permutation(n*n);
			int id;
			for (int j = 0; !perco.percolates(); j++) {
				//row, col = select a random from a blocked site;
				id = ids[j];
				perco.open(id/n+1, id%n+1);
			}
			thresholds[i] = (double)(perco.numberOfOpenSites())/(n*n);
		}
		System.out.println(Arrays.toString(thresholds));
	}
    
    // sample mean of percolation threshold
    //public double mean()

    // sample standard deviation of percolation threshold
    //public double stddev()

    // low endpoint of 95% confidence interval
    //public double confidenceLo()

    // high endpoint of 95% confidence interval
    //public double confidenceHi()

   // test client (see below)
   public static void main(String[] args) {
	   var foo = new PercolationStats(100, 10);
   }


}
