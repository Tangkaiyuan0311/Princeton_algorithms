import edu.princeton.cs.algs4.Quick;

public class CircularSuffixArray{
    private class Suffix implements Comparable<Suffix>{
        private final char[] original;
        private final int offset;
        Suffix(char[] original, int offset) {
            this.original = original;
            this.offset = offset;
        }
        public int compareTo(Suffix this, Suffix that) {
            int i = this.offset;
            int j = that.offset;
            for (int p = 0; p < original.length; p++) {
                if(original[i] > original[j])
                    return 1;
                else if (original[i] < original[j])
                    return -1;
                else {
                    i = (i+1) % original.length;
                    j = (j+1) % original.length;
                }
            }
            return 0;
        }

    }
    private final int length;
    private final Suffix[] suffixes;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null)
            throw new IllegalArgumentException("Null argument");
        char[] a = s.toCharArray();
        length = a.length;
        suffixes = new Suffix[length];
        for (int i = 0; i < length; i++)
            suffixes[i] = new Suffix(a, i);
        Quick.sort(suffixes); 

    }

    // length of s
    public int length() {
        return length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i > length-1)
            throw new IllegalArgumentException("Out of range");
        return suffixes[i].offset;
    }

    // unit testing (required)
    public static void main(String[] args) {
        String s = "ABRACADABRA!";
        var sa = new CircularSuffixArray(s);
        for (int i = 0; i < s.length(); i++) {
            System.out.println(sa.index(i));
        }
    }

}
