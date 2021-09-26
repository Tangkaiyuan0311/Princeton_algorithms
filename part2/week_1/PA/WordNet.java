import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
public class WordNet {

	private final Digraph G;
	private ST<Integer, String> id2syn;
	private ST<String, Bag<Integer>> noun2ids;
	private final SAP sap;
	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) {
		// open corresponding file
		In in_syn = new In(synsets);
		In in_hyp = new In(hypernyms); 
		id2syn = new ST<Integer, String>(); // id->synset
		noun2ids = new ST<String, Bag<Integer>>(); // noun->ids

		// associate string with integer(vertex)
		String[] entries;
		String[] words;
		Bag<Integer> bag;
		while (in_syn.hasNextLine()) {
			entries = in_syn.readLine().split(",");
			words = entries[1].split(" ");
			for (int i = 0; i < words.length; i++) {
				if ((bag = noun2ids.get(words[i])) != null)
					bag.add(Integer.parseInt(entries[0]));
				else {
					bag = new Bag<Integer>();
					bag.add(Integer.parseInt(entries[0]));
					noun2ids.put(words[i], bag);
				}
			}
			// require: word->ids, id->synset
			id2syn.put(Integer.parseInt(entries[0]), entries[1]);
		}

		G = new Digraph(id2syn.size());

		// construct digraph from hypernyms
		while (in_hyp.hasNextLine()) {
			entries = in_hyp.readLine().split(",");
			// entries[1~len-1];
			for (int i = 1; i < entries.length; i++)
				G.addEdge(Integer.parseInt(entries[0]), Integer.parseInt(entries[i]));
		}
		sap = new SAP(G);
		/*
		System.out.println("construct a graph with " + G.V() + " vertices");
		for (int i : noun2ids.get("word"))
			System.out.println(i);
		*/
	}

	// returns all WordNet nouns
	public Iterable<String> nouns() {
		return noun2ids.keys();
	}

	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		return noun2ids.get(word) != null;
	}


	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
		Bag<Integer> idsA = noun2ids.get(nounA);
		Bag<Integer> idsB = noun2ids.get(nounB);
		return sap.length(idsA, idsB);

	}

	// a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		Bag<Integer> idsA = noun2ids.get(nounA);
        Bag<Integer> idsB = noun2ids.get(nounB);
		return id2syn.get(sap.ancestor(idsA, idsB));
	}


	// do unit testing of this class
	public static void main(String[] args) {
		var net = new WordNet("synsets.txt", "hypernyms.txt");
		
		
		while (!StdIn.isEmpty()) {
			String v = StdIn.readString();
			String w = StdIn.readString();
			int dis = net.distance(v, w);
			String a = net.sap(v, w);
			StdOut.printf("distance = %d, ancestor = %s\n", dis, a);
		}

	}
}




















