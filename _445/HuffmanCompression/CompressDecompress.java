/**
 * It is okay to use ArrayList class but you are not allowed to use any other
 * predefined class supplied by Java.
 */
import java.util.ArrayList;

public class CompressDecompress
{
	/**
	 * Get a string representing a Huffman tree where its root node is root
	 * @param root the root node of a Huffman tree
	 * @return a string representing a Huffman tree
	 */
	public static String getTreeString(final BinaryNodeInterface<Character> root)
	{
		// TO DO
		String huffTree = "";

		if (root != null) {
			if (root.isLeaf()) { return ("L" + root.getData()); }
			else {
				if (root.hasLeftChild()) {
					huffTree += getTreeString(root.getLeftChild());
				}
				if (root.hasRightChild()) {
					huffTree += getTreeString(root.getRightChild());
				}
				if (huffTree != null) { return ("I" + huffTree); }
			}
		}

		return "";
	}

	/**
	 * Compress the message using Huffman tree represented by treeString
	 * @param treeString the string represents the Huffman tree of the message
	 * @param message the message to be compressed
	 * @return a string representing compressed message.
	 */
	public static String compress(final BinaryNodeInterface<Character> root, final String message)
	{
		// TO DO
		if (root != null) {
			String compressedString = "";
			for (int charIndex = 0; charIndex < message.length(); charIndex++) {
				compressedString += getPathTo(root, message.charAt(charIndex));
			}
			return compressedString;
		}
		return "";
	}
	
	/**
	 * Decompress the message using Huffman tree represented by treeString
	 * @param treeString the string represents the Huffman tree of the
	 * compressed message
	 * @param message the compressed message to be decompressed
	 * @return a string representing decompressed message
	 */
	public static String decompress(final String treeString, final String message)
	{
		// TO DO
		if (treeString != null && treeString.length() > 0) {
			if (message != null && message.length() > 0) {
				ArrayList<Character> compressChars = new ArrayList<Character>();
				for (int i = 0; i < message.length(); i++) {
					compressChars.add((Character) message.charAt(i));
				}
				ArrayList<Character> treeChars = new ArrayList<Character>();
				for (int i = 0; i < treeString.length(); i ++) {
					treeChars.add((Character) treeString.charAt(i));
				}
				ArrayList<Character> dataList = new ArrayList<Character>();
				ArrayList<String> pathList = new ArrayList<String>();
				treeChars.trimToSize();
				BinaryNodeInterface<Character> root = createTree(treeChars, dataList);
				
				//get all paths
				for (Character c : dataList) {
					pathList.add(getPathTo(root, c));
				}

				pathList.trimToSize();
				dataList.trimToSize();
				compressChars.trimToSize();
				
				
				String currentPath = "";
				String decompressedString = "";

				for (Character c : compressChars) {
					currentPath += String.valueOf(c);
					if (pathList.contains(currentPath)) {
						decompressedString += getTreeData(root, currentPath);
						currentPath = "";
					}
				}
				return decompressedString;
			}
		}
		return "";
	}

	private static String getPathTo(final BinaryNodeInterface<Character> root, char c)
	{
		if (root.isLeaf() && root.getData().equals((Character) c)) {
			return "";
		}
		
		if (root.hasLeftChild()) {
			String path = getPathTo(root.getLeftChild(), c);
			if (path != null) {
				return ("0" + path);
			}
		}
		if (root.hasRightChild()) {
			String path = getPathTo(root.getRightChild(), c);
			if (path != null) {
				return ("1" + path);
			}
		}

		return null;
	}

	private static BinaryNodeInterface<Character> createTree(final ArrayList<Character> treeChars, ArrayList<Character> dataList) {
		BinaryNodeInterface<Character> root = new BinaryNode<Character>();
		if (treeChars.size() > 0) {
			if (treeChars.get(0).equals('I')) {
				treeChars.remove(0); // removes I
				root.setLeftChild(createTree(treeChars, dataList));
				root.setRightChild(createTree(treeChars, dataList));
			}
			else if (treeChars.get(0).equals('L')) {
				treeChars.remove(0); // removes L
				root.setData(treeChars.remove(0)); // sets and removes data
				dataList.add(root.getData());
			}
		}
		return root;
	}
	
	private static Character getTreeData(final BinaryNodeInterface<Character> root, final String pathTo) {
		if (pathTo.length() > 0) {
			if (pathTo.charAt(0) == '0') {
				if (pathTo.length() == 1) {
					return getTreeData(root.getLeftChild(), "");
				}
				else {
					return getTreeData(root.getLeftChild(), pathTo.substring(1));
				}
			}
			if (pathTo.charAt(0) == '1') {
				if (pathTo.length() == 1) {
					return getTreeData(root.getRightChild(), "");
				}
				else {
					return getTreeData(root.getRightChild(), pathTo.substring(1));
				}
			}
		}
		return root.getData();
	}
}