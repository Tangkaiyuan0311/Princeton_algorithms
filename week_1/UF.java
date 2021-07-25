import java.util.*;

public class UF
{ // disjoint-set forest

	private int[] id; // parent link
	private int[] sz; // subtree size
	private int cnt; // # connected-components

	public UF(int N)
	{
		cnt = N;
		id = new int[N];
		sz = new int[N];
		for (int i = 0; i < N; i++)
		{
			id[i] = i;
			sz[i] = 1;
		}
	}

	public int count()
	{
		return cnt;
	}


	// validate that p is a valid index
    	private void validate(int p) {
        	int n = id.length;
        	if (p < 0 || p > n-1) {
            		throw new IllegalArgumentException("index " + p + " is not between 0 and " + (n-1));
        	}
   	}


	private int find_set(int p)
	{
		validate(p);
		while (id[p] != p)
		{
			id[p] = id[id[p]]; // path compression
			p = id[p];
		}
		return p;
	}

	public void union(int p, int q)
	{
		int root_p = find_set(p);
		int root_q = find_set(q);
		if (root_p == root_q)
			return;
		if (sz[root_p] < sz[root_q])
		{
			id[root_p] = root_q;
			sz[root_q] += sz[root_p];
		}
		else
		{
			id[root_q] = root_p;
                        sz[root_p] += sz[root_q];
		}
		cnt--;
	}


	public boolean connected(int p, int q)
	{
		return (find_set(p) == find_set(q));
	}




	public static void main(String[] args)
	{
		Scanner in = new Scanner(System.in);
		int N = in.nextInt();
		UF uf = new UF(N);
		while (in.hasNext())
		{
			int p = in.nextInt();
			int q = in.nextInt();
			if (uf.connected(p, q)) continue;
			uf.union(p, q);
		}
		System.out.println(uf.count() + " connected components");
	}
}
