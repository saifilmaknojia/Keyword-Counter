/**
 * @author Shaifil
 *
 */
public class FibonacciHeap {
	int trackNumberOfNodes;
	// total number of nodes present
	Node maxNodePointer;
	// pointer to the max node

	/**
	 * below function adds the new entry
	 */
	public void insert(Node newNode) {
		// if a node already exists in the root list, merge the new node with it and
		// point max to the node with highest key
		if (maxNodePointer != null) {
			combineSiblings(newNode, maxNodePointer);
			trackNumberOfNodes++;
			if (newNode.key > maxNodePointer.key)
				maxNodePointer = newNode;
		}
		// else make the new node as the max
		else {
			maxNodePointer = newNode;
			trackNumberOfNodes++;
		}
		// return n;
	}

	// joins siblings list of joining node with that of maxNodePointer
	// i.e readjust neighboring nodes
	void combineSiblings(Node maxNodePointer, Node joiningNode) {
		Node first = maxNodePointer.rightNeighbor;
		Node second = joiningNode.leftNeighbor;
		maxNodePointer.rightNeighbor = joiningNode;
		joiningNode.leftNeighbor = maxNodePointer;
		first.leftNeighbor = second;
		second.rightNeighbor = first;
	}


	/**
	 * If the heap is empty, null is returned. else Returns the keyword with the
	 * maximum key in the heap.
	 */
	public Node getMax() {
		if (maxNodePointer == null)
			return null;
		if (maxNodePointer.childNode != null)
			{
				setParentPointerNull();
		}
		// removing maxNodePointer from root list
		Node prevMax = maxNodePointer;
		if (maxNodePointer.rightNeighbor == maxNodePointer) {
			// if there was only one node 
			maxNodePointer = null;
			trackNumberOfNodes--;
		} else {
			// if more than 1 node in root list, assign the max pointer to right neighbor
			maxNodePointer = maxNodePointer.rightNeighbor;
			removeNodeFromFiboHeap(prevMax);

			pairwiseCombine();
			trackNumberOfNodes--;
		}
		cleanupRemovedNode(prevMax);
		return prevMax;

		// return null;
}

	// set parent pointer to null for max nodes children
	private void setParentPointerNull()
	{
		Node temporaryNode = maxNodePointer.childNode;
		// setting parent pointers for maxNodePointer's children to null
		// i.e moving max nodes children to root list
		while (temporaryNode.parent != null) {
			temporaryNode.parent = null;
			temporaryNode = temporaryNode.rightNeighbor;
		}
		// combining maxNodePointer's children to root list
		// i.e make max node the neighbors of newly added nodes in rootlist
		combineSiblings(temporaryNode, maxNodePointer);
	}

	// pairwiseCombines heaps of same degreeOfNode
	private void pairwiseCombine() {

		Node[] treesWithDegreeTracker = new Node[trackNumberOfNodes + 1];

		Node currentNode = maxNodePointer;
		Node nextNode = maxNodePointer;
		do {
			Node tempCombine1 = currentNode;
			int currDegree = currentNode.degreeOfNode;
			while (treesWithDegreeTracker[currDegree] != null) {
				//System.out.println("Size of array = "+treesWithDegreeTracker.length);
				Node tempCombine2 = treesWithDegreeTracker[currDegree];
				// tempcombine2 will store node with given degree by extracting it from array which will be 
				// merged with tempcombine1 
				if (tempCombine2.key > tempCombine1.key) {
					Node temporaryNode = tempCombine1;
					tempCombine1 = tempCombine2;
					tempCombine2 = temporaryNode;
				}
				if (tempCombine2 == nextNode) {
					nextNode = nextNode.rightNeighbor;
				}
				if (tempCombine2 == currentNode) {
					currentNode = currentNode.leftNeighbor;
				}
				joinSameDegreeNodes(tempCombine2, tempCombine1);
				treesWithDegreeTracker[currDegree++] = null;
			}
			treesWithDegreeTracker[currDegree] = tempCombine1;
			currentNode = currentNode.rightNeighbor;
		} while (currentNode != nextNode);

		// changing the max pointer to point to the new maxNode
		maxNodePointer = null;
		for (int i = 0; i < treesWithDegreeTracker.length; i++)
			if (treesWithDegreeTracker[i] != null) {
				if ((maxNodePointer == null) || (treesWithDegreeTracker[i].key > maxNodePointer.key))
					maxNodePointer = treesWithDegreeTracker[i];
			}
	}

	// set default values for the node which was removed
	void cleanupRemovedNode(Node removedNode) {
		removedNode.childNode = null;
		removedNode.degreeOfNode = 0;
		removedNode.parent = null;
		removedNode.childCut = false;
		removedNode.leftNeighbor = removedNode;
		removedNode.rightNeighbor = removedNode;
	}

	// this method basically makes the node with higher key as the parent of node with smaller key
	private void joinSameDegreeNodes(Node min, Node maxNodePointer) {
		removeNodeFromFiboHeap(min);
		min.parent = maxNodePointer;
		if (maxNodePointer.childNode != null)
			combineSiblings(maxNodePointer.childNode, min);
		else
			maxNodePointer.childNode = min;
		maxNodePointer.degreeOfNode++;
		min.childCut = false;
	}

	/**
	 * increases the key value associated with keyword. 
	 * If the tag already exists in
	 * Hashmap/heap, we just increase its key by the new value.
	 */
	public void increaseKey(Node node, int key) {
		node.key = key;
		Node parent = node.parent;
		if ((parent != null) && (node.key > parent.key)) {
			cutNode(node, parent);
			cascading(parent);
		}
		if (node.key > maxNodePointer.key)
			maxNodePointer = node;
	}

	// cut node childNode from parentNode
	private void cutNode(Node childNode, Node parentNode) {
		// remove x from y's children
		if (parentNode.childNode == childNode)
			parentNode.childNode = childNode.rightNeighbor;
		// rechecking above condition to check if there was only 1 child
		if (parentNode.childNode == childNode)
			parentNode.childNode = null;

		parentNode.degreeOfNode--;
		removeNodeFromFiboHeap(childNode);
		combineSiblings(childNode, maxNodePointer);
		childNode.parent = null;
		childNode.childCut = false;

	}

	private void cascading(Node m) {
		Node parentNode = m.parent;
		if (parentNode != null) {
			if (!m.childCut) {
				m.childCut = true;
			} else {
				cutNode(m, parentNode);
				cascading(parentNode);
			}
		}
	}

	// makes rightNeighbor and leftNeighbor point to itself
	void removeNodeFromFiboHeap(Node toRemoveNode) {
		if (toRemoveNode.rightNeighbor == toRemoveNode)
			return;
		toRemoveNode.leftNeighbor.rightNeighbor = toRemoveNode.rightNeighbor;
		toRemoveNode.rightNeighbor.leftNeighbor = toRemoveNode.leftNeighbor;
		toRemoveNode.rightNeighbor = toRemoveNode;
		toRemoveNode.leftNeighbor = toRemoveNode;
	}

}
