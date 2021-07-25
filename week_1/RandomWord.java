/**
 * reads a sequence of words from standard input and prints one of those words
 * using Knuth's method
 */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
	public static void main(String[] args) {
		String currentWord = null;
		String champ = null;
		double p;
		int wordCnt = 0;
		while (!StdIn.isEmpty()) {
			currentWord = StdIn.readString();
			// StdOut.println("read in " + currentWord);
			wordCnt++;
			p = 1.0/wordCnt;
			// StdOut.println("select this word with probability: " + p);
			if (StdRandom.bernoulli(p))
				champ = currentWord;
		}
		// StdOut.println("\nread in " + wordCnt + " words...");
		// StdOut.println("champion is: " + champ);
		StdOut.println(champ);
	}
}


