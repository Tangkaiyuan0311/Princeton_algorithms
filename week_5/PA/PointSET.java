import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.TreeSet;
import edu.princeton.cs.algs4.Stack;



public class PointSET {

	private TreeSet<Point2D> bst;

	public PointSET() { // construct an empty set of points
		bst = new TreeSet<Point2D>();
	}

	public boolean isEmpty(){  // is the set empty? 
		return bst.size() == 0;
	}
	
	public int size(){ // number of points in the set 
		return bst.size();
	}

	public void insert(Point2D p) { // add the point to the set (if it is not already in the set)
		if (p == null)
			throw new IllegalArgumentException("null argument");
		bst.add(p); // leave the tree unchanged if p already in the tree
	}
	
	public boolean contains(Point2D p){ // does the set contain point p?
		if (p == null)
            throw new IllegalArgumentException("null argument");
		return bst.contains(p);
	}

	public void draw(){ // draw all points to standard draw 
		for (Point2D p : bst)
			p.draw();
	}

	public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle (or on the boundary) 
		if (rect == null)
            throw new IllegalArgumentException("null argument");
		var st = new Stack<Point2D>();
		for (Point2D p : bst) {
			if (rect.contains(p))
				st.push(p);
		}
		return st;
	}
	
	public Point2D nearest(Point2D p) { // a nearest neighbor in the set to point p; null if the set is empty 
		if (p == null)
            throw new IllegalArgumentException("null argument");
		if (isEmpty())
			return null;
		double best_dst = Double.POSITIVE_INFINITY;
		Point2D best_p = null;
		for (Point2D x : bst) {
			if (x.distanceTo(p) < best_dst) {
				best_p = x;
				best_dst = best_p.distanceTo(p);
			}
		}
		return best_p;
	}

	public static void main(String[] args) { // unit testing of the methods (optional) 
		var s = new PointSET();
		s.insert(new Point2D(0.2, 0.2));
		s.insert(new Point2D(0.4, 0.4));
		s.insert(new Point2D(0.6, 0.6));
		s.insert(new Point2D(0.7, 0.7));
		s.insert(new Point2D(0.8, 0.8));
		s.draw();
		assert (s.nearest(new Point2D(0.9, 0.9)).equals(new Point2D(0.8, 0.8)));
		assert (s.nearest(new Point2D(0.05, 0.05)).equals(new Point2D(0.1, 0.1)));
		var range = s.range(new RectHV(0, 0, 0.5, 0.5));
		System.out.println();
		for (var p : range)
			System.out.print(p + " ");

	}
}
