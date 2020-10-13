/*
 * Muhammad Suleman
 * CS_102_201902
 * Instructor: Giuseppe Turini
 */
package TennisDatabase;

/*
 * Class containing binary search tree that stores tennis players and related data 
 */
class TennisPlayerContainer implements TennisPlayerContainerInterface {
	private TennisPlayerContainerNode root; // root of tree
	private int numOfPlayers; // Number of players

	/*
	 * Gives number of players in BST
	 */
	@Override
	public int getNumPlayers() {
		return numOfPlayers;
	}

	/*
	 * returns player object with id == playerId
	 */
	@Override
	public TennisPlayer getPlayer(String playerId) throws TennisDatabaseRuntimeException {
		return getPlayerRec(root, playerId.toLowerCase());
	}

	/*
	 * Method that accompanies getPlayer and returns player using recursion
	 */
	private TennisPlayer getPlayerRec(TennisPlayerContainerNode currRoot, String playerId) {
		TennisPlayer player = null;
		if (currRoot == null) {
			// returns null
		} else {
			String currRootPlayerID = currRoot.getPlayer().getId().toLowerCase();
			if (playerId.compareTo(currRootPlayerID) == 0) {
				player = currRoot.getPlayer();
			} else if (playerId.compareTo(currRootPlayerID) > 0) {
				player = getPlayerRec(currRoot.getRight(), playerId);
			} else {
				player = getPlayerRec(currRoot.getLeft(), playerId);
			}
		}
		return player;
	}

	/*
	 * deletes player with id playerId in tree
	 */
	@Override
	public void deletePlayer(String playerId) throws TennisDatabaseRuntimeException {
		root = deletePlayerRec(root, playerId.toLowerCase());
		numOfPlayers--;
	}

	/*
	 * method that accompanies deletePlayer and deletes player maintaining the BST
	 */
	private TennisPlayerContainerNode deletePlayerRec(TennisPlayerContainerNode currRoot, String playerId) {
		if (currRoot == null) {
			throw new TennisDatabaseRuntimeException("No such item found");
		} else {
			String currRootPlayerID = currRoot.getPlayer().getId().toLowerCase();
			if (playerId.compareTo(currRootPlayerID) > 0) {
				currRoot.setRight(deletePlayerRec(currRoot.getRight(), playerId));
				return currRoot;
			} else if (playerId.compareTo(currRootPlayerID) < 0) {
				currRoot.setLeft(deletePlayerRec(currRoot.getLeft(), playerId));
				return currRoot;
			} else {
				return deleteNode(currRoot, playerId); // Returns currRoot with new right subtree.
			}
		}
	}

	/*
	 * Deletes the node and returns the children branch (if it has one)
	 */
	private TennisPlayerContainerNode deleteNode(TennisPlayerContainerNode currRoot, String playerId) {
		if (currRoot.getLeft() == null && currRoot.getRight() == null) {
			return null;
		} else if (currRoot.getLeft() == null) {
			return currRoot.getRight();
		} else if (currRoot.getRight() == null) {
			return currRoot.getLeft();
		} else {
			// Find the inorder successor of currRoot key.
			TennisPlayerContainerNode replacement = getLeftMostNode(currRoot.getRight());
			TennisPlayerContainerNode replacementItem = deleteLeftMostNode(currRoot.getRight());
			currRoot = replacement;
			currRoot.setRight(replacementItem);
			return currRoot;
		}
	}

	/*
	 * Returns the node that is the leftmost descendant of the subtree rooted at
	 * node
	 */
	private TennisPlayerContainerNode deleteLeftMostNode(TennisPlayerContainerNode node) {
		if (node.getLeft() == null) {
			return node.getRight();
		} else {
			TennisPlayerContainerNode replacementOfLeftChild = deleteLeftMostNode(node.getLeft());
			node.setLeft(replacementOfLeftChild);
			return node;
		}
	}

	/*
	 * Deletes leftmost descendant of treeNode. Returns subtree of deleted node.
	 */
	private TennisPlayerContainerNode getLeftMostNode(TennisPlayerContainerNode node) {
		if (node.getLeft() == null) {
			return node;
		} else {
			return getLeftMostNode(node.getLeft());
		}
	}

	/*
	 * Inserts player in tree
	 */
	@Override
	public void insertPlayer(TennisPlayer player) throws TennisDatabaseException {
		this.root = insertPlayerRec(this.root, player);
		this.numOfPlayers++;
	}

	/*
	 * Inserts tennis player in BST recursively
	 */
	private TennisPlayerContainerNode insertPlayerRec(TennisPlayerContainerNode currRoot, TennisPlayer newPlayer)
			throws TennisDatabaseException {
		if (currRoot == null) {
			TennisPlayerContainerNode newNode = new TennisPlayerContainerNode(newPlayer);
			return newNode;
		} else {
			int compareResult = currRoot.getPlayer().compareTo(newPlayer);
			if (compareResult == 0) {
				throw new TennisDatabaseException("Error: Player alreaddy exists!");
			} else if (compareResult > 0) {
				TennisPlayerContainerNode newNode = insertPlayerRec(currRoot.getLeft(), newPlayer);
				currRoot.setLeft(newNode);
				return currRoot;
			} else {
				TennisPlayerContainerNode newNode = insertPlayerRec(currRoot.getRight(), newPlayer);
				currRoot.setRight(newNode);
				return currRoot;
			}
		}
	}

	/*
	 * Insert tennis match in sorted linked list in each tennis player
	 */
	@Override
	public void insertMatch(TennisMatch match) throws TennisDatabaseException {
		String player1Id = match.getIdPlayer1();
		String player2Id = match.getIdPlayer2();
		getBSTNode(root, player1Id.toLowerCase()).insertMatch(match);
		getBSTNode(root, player2Id.toLowerCase()).insertMatch(match);
	}

	/*
	 * Gets the BST node of containing player with id playerId
	 * It is similar to getPlayer but returns node instead of player object
	 */
	private TennisPlayerContainerNode getBSTNode(TennisPlayerContainerNode currNode, String playerId) {
		String currRootPlayerID = currNode.getPlayer().getId().toLowerCase();
		if (currNode == null || playerId.compareTo(currRootPlayerID) == 0) {
			return currNode;
		} else if (playerId.compareTo(currRootPlayerID) > 0) {
			return getBSTNode(currNode.getRight(), playerId);
		} else {
			return getBSTNode(currNode.getLeft(), playerId);
		}
	}

	/*
	 * Not used/required
	 */
	@Override
	public TennisMatch[] getMatchesOfPlayer(String playerId)
			throws TennisDatabaseException, TennisDatabaseRuntimeException {
		return null;
	}

	/*
	 * Returns the iterator object of TennisPlayerContainer
	 */
	@Override
	public TennisPlayerContainerIterator iterator() {
		TennisPlayerContainerIterator tpcIterator = new TennisPlayerContainerIterator(root);
		return tpcIterator;
	}

}
