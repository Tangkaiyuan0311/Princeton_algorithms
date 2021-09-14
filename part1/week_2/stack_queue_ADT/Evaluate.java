import java.util.Scanner;
public class Evaluate {
	public static void main(String[] args) {
		Stack<String> ops = new Stack<String>();
		Stack<Double> vals = new Stack<Double>();
		Scanner in = new Scanner(System.in);
		System.out.println("give your expression, end with Crtl-d");
		while (in.hasNext()) {
			String s = in.next();
			if      (s.equals("("))			      ;
			else if (s.equals("+"))	   ops.push(s);
			else if (s.equals("-"))    ops.push(s);
			else if (s.equals("*"))    ops.push(s);
			else if (s.equals("/"))    ops.push(s);
			else if (s.equals("sqrt")) ops.push(s);
			else if (s.equals(")")) {
				// pop, evaluate, push result
				String op = ops.pop();
				double v = vals.pop();
				if      (op.equals("+"))	v = vals.pop() + v;
				else if (op.equals("-"))    v = vals.pop() - v;
				else if (op.equals("*"))    v = vals.pop() * v;
				else if (op.equals("/"))    v = vals.pop() / v;
				else if (op.equals("sqrt")) v = Math.sqrt(v);
				vals.push(v);
			}
			else
				vals.push(Double.parseDouble(s));
		}
		System.out.println(vals.pop());
	}
}

