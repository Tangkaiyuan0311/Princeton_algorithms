import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;



public class KdTree {

	private static boolean Horizontal = false;
	private static boolean Vertical = true;
	private Node root;
	private int N; // number of points

	private class Node {
		Point2D point;
		boolean level; // splitting axis of this node
		Node left, right;
		RectHV rect;
		public Node(Point2D point, boolean level, RectHV rect) {
			this.point = point;
			this.level = level;
			this.rect = rect;
		}
	}

	public KdTree() { // construct an empty set of points
		this.root = null;
		this.N = 0;
	}

	public boolean isEmpty(){  // is the set empty? 
		return N == 0;
	}
	
	public int size(){ // number of points in the set 
		return N;
	}

	public void insert(Point2D p) { // add the point to the set (if it is not already in the set)
		if (p == null)
			throw new IllegalArgumentException("null argument");
		if (root == null)
			root = insert(root, p, Vertical, new RectHV(0, 0, 1, 1));
		else
			root = insert(root, p, root.level, root.rect);
		N++;

	}
	// insert p into tree, assuming p is in associated rect, level should be tree's level, rect should be tree's rect
	private Node insert(Node tree, Point2D p, boolean level, RectHV rect) {
		if (tree == null)
			return new Node(p, level, rect);
		assert (tree.rect.contains(p));
		if (p.compareTo(tree.point) == 0)
			return tree; // do nothing
		double xmin, xmax, ymin, ymax;
		if (((tree.level == Horizontal) && (p.y() > tree.point.y())) || ((tree.level == Vertical) && (p.x() > tree.point.x()))) {
			if (tree.right == null) {
				if (tree.level == Horizontal) {
					xmin = tree.rect.xmin();
					ymin = tree.point.y();
					xmax = tree.rect.xmax();
					ymax = tree.rect.ymax();
				}
				else {
					xmin = tree.point.x();
					ymin = tree.rect.ymin();
					xmax = tree.rect.xmax();
					ymax = tree.rect.ymax();
				}
				tree.right = insert(tree.right, p, !tree.level, new RectHV(xmin, ymin, xmax, ymax));
			}
			else
				tree.right = insert(tree.right, p, tree.right.level, tree.right.rect);
			return tree;
		}
		else {
			if (tree.left == null) {
				if (tree.level == Horizontal) {
					xmin = tree.rect.xmin();
					ymin = tree.rect.ymin();
					xmax = tree.rect.xmax();
					ymax = tree.point.y();
				}
				else {
					xmin = tree.rect.xmin();
					ymin = tree.rect.ymin();
					xmax = tree.point.x();
					ymax = tree.rect.ymax();
				}
				tree.left = insert(tree.left, p, !tree.level, new RectHV(xmin, ymin, xmax, ymax));
			}
			else // tree.left is not null
				tree.left = insert(tree.left, p, tree.left.level, tree.left.rect);
			return tree;
		}
	}

	public void draw(){ // draw all points to standard draw
        StdDraw.setPenRadius(0.01);
		draw(root);
    }

	// recursive draw the kd tree
	private void draw(Node tree) {
		if (tree == null)
			return;
		draw(tree.left);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.point(tree.point.x(), tree.point.y());
		if (tree.level == Horizontal) {
			StdDraw.setPenColor(StdDraw.BLUE);
			StdDraw.line(tree.rect.xmin(), tree.point.y(), tree.rect.xmax(), tree.point.y());
		}
		else {
			StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(tree.point.x(), tree.rect.ymin(), tree.point.x(), tree.rect.ymax());
		}
		draw(tree.right);
	}
	
	public boolean contains(Point2D p){ // does the set contain point p?
		if (p == null)
            throw new IllegalArgumentException("null argument");
		return contains(root, p);
	}
	private boolean contains(Node tree, Point2D p) {
		if (tree == null)
			return false;
		if (!tree.rect.contains(p))
			return false;
		if (tree.point.equals(p))
			return true;
		else if (((tree.level == Horizontal) && (p.y() > tree.point.y())) || ((tree.level == Vertical) && (p.x() > tree.point.x())))
			return contains(tree.right, p);
		else
			return contains(tree.left, p);
	}

	
	public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle (or on the boundary) 
		if (rect == null)
            throw new IllegalArgumentException("null argument");
		var s = new Stack<Point2D>();
		range(root, rect, s);
		return s;
	}
	private void range(Node tree, RectHV rect, Stack<Point2D> s) {
		if (tree == null)
			return;
		if (!tree.rect.intersects(rect))
			return;
		if (rect.contains(tree.point))
			s.push(tree.point);
		if ((tree.left != null) && (tree.left.rect.intersects(rect)))
			range(tree.left, rect, s);
		if ((tree.right != null) && (tree.right.rect.intersects(rect)))
			range(tree.right, rect, s);
	}
	
	public Point2D nearest(Point2D p) { // a nearest neighbor in the set to point p; null if the set is empty 
		if (p == null)
            throw new IllegalArgumentException("null argument");
		if (isEmpty())
			return null;
		double best_dst = Double.POSITIVE_INFINITY;
		return nearest(root, p, best_dst);
	}

	// return best point in tree w.r.t best_dst, return null if no better one
	private Point2D nearest(Node tree, Point2D p, double best_dst) {
		if (tree == null)
			return null;
		if (tree.rect.distanceTo(p) > best_dst)
			return null;
		// potential best in tree
		Point2D best_p = null;
		Point2D better_p = null;
		if (tree.point.distanceTo(p) < best_dst) {
			best_p = tree.point;
			best_dst = tree.point.distanceTo(p);
		}

		if (((tree.level == Horizontal) && (p.y() > tree.point.y())) || ((tree.level == Vertical) && (p.x() > tree.point.x()))) {
			better_p = nearest(tree.right, p, best_dst);
			if (better_p != null) {
				best_p = better_p;
				best_dst = better_p.distanceTo(p);
			}
			better_p = nearest(tree.left, p, best_dst);
			if (better_p != null)
				best_p = better_p;
			return best_p;
		}
		else {
			better_p = nearest(tree.left, p, best_dst);
            if (better_p != null) {
                best_p = better_p;
                best_dst = better_p.distanceTo(p);
            }
            better_p = nearest(tree.right, p, best_dst);
            if (better_p != null)
                best_p = better_p;
            return best_p;
		}
	}

	public static void main(String[] args) { // unit testing of the methods (optional) 
		var s = new KdTree();
		s.insert(new Point2D(0.2, 0.2));
		s.insert(new Point2D(0.4, 0.4));
		s.insert(new Point2D(0.6, 0.6));
		s.insert(new Point2D(0.7, 0.7));
		s.insert(new Point2D(0.8, 0.8));
		s.draw();
		assert (s.nearest(new Point2D(0.9, 0.9)).equals(new Point2D(0.8, 0.8)));
		var range = s.range(new RectHV(0, 0, 0.5, 0.5));
		System.out.println();
		for (var p : range)
			System.out.print(p + " ");

	}

}
