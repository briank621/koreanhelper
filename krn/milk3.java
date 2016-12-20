/*
ID: brian621
LANG: JAVA
TASK: milk3
*/

import java.util.*;
import java.io.*;

public class milk3{
	
	static int a;
	static int b;
	static int c;

	static HashSet<Integer> pos = new HashSet<Integer>();
	static HashSet<int[]> seen = new HashSet<int[]>();

	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new FileReader("milk3.in"));
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("milk3.out")));

		String[] line = br.readLine().split(" ");
		a = Integer.parseInt(line[0]);
		b = Integer.parseInt(line[1]);
		c = Integer.parseInt(line[2]);

		bfs(0, 0, c);		
		List<Integer> ans = new ArrayList<Integer>(pos);
		Collections.sort(ans);

		String out = "";
		for(int i = 0; i < ans.size(); i++)
			out += i + " ";

		out = out.substring(0, out.length() - 1);

		pw.println(out);
		System.out.println(out);

		br.close();
		pw.close();
	}

	public static void bfs(int ma, int mb, int mc){
		int[] state = new int[]{ma, mb, mc};
		if(seen.contains(state))
			return;
		seen.add(state);

		if(ma == 0)
			pos.add(mc);
		//starting from a
		int over = b - (mb + ma);
		if(over > 0)
			bfs(over, b, mc);
		else
			bfs(0, mb + ma, mc);
		over = c - (mc + ma);
		if(over > 0)
			bfs(over, mb, c);
		else
			bfs(0, mb, ma + mc);
		//starting from b
		over = a - (mb + ma);
		if(over > 0)
			bfs(a, over, mc);
		else
			bfs(ma+mb, 0, mc);
		over = c - (mb + mc);
		if(over > 0)
			bfs(ma, over, c);
		else
			bfs(ma, 0, mb + mc);
		//starting from c
		over = a - (ma + mc);
		if(over > 0)
			bfs(a, mb, over);
		else
			bfs(ma + mc, mb, 0);
		over = b - (mb + mc);
		if(over > 0)
			bfs(ma, b, over);
		else
			bfs(ma, mb + mc, 0);
	}

}