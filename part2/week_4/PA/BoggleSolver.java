import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
//import edu.princeton.cs.algs4.TrieSET;
// import edu.princeton.cs.algs4.TrieST;

public class BoggleSolver
{
    private final Trie dict;
    //private String lastPre; 
    // the most recently hasPrefix argument (which is in the dict)

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        dict = new Trie();
        for (String word : dictionary)
            dict.add(word);
    }

    private class Point {
        int row, col; // coordinate
        char c; // if c==q, we means qu
        Point(int row, int col, char c) {
            this.row = row;
            this.col = col;
            this.c = c;
        }
    }
    private class array2D {
        private int row, col; // number of rows and columns
        private Point[][] dices;
        Bag<Point>[][] adjacent;

        array2D(BoggleBoard board) {
            row = board.rows();
            col = board.cols();
            dices = new Point[row][col];
            adjacent = (Bag<Point>[][]) new Bag[row][col];
            for (int i = 0; i < row; i++)
                for (int j = 0; j < col; j++)
                    dices[i][j] = new Point(i, j, board.getLetter(i, j));
        }

        // check for index range
        boolean check(int i, int j) {
            if ((i<row && i >=0)&&(j<col && j >=0))
                return true;
            return false;
        }
        // adjacent point of p in the dices, avoid precomputation
        Iterable<Point> adj(Point p) {
            int i = p.row;
            int j = p.col;
            
            if (adjacent[i][j] != null)
                return adjacent[i][j];
            
            Bag<Point> adj = new Bag<Point>();
            if (check(i+1, j))
                adj.add(dices[i+1][j]);
            if (check(i-1, j))
                adj.add(dices[i-1][j]);
            if (check(i, j+1))
                adj.add(dices[i][j+1]);
            if (check(i, j-1))
                adj.add(dices[i][j-1]);
            if (check(i+1, j+1))
                adj.add(dices[i+1][j+1]);
            if (check(i+1, j-1))
                adj.add(dices[i+1][j-1]);
            if (check(i-1, j+1))
                adj.add(dices[i-1][j+1]);
            if (check(i-1, j-1))
                adj.add(dices[i-1][j-1]);
            adjacent[i][j] = adj;
            return adj;
        }

        // 1D index in row major order
        int flat(Point p) {
            int i = p.row;
            int j = p.col;
            return i * col + j;
        }

    }
    
    // dfs on p
    // pre: String of the path from search root to p
    // backtracking optimization enabled
    // marked: the simple path from root to p, maintained across dfs
    // traverse all simple path from v, no crossing with marked
    /*
    private void dfs(Point p, String pre, boolean[] marked, Trie words, array2D array) {
        if (lastPre != null && pre.substring(0, pre.length()-1).equals(lastPre))
            if (!dict.hasPrefix(pre, true))
                return;
        else
            if (!dict.hasPrefix(pre, false))
                return;
        lastPre = pre;
        // last is set in the trie corresponding to pre
        if (pre.length() > 2 && dict.contains(pre, true) && !words.contains(pre, false))
            words.add(pre);
        for (Point x : array.adj(p)) {
            if (!marked[array.flat(x)]) {
                marked[array.flat(x)] = true;
                if (x.c == 'Q')
                    dfs(x, pre+x.c+'U', marked, words, array);
                else
                    dfs(x, pre+x.c, marked, words, array);
                marked[array.flat(x)] = false;
            }
        }
    }
    */
    // dfs on p
    // pre: String of the path from search root to p
    // backtracking optimization enabled
    // marked: the simple path from root to p, maintained across dfs
    // traverse all simple path from v, no crossing with marked
    private void dfs(Point p, String pre, boolean[] marked, Trie words, array2D array) {
        if (!dict.hasPrefix(pre, false))
            return;
        // last is set by trie
        // we can enable history for contain
        if (pre.length() > 2 && dict.contains(pre, true) && !words.contains(pre, false))
            words.add(pre);
        for (Point x : array.adj(p)) {
            if (!marked[array.flat(x)]) {
                marked[array.flat(x)] = true;
                if (x.c == 'Q')
                    dfs(x, pre+x.c+'U', marked, words, array);
                else
                    dfs(x, pre+x.c, marked, words, array);
                marked[array.flat(x)] = false;
            }
        }
    }
    

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Trie words = new Trie();
        array2D array = new array2D(board);
        int row = array.row;
        int col = array.col;
        boolean[] marked;
        for(int i = 0; i < row; i++)
            for (int j = 0; j < col; j++) {
                marked = new boolean[row*col];
                Point root = array.dices[i][j];
                marked[array.flat(root)] = true;
                if (root.c == 'Q')
                    dfs(root, ""+root.c+'U', marked, words, array);
                else
                    dfs(root, ""+root.c, marked, words, array);
            }
        return words;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        int score = 0;
        if (dict.contains(word, false)) {
            int len = word.length();
            if (len<=4 && len >=3)
                score = 1;
            else if (len == 5)
                score = 2;
            else if (len == 6)
                score = 3;
            else if (len == 7)
                score = 5;
            else if (len >= 8)
                score = 11;  
        }
        return score;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        
        StdOut.println("Score = " + score);
        
        /*
        BoggleBoard[] boards = new BoggleBoard[1000];
        for (int i = 0; i < 999; i++)
            boards[i] = new BoggleBoard(10, 10);
        
        long start = System.currentTimeMillis();
        
        for (int i = 0; i < 999; i++)
            solver.getAllValidWords(boards[i]);
        long end = System.currentTimeMillis();
        double time = (end-start)/1000.0;
        
        StdOut.println("Complete in " + time + " seconds");
        */
        
        
    }
    
}

