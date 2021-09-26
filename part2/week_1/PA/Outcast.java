import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class Outcast {
	private final WordNet net;
	public Outcast(WordNet wordnet) {        // constructor takes a WordNet object
		this.net = wordnet;
	}

	public String outcast(String[] nouns) {  // given an array of WordNet nouns, return an outcast
		int N = nouns.length;
		var distance = new int[N][N];
		for (int i = 0; i < N-1; i++) {
			for (int j = i + 1; j < N; j++)
				distance[i][j] = net.distance(nouns[i], nouns[j]);
		}

		int dis_sum;
		int largest = -1;
		int index = -1;
		for (int i = 0; i < N; i++) {
			dis_sum = 0;
			for (int j = 0; j < i; j++)
				dis_sum += distance[j][i];
			for (int j = i + 1; j < N; j++)
				dis_sum += distance[i][j];
			if (dis_sum > largest) {
				largest = dis_sum;
				index = i;
			}
		}
		return nouns[index];
	}

	public static void main(String[] args) { // see test client below
		WordNet wordnet = new WordNet(args[0], args[1]);
		Outcast outcast = new Outcast(wordnet);
		for (int t = 2; t < args.length; t++) {
			In in = new In(args[t]);
			String[] nouns = in.readAllStrings();
			StdOut.println(args[t] + ": " + outcast.outcast(nouns));
		}
	}
}
