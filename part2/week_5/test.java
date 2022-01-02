public class test {
    private static class Node {
        Node left;
        Node right;
        int key; // won't be -1
        Node(Node left, Node right, int key) {
            this.left = left;
            this.right = right;
            this.key = key;
        }
    }
    private static int current; // file pointer simulation


    // encode tree x starting from T[i]
    // return index after the ending index
    // x != null
    public static int encode(Node x, int i, int[] T) {
        T[i] = x.key;
        // encode left subtree
        if (x.left == null) {
            T[i+1] = -1;
            i += 2;
        }
        else
            i = encode(x.left, i+1, T);
        
        // encode right subtree
        if (x.right == null) {
            T[i] = -1;
            i += 1;
        }
        else
            i = encode(x.right, i, T);
        
            return i;
    }

    // decode subtree rotted at T[i]
    // return subtree
    public static Node decode(int i, int[] T) {
        if (T[i] == -1)  {// null tree marker
            current++;
            return null;
        }
        else {
            Node subroot = new Node(null, null, T[i]);
            current++;
            subroot.left = decode(current, T);
            subroot.right = decode(current, T);
            return subroot;
        }
    }

    public static void main(String[] args) {
        Node e = new Node(null, null, 6);
        Node d = new Node(null, null, 3);
        Node c = new Node(d, e, 5);
        Node b = new Node(null, null, 1);
        Node a = new Node(b, c, 2);
        int[] T = new int[100];
        encode(a, 0, T);
        current = 0;
        Node root = decode(0, T);
    }
}
