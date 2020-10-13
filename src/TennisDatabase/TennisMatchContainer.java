/*
 * Muhammad Suleman
 * CS_102_201902
 * Instructor: Giuseppe Turini
 */
package TennisDatabase;

import java.util.Iterator;
import java.util.LinkedList;

/*
 * Class containing a linked list that stores all matches of tennis players and related methods
 */
class TennisMatchContainer implements TennisMatchContainerInterface {

	private LinkedList<TennisMatch> tennisMatches; // List to store tennis matches

	// Constructor
	public TennisMatchContainer() {
		tennisMatches = new LinkedList<TennisMatch>();
	}

	/*
	 * Return number of matches
	 */
	@Override
	public int getNumMatches() {
		return tennisMatches.size();
	}

	/*
	 * returns the iterator object
	 */
	@Override
	public Iterator<TennisMatch> iterator() {
		return tennisMatches.iterator();
	}

	/*
	 * Inserts the match in list sorted
	 */
	@Override
	public void insertMatch(TennisMatch m) throws TennisDatabaseException {
		if (tennisMatches.isEmpty()) {
			tennisMatches.add(m);
		} else {
			int index = binaryInsertion(m, tennisMatches.size(), 0);
			tennisMatches.add(index, m);
		}

	}

	/*
	 * Method that inserts the match in list based on date of match
	 */
	private int binaryInsertion(TennisMatch match, int upperBound, int lowerBound) {
		while (upperBound > lowerBound) {
			int midPoint = (upperBound + lowerBound) / 2;
			int compareResult = tennisMatches.get(midPoint).compareTo(match);
			// tennisMatches.get(midPoint) > match
			if (compareResult == 1) {
				upperBound = midPoint;
			} else {
				lowerBound = midPoint + 1;
			}
		}
		return lowerBound;
	}

	/*
	 * returns all matches in linked list
	 */
	@Override
	public TennisMatch[] getAllMatches() throws TennisDatabaseRuntimeException {
		TennisMatch[] allMatches = tennisMatches.toArray(new TennisMatch[tennisMatches.size()]);
		return allMatches;
	}

	/*
	 * Not used since user interface is provided
	 */
	@Override
	public TennisMatch[] getMatchesOfPlayer(String playerId) throws TennisDatabaseRuntimeException {
		return null;
	}

	/*
	 * Deletes matches of player that was deleted
	 */
	@Override
	public void deleteMatchesOfPlayer(String playerId) throws TennisDatabaseRuntimeException {
		for (int i = 0; i < tennisMatches.size(); i++) {
			if (tennisMatches.get(i).getIdPlayer1().equalsIgnoreCase(playerId)) {
				tennisMatches.remove(i);
				i--;
			} else if (tennisMatches.get(i).getIdPlayer2().equalsIgnoreCase(playerId)) {
				tennisMatches.remove(i);
				i--;
			}
		}
	}

}
