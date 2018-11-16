/**
 * @author Shaifil
 *
 */
public class Node {
	// getters and declarations of a particular fibonacci heap structure's node
	boolean childCut;
	Node childNode;
	Node leftNeighbor;
	Node parent;
	Node rightNeighbor;
	String keyword;
	int key;
	int degreeOfNode;

	public Node(String keyword, int key) {
		rightNeighbor = this;
		leftNeighbor = this;
		this.key = key;
		this.keyword = keyword;
	}

	public int getKey() {
		return key;
	}

	public String getkeyword() {
		return keyword;
	}
}
