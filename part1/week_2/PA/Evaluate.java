import java.util.Scanner;
public class Evaluate {
	public static void main(String[] args) {
		Deque<String> ops = new Deque<String>();
		Deque<Double> vals = new Deque<Double>();
		Scanner in = new Scanner(System.in);
		System.out.println("give your expression, end with Crtl-d");
		while (in.hasNext()) {
			String s = in.next();
			if      (s.equals("("))			      ;
			else if (s.equals("+"))	   ops.addFirst(s);
			else if (s.equals("-"))    ops.addFirst(s);
			else if (s.equals("*"))    ops.addFirst(s);
			else if (s.equals("/"))    ops.addFirst(s);
			else if (s.equals("sqrt")) ops.addFirst(s);
			else if (s.equals(")")) {
				// pop, evaluate, push result
				String op = ops.removeFirst();
				double v = vals.removeLast();
				if      (op.equals("+"))	v = vals.removeLast() + v;
				else if (op.equals("-"))    v = vals.removeLast() - v;
				else if (op.equals("*"))    v = vals.removeLast() * v;
				else if (op.equals("/"))    v = vals.removeLast() / v;
				else if (op.equals("sqrt")) v = Math.sqrt(v);
				vals.addLast(v);
			}
			else
				vals.addLast(Double.parseDouble(s));
		}
		System.out.println(vals.removeLast());
	}
}

