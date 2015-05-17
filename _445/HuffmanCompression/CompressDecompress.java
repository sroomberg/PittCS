import java.util.ArrayList;

public class CompressDecompress
{
	public static String getTreeString(final BinaryNodeInterface<Character> root)
	{
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

	public static String compress(final BinaryNodeInterface<Character> root, final String message)
	{
		if (root != null) {
			String compressedString = "";
			for (int charIndex = 0; charIndex < message.length(); charIndex++) {
				compressedString += getPathTo(root, message.charAt(charIndex));
			}
			return compressedString;
		}
		return "";
	}
	
	public static String decompress(final String treeString, final String message)
	{
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