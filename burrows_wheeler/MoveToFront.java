/**
 *
 * @author huseyngasimov
 */
public class MoveToFront {
	private static int R = 256; // vocabulary size	
	
	public static void encode() {
		char[] seq = new char[R];		
		for (int i = 0; i < R; i++) seq[i] = (char)i;
		
		while(!BinaryStdIn.isEmpty()) {
			char c = BinaryStdIn.readChar();
			int ind = moveToFront(seq, c); // index of c before moving to front
			BinaryStdOut.write(ind, 8);
		}
				
		BinaryStdOut.flush();
		BinaryStdOut.close();
	}
	
	public static void decode() {
		char[] seq = new char[R];		
		for (int i = 0; i < R; i++) seq[i] = (char)i;
		
		while(!BinaryStdIn.isEmpty()) {
			int ind = BinaryStdIn.readChar(); 			
			BinaryStdOut.write(seq[ind]);
			moveToFront(seq, ind);
		}
				
		BinaryStdOut.flush();
		BinaryStdOut.close();
	}
	
	public static void main(String[] args) {	
		if (args.length == 0) return;
		if (args[0].equals("-")) { encode(); return; }
		if (args[0].equals("+")) { decode(); return; }		
	}
	
	private static int moveToFront(char[] seq, char c) {
		for (int i = 0; i < seq.length; i++) 
			if (seq[i] == c) {
				for (int j = i-1; j >= 0; j--) seq[j+1] = seq[j];
				seq[0] = c;
				return i;
			}
		return -1;
	} 	
	
	private static void moveToFront(char[] seq, int i) {		
		if (i < 0 || i > seq.length - 1) return;
		char c = seq[i];
		for (int j = i-1; j >= 0; j--) seq[j+1] = seq[j];
		seq[0] = c;
	}
	
	private static void printArr(char[] seq) {
		for (int i = 0; i < seq.length; i++) System.out.print(seq[i] + " ");
		System.out.println();
	}
}