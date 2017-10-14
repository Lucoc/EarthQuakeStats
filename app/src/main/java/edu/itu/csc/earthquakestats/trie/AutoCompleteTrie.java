package edu.itu.csc.earthquakestats.trie;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * An trie data structure that implements AutoComplete interface.
 *
 * @author Jigar Gosalia
 *
 */
public class AutoCompleteTrie implements AutoComplete {

	private TrieNode root;

    private int size;

	public AutoCompleteTrie() {
        root = new TrieNode();
	}

	/**
	 * Insert a city name by creating and linking the necessary trie nodes into
	 * the trie.
	 * @return
	 */
	public boolean addWord(String cityName) {
		String cityNameL = cityName.toLowerCase();
		TrieNode node = root;
		for (char character : cityNameL.toCharArray()) {
			if (node.getValidNextCharacters().contains(character)) {
				node = node.getChild(character);
			} else {
				node = node.insert(character);
			}
		}
		if (!node.endsWord()) {
			node.setEndsWord(true);
			size++;
			return true;
		}
		return false;
	}

	/**
	 * Return the number of complete city names.
	 */
	public int size() {
		return this.size;
	}

	/**
	 * Returns whether the string is a word in the trie, using the algorithm
	 * described in the videos for this week.
	 */
	public boolean isWord(String s) {
		// Implement this method
		String wordToFind = s.toLowerCase();
		TrieNode node = root;
		for (char c : wordToFind.toCharArray()) {
			if (node.getValidNextCharacters().contains(c)) {
				node = node.getChild(c);
			} else {
				return false;
			}
		}
		if (node.endsWord()) {
			return true;
		}
		return false;
	}

	/**
	 * Return maximum count of recommended city names list.
	 *
	 * If prefix is not in the trie, it returns an empty list.
	 * 
	 * @param prefix
	 * @param count
	 * @return
	 */
	@Override
	public List<String> predict(String prefix, int count) {
		List<String> recommendations = new LinkedList<String>();
		TrieNode node = root;
		for (char c : prefix.toCharArray()) {
			if (node.getValidNextCharacters().contains(c)) {
				node = node.getChild(c);
			} else {
				return recommendations;
			}
		}
		if (node.endsWord()) {
			recommendations.add(node.getText());
		}

		Queue<TrieNode> nodeQueue = new LinkedList<TrieNode>();
		List<Character> children = new LinkedList<Character>(node.getValidNextCharacters());

		for (int i = 0; i < children.size(); i++) {
			char c = children.get(i);
			nodeQueue.add(node.getChild(c));
		}
		while (!nodeQueue.isEmpty() && recommendations.size() < count) {
			TrieNode firstNode = nodeQueue.poll();
			if (firstNode.endsWord()) {
				recommendations.add(firstNode.getText());
			}

			List<Character> childNodes = new LinkedList<Character>(firstNode.getValidNextCharacters());
			for (int i = 0; i < childNodes.size(); i++) {
				char c = childNodes.get(i);
				nodeQueue.add(firstNode.getChild(c));
			}
		}
		return recommendations;
	}

	/**
	 * Print the Trie Tree
	 * @return
	 */
	public String printTrieTree() {
		StringBuilder tree = new StringBuilder();
		return printNode(tree, root);
	}

	/**
	 * Pre-order Traversal from given node.
	 * @param tree
	 * @param currentNode
	 * @return
	 */
	public String printNode(StringBuilder tree, TrieNode currentNode) {
		if (currentNode != null) {
			tree.append(currentNode.getText() + "\n");
			TrieNode next = null;
			for (Character c : currentNode.getValidNextCharacters()) {
				next = currentNode.getChild(c);
				printNode(tree, next);
			}
		}
		return tree.toString();
	}

}