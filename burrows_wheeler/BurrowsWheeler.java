import java.util.LinkedList;

/**
 *
 * @author huseyngasimov
 */
public class BurrowsWheeler {
	private static int R = 256; // dictionary size
	
	public static void encode() {
		String s = BinaryStdIn.readString();
		int l = s.length();
		CircularSuffixArray csa = new CircularSuffixArray(s);
		char[] last = new char[l];
		int first = 0;
		for (int i = 0; i < l; i++) {
			if (csa.index(i) == 0) {
				last[i] = s.charAt(l-1);		
				first = i;
			}
			else 
				last[i] = s.charAt(csa.index(i)-1);
		}

		BinaryStdOut.write(first, 32);		
		for (int i = 0; i < l; i++) BinaryStdOut.write(last[i]);		
		BinaryStdOut.flush();
		BinaryStdOut.close();
	}
	
	public static void decode() {
		int first = BinaryStdIn.readInt(32);				
		char[] t = BinaryStdIn.readString().toCharArray();
		int[] count = new int[R+1];
		char[] firstCol = sort(t, count);		
		int N = t.length;
		
		// --- create the next[] array ---
		int[] next = new int[N];
		LinkedList[] index = new LinkedList[R+1];
		for (int i = 0; i < R+1; i++) 
			if (count[i] > 0) index[i] = new LinkedList<Integer>();
		
		for (int i = 0; i < N; i++) index[t[i]].add(i);	// save all appearances of characters in the index[] array
		
		for (int i = 0; i < N; i++)
			next[i] = (Integer)index[firstCol[i]].remove();
		
		// --- decoding ---
		int j = first;
		char[] origString = new char[N];
		for (int i = 0; i < N; i++, j = next[j]) {
			origString[i] = firstCol[j];			
		}
		
		for (int i = 0; i < N; i++) BinaryStdOut.write(origString[i]);		
		BinaryStdOut.flush();
		BinaryStdOut.close();
	}
	
	/** 
	 * --- radix sort ~ 11 N + 4 R running time ---        
	 * dont change a[], save frequency counts in the count[] array
	 * return the sorted array
	 */
	private static char[] sort(char[] a, int[] count) {
		int N = a.length;		
		
		// compute frequency counts
		for (int i = 0; i < N; i++)
			count[a[i]+1]++;
		
		// transform counts to indicies
		for (int r = 0; r < R; r++)
			count[r+1] += count[r];
		
		// distribute
		char[] aux = new char[N];
		for (int i = 0; i < N; i++)
			aux[count[a[i]]++] = a[i];
		
		return aux;
	}
	
	public static void main(String[] args) {
		if (args.length == 0) return;
		if (args[0].equals("-")) { encode(); return; }
		if (args[0].equals("+")) decode();
	}
}