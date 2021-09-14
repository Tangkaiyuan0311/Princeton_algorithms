import edu.princeton.cs.algs4.StdIn;


// test client of symbol table
public class FrequencyCounter {
	public void main(String[] args) {
		int minlen = Interger.parseInt(args[0]); // key length cutoff
		ST<String, Interger> st = new ST<String, Interger>;
		// build symbol table
		while (!StdIn.isEmpty()) {
			String word = StdIn.readString();
			if (world.length < minlen)
				continue;
			if (!st.contains(word))
				st.put(word, 1);
			else
				st.put(word, st.get(word)+1)
		}

		//find max
		String max;
		int maxCnt;
		for (String key : st.keys()) {
			if (st.get(key) > maxCnt) {
				max = key;
				maxCnt = st.get(key);
			}
		}

		System.out.println(max + ": " + maxCnt);


	}
}
