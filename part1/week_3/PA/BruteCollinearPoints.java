import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Bag;
import java.util.Arrays;

public class BruteCollinearPoints {
	private final Point[] in_points;
	private int numSegs;
	private Bag<LineSegment> segBag;

	// finds all line segments containing 4 points
	public BruteCollinearPoints(Point[] points) {
		if (points == null)
			throw new IllegalArgumentException("null argument");
		in_points = new Point[points.length];
		for (int i = 0; i < points.length; i++) {
			if (points[i] != null)
				in_points[i] = points[i];
			else
				throw new IllegalArgumentException("null argument");
		}
		Arrays.sort(in_points);
		for (int i = 1; i < in_points.length; i++) {
			if (in_points[i].equals(in_points[i-1]))
				throw new IllegalArgumentException("invalid array elements");
		}
		numSegs = 0;
		segBag = new Bag<LineSegment>();
		if (points.length >= 4) {
			findSegs();
		}

	}

	private boolean checkLine(Point p, Point q, Point r, Point s) { // asssume: p q r s are in order
		if ((p.slopeTo(q) == p.slopeTo(r)) && (p.slopeTo(r) == p.slopeTo(s)))
			return true;
		else
			return false;
	}

	// the number of line segments
	public int numberOfSegments() {
		return numSegs;
	}

	private void findSegs() {
		int N = in_points.length;
        for (int i = 0; i <= N-4; i++)
            for (int j = i+1; j <= N-3; j++)
                for (int k = j+1; k <= N-2; k++)
                    for (int l = k+1; l <= N-1; l++) {
                        if (checkLine(in_points[i], in_points[j], in_points[k], in_points[l])) {
                            segBag.add(new LineSegment(in_points[i], in_points[l]));
                            numSegs++;
                        }
                    }
	}
	
	// the line segments
	public LineSegment[] segments() {
		
		var lines = new LineSegment[numSegs];
		int i = 0;
		for (var line : segBag)
			lines[i++] = line;
		return lines;	
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
		//FastCollinearPoints collinear = new FastCollinearPoints(points);
		BruteCollinearPoints collinear = new BruteCollinearPoints(points);
		for (LineSegment segment : collinear.segments()) {
			StdOut.println(segment);
			segment.draw();
		}
		StdDraw.show();
	}
}
