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
	private boolean rootedDAG;
	private int root;
	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) {
		if (synsets == null || hypernyms == null)
			throw new IllegalArgumentException("null constructor argument");
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
		
		rootedDAG = true;
		root = -1;
		rootedDAG(G);
		if (!rootedDAG)
			throw new IllegalArgumentException("not a rooted DAG");

		sap = new SAP(G);
		/*
		System.out.println("construct a graph with " + G.V() + " vertices");
		for (int i : noun2ids.get("word"))
			System.out.println(i);
		*/
	}

	private void rootedDAG(Digraph G) {
		int v;
		boolean[] onStack = new boolean[G.V()];
		boolean[] marked = new boolean[G.V()];
		for (v = 0; v < G.V(); v++) {
			if (!rootedDAG)
				return;
			if (!marked[v])
				dfs(G, v, marked, onStack);
		}

	}

	// dfs from v, detect and set rootedDAG from v, if find !rootedDAG, return accordingly
	private void dfs(Digraph G, int v, boolean[] marked, boolean[] onStack) {
		marked[v] = true;
		onStack[v] = true;
		
		if (!(G.adj(v).iterator().hasNext())) {
			// v must be the root if G is rooted
			if (root == -1)
				root = v;
			else
				if (root != v) {
					// System.out.println("not single rooted");
					rootedDAG = false;
				}
		}
		for (int w : G.adj(v)) {
			if (!rootedDAG) {
				onStack[v] = false;
				return;
			}
			else if (!marked[w])
				dfs(G, w, marked, onStack);
			else if (onStack[w]) { // cycle detected
				// System.out.println("cycle detected");
				rootedDAG = false;
			}
		}
		onStack[v] = false;
	}

	// returns all WordNet nouns
	public Iterable<String> nouns() {
		return noun2ids.keys();
	}

	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		if (word == null)
			new IllegalArgumentException("null argument");
		return noun2ids.get(word) != null;
	}


	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
		if (!isNoun(nounA) || !isNoun(nounB))
			throw new IllegalArgumentException("not valid noun argument");
		Bag<Integer> idsA = noun2ids.get(nounA);
		Bag<Integer> idsB = noun2ids.get(nounB);
		return sap.length(idsA, idsB);

	}

	// a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("not valid noun argument");
		Bag<Integer> idsA = noun2ids.get(nounA);
        Bag<Integer> idsB = noun2ids.get(nounB);
		return id2syn.get(sap.ancestor(idsA, idsB));
	}


	// do unit testing of this class
	public static void main(String[] args) {
		var net = new WordNet("synsets.txt", "hypernyms.txt");
		// var net2 = new WordNet("synsets6.txt", "hypernyms6InvalidCycle.txt");
		// var net3 = new WordNet("synsets6.txt", "hypernyms6InvalidTwoRoots.txt");
		
		while (!StdIn.isEmpty()) {
			String v = StdIn.readString();
			String w = StdIn.readString();
			int dis = net.distance(v, w);
			String a = net.sap(v, w);
			StdOut.printf("distance = %d, ancestor = %s\n", dis, a);
		}
		

	}
}




















