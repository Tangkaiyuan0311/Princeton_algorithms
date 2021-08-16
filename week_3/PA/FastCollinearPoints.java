import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {

	private Point[] input;
	private int numSegs;
	private Bag<LineSegment> segBag;
	private final int N; // number of points

	public FastCollinearPoints(Point[] points) {    // finds all line segments containing 4 or more points
		if (points == null)
            throw new IllegalArgumentException("null argument");
		// copy to input
		N = points.length;
		input = new Point[N];
		for (int i = 0; i < N; i++) {
			if (points[i] != null)
				input[i] = points[i];
			else
				throw new IllegalArgumentException("null argument");
		}
		// sort input in natural order, check for exceptions
		Arrays.sort(input);
        for (int i = 1; i < input.length; i++) {
            if (input[i].equals(input[i-1]))
                throw new IllegalArgumentException("reapeted array elements");
        }
		numSegs = 0;
		segBag = new Bag<LineSegment>();
		if (N >= 4) {
		var help = new Point[N-1];
        var equal = new boolean[N-2];
        findSegs(help, equal);
		}
	}
	public int numberOfSegments() {       // the number of line segments
		return numSegs;
	}

	
	private void copy(Point[] src, Point[] dst, int start, int end, int dst_start) {
		// assumen dst is large enough
		if (start > end)
			return;
		int j = dst_start;
		for (int i = start; i <= end; i++) {
			dst[j] = src[i];
			j++;
		}
	}

	// find all segments from sorted input and store segments in segBag, with the help of a utility array help and equal
	private void findSegs(Point[] help, boolean[] equal) {
		// examine each input[i]
		for (int i = 0; i < N; i++) {
			
			// set up
			Point p = input[i];
			Comparator<Point> comp = p.slopeOrder();
			// get remaining points other than p, help must be in natural order
			copy(input, help, 0, i-1, 0); // copy input[0~i-1] to help
			copy(input, help, i+1, N-1, i); // copy input[i+1] to help


			/*
			swap(input, i, 0); // place input[i] to the first head
			copy(input, help, 1, N-1); // get remaining points other than p, help is in natural order
			swap(input, i, 0); // restore input array
			*/
			
			// sort help w.r.t slopeTo
			Arrays.sort(help, comp); // stable sort
			for (int j = 0; j < help.length-1; j++)
				equal[j] = (p.slopeTo(help[j]) == p.slopeTo(help[j+1]));
			// System.out.println(Arrays.toString(equal));
			
			// precess equal array to locate consecutive trues
			int cnt = 0; // count of consecutive trues
			int start = -1, end = -1; // deliminater of consecutive trues;
			boolean state = false; // true: scanning thues	false: scanning falses

			for (int k = 0; k < equal.length; k++) {
				if (state) {
					if (equal[k] == true) {
						cnt++;
						if (k == equal.length-1) { // last element is true
							end = k;
							assert ((end-start+1) == cnt);
							if (cnt >= 2) { // help[start] ~ help[end+1] along with p is in the same max segment
								if (p.compareTo(help[start]) < 0) { // p is the min point, ok
									// System.out.println("find consecutive true: (" + start + ", " + end + ")");
									segBag.add(new LineSegment(p, help[end+1]));
									numSegs++;
								}
							}
						}
					}
					else {
						end = k-1; // last true
						assert ((end-start+1) == cnt);
						if (cnt >= 2) { // help[start] ~ help[end+1] along with p is in the same max segment
							if (p.compareTo(help[start]) < 0) { // p is the min point, ok
								// System.out.println("find consecutive true: (" + start + ", " + end + ")");
                                segBag.add(new LineSegment(p, help[end+1]));
								numSegs++;
							}
						}
						state = false;
						start = -1;
						end = -1;
						cnt = 0;
					}
				}
				else {
					if (equal[k] == false)
						continue;
					else {
						start = k;
						cnt++; // cnt = 1 afterwards
						assert (cnt == 1);
						state = true;
					}
				}
			}
		}
	}

	public LineSegment[] segments() {               // the line segments
		var ret = new LineSegment[numSegs];
		int i = 0;
		for (var line : segBag) {
			ret[i] = line;
			i++;
		}
		return ret;
	}
	public static void main(String[] args) {

		// read the n points from a file
		In in = new In(args[0]);
		int n = in.readInt();
		Point[] points = new Point[n];
		for (int i = 0; i < n; i++) {
			int x = in.readInt();
			int y = in.readInt();
			points[i] = new Point(x, y);
		}

		// draw the points
		StdDraw.enableDoubleBuffering();
		StdDraw.setXscale(0, 32768);
		StdDraw.setYscale(0, 32768);
		for (Point p : points) {
			p.draw();
		}
		StdDraw.show();

		// print and draw the line segments
		FastCollinearPoints collinear = new FastCollinearPoints(points);
		// BruteCollinearPoints collinear = new BruteCollinearPoints(points);
		for (LineSegment segment : collinear.segments()) {
			StdOut.println(segment);
			segment.draw();
		}
		StdDraw.show();
	}

}
