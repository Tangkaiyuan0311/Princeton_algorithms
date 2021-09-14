import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
// import java.util.*;
// import Percolation;

public class PercolationStats {
	private final double[] thresholds;
    // private Percolation perco = null;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
		if ((n <= 0) || (trials <= 0)) {
			throw new IllegalArgumentException("negative arguments");
		}
		thresholds = new double[trials];
		Percolation perco = null;
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
		// System.out.println(Arrays.toString(thresholds));
	}
    
    // sample mean of percolation threshold
    public double mean() {
		return StdStats.mean(thresholds);
	}

    // sample standard deviation of percolation threshold
    public double stddev() {
		return StdStats.stddev(thresholds);
	}

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
		double lo = mean() - 1.96*stddev()/(Math.sqrt(thresholds.length));
		return lo;
	}


    // high endpoint of 95% confidence interval
    public double confidenceHi() {
		double hi = mean() + 1.96*stddev()/(Math.sqrt(thresholds.length));
        return hi;
	}

   // test client (see below)
   public static void main(String[] args) {
	   var stats = new PercolationStats(Integer. parseInt(args[0]), Integer. parseInt(args[1]));
	   double mean = stats.mean();
	   double stddev = stats.stddev();
	   var interval = new double[2];
	   interval[0] = stats.confidenceLo();
	   interval[1] = stats.confidenceHi();
	   System.out.println("mean                   " + " = " + mean);
	   System.out.println("stddev                 " + " = " + stddev);
	   System.out.println("95% confidence interval" + " = " + "[" + interval[0] + ", " + interval[1] + "]");
   }


}
