/*
 * Muhammad Suleman
 * CS_102_201902
 * Instructor: Giuseppe Turini
 */
package TennisDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

/*
 * Class containing methods which allows to manipulate a collection of tennis records
 * It has two objects one of TennisPlayerContainer and the other is of TennisMatchContainer
 */
public class TennisDatabase implements TennisDatabaseInterface {
	private TennisPlayerContainer tpc; // TennisPlayerContainer object
	private TennisMatchContainer tmc; // TennisMatchContainer object
	// Constants used when updating player score in switch statement
	private final int PLAYER1WON = 1;
	private final int PLAYER2WON = 2;

	public TennisDatabase() {
		// Initializes both objects to null
		tpc = new TennisPlayerContainer();
		tmc = new TennisMatchContainer();
	}

	/*
	 * Loads Player and Matches from files In case of wrong/incomplete data skips
	 * over the line
	 */
	@Override
	public void loadFromFile(String fileName) throws TennisDatabaseException, TennisDatabaseRuntimeException {
		boolean errorInFile = false;
		// Checking for file error for scanner
		Scanner fileScanner;
		try {
			fileScanner = new Scanner(new File(fileName));
			fileScanner.useDelimiter("[\\r\\n]*");
		} catch (FileNotFoundException e) {
			errorInFile = true;
			throw new TennisDatabaseException("Error: File couldn't be read");
		}

		while (fileScanner.hasNextLine()) {
			String line = fileScanner.nextLine();
			String[] lineSplit = line.split("/");
			// Splits line into array using delimiter "/"
			// A complete line has 5 "/" therefore have 6 elements in array
			if (lineSplit.length != 6) {
				errorInFile = true;
				continue; // Continue to next line
			} else {
				// Checks if any of the entered data is blank or empty
				boolean emptyField = false;
				for (int i = 0; i < lineSplit.length; i++) {
					if (lineSplit[i].isEmpty() || lineSplit[i].isBlank()) {
						emptyField = true;
					}
				}
				if (emptyField) {
					errorInFile = true;
					continue; // Continue to next line
				}
			}
			if (lineSplit[0].equalsIgnoreCase("PLAYER")) {
				try {
					String id = lineSplit[1];
					String firstName = lineSplit[2];
					String lastName = lineSplit[3];
					int year = Integer.parseInt(lineSplit[4]);
					String country = lineSplit[5];
					insertPlayer(id, firstName, lastName, year, country);
				} catch (Exception e) {
					// Catches error if there is either NumberFormatException or
					// TennisDatabaseRuntimeException | TennisDatabaseException
					errorInFile = true;
				}
			} else {
				try {
					String idPlayer1 = lineSplit[1];
					String idPlayer2 = lineSplit[2];
					String date = lineSplit[3];
					int year = Integer.parseInt(date.substring(0, 4));
					int month = Integer.parseInt(date.substring(4, 6));
					int day = Integer.parseInt(date.substring(6, 8));
					String tournament = lineSplit[4];
					String score = lineSplit[5];
					insertMatch(idPlayer1, idPlayer2, year, month, day, tournament, score);
				} catch (Exception e) {
					// Catches error if there is either NumberFormatException or
					// TennisDatabaseRuntimeException | TennisDatabaseException
					errorInFile = true;
				}
			}
		}
		fileScanner.close();
		if (errorInFile) {
			throw new TennisDatabaseRuntimeException("Error reading file");
		}
	}

	/*
	 * Saves data to a text files. Gives error if file could not be saved
	 */
	@Override
	public void saveToFile(String fileName) throws TennisDatabaseException {
		try {
			PrintWriter writer = new PrintWriter(new File(fileName), "UTF-8");
			TennisPlayerContainerIterator preorder = tpc.iterator();
			preorder.setPreorder();
			while (preorder.hasNext()) {
				writer.write(preorder.next().toString());
				writer.write("\n");
			}
			TennisMatch[] allMatches = getAllMatches();
			for (int i = 0; i < allMatches.length; i++) {
				writer.write(allMatches[i].toString());
				writer.write("\n");
			}
			writer.close();
		} catch (Exception e) {
			throw new TennisDatabaseException("Could not create file!");
		}
	}

	/*
	 * Resets both container so delete database
	 */
	@Override
	public void reset() {
		tmc = null;
		tpc = null;
	}

	/*
	 * Gets player object with id "id"
	 */
	@Override
	public TennisPlayer getPlayer(String id) throws TennisDatabaseRuntimeException {
		TennisPlayer player = tpc.getPlayer(id);
		if (player == null) {
			throw new TennisDatabaseRuntimeException("No such player exists!");
		}
		return player;
	}

	/*
	 * Gets all players from TennisPlayerContainer in ascending order
	 */
	@Override
	public TennisPlayer[] getAllPlayers() throws TennisDatabaseRuntimeException {
		try {
			TennisPlayerContainerIterator inorder = tpc.iterator();
			inorder.setInorder();
			int numOfPlayers = tpc.getNumPlayers();
			TennisPlayer[] allPlayers = new TennisPlayer[numOfPlayers];
			int index = 0;
			while (inorder.hasNext()) {
				allPlayers[index] = inorder.next();
				index++;
			}
			return allPlayers;
		} catch (Exception e) {
			throw new TennisDatabaseRuntimeException("Tennis Player Container is empty!");
		}
	}

	/*
	 * Not used in code since user interface is provided
	 */
	@Override
	public TennisMatch[] getMatchesOfPlayer(String playerId)
			throws TennisDatabaseException, TennisDatabaseRuntimeException {
		return null;
	}

	/*
	 * Get all matches of a tennis player from TennisMatchContainer
	 */
	@Override
	public TennisMatch[] getAllMatches() throws TennisDatabaseRuntimeException {
		try {
			return tmc.getAllMatches();
		} catch (Exception e) {
			throw new TennisDatabaseRuntimeException("Tennis Match Container is empty!");
		}
	}

	/*
	 * Creates TennisPlayer object and inserts it in TennisPlayerContainer
	 */
	@Override
	public void insertPlayer(String id, String firstName, String lastName, int year, String country)
			throws TennisDatabaseException {
		TennisPlayer newPlayer = new TennisPlayer(id, firstName, lastName, year, country);
		tpc.insertPlayer(newPlayer);
	}

	/*
	 * Deletes a player from TennisPlayerContainer. If player doesn't exists gives
	 * error.
	 */
	@Override
	public void deletePlayer(String playerId) throws TennisDatabaseRuntimeException {
		tpc.deletePlayer(playerId);
		tmc.deleteMatchesOfPlayer(playerId);
		resetPlayersWinLossRatio();
		TennisMatch[] allMatches = getAllMatches();
		for (TennisMatch match : allMatches) {
			updateScore(match);
		}
		if (allMatches.length == 0)
			resetPlayersWinLossRatio();

	}

	/*
	 * Resets win/lose ration. Used when a player is deleted to update scores
	 */
	private void resetPlayersWinLossRatio() {
		TennisPlayer[] allPlayers = getAllPlayers();
		for (TennisPlayer player : allPlayers) {
			player.setWins(0);
			player.setLosses(0);
			player.setWinLossRatio("0/0");
		}
	}

	/*
	 * Creates TennisMatch object and inserts it in TennisMatchContainer
	 */
	@Override
	public void insertMatch(String idPlayer1, String idPlayer2, int year, int month, int day, String tournament,
			String score) throws TennisDatabaseException {
		TennisMatch newMatch = null;
		// Tries to insert match. Throws exception if match is invalid
		try {
			newMatch = new TennisMatch(idPlayer1, idPlayer2, year, month, day, tournament, score);
		} catch (TennisDatabaseRuntimeException e) {
			throw new TennisDatabaseRuntimeException("Skipped Tennis Match with invalid score");
		}
		// If match score is valid and both players are in TennisPlayerContainer
		if (newMatch != null && checkMatchValidity(newMatch)) {
			tmc.insertMatch(newMatch);
			tpc.insertMatch(newMatch);
			updateScore(newMatch);
		} else {
			throw new TennisDatabaseRuntimeException("Skipped Tennis Match with invalid score");
		}
	}

	/*
	 * Checks if both players are in TennisPlayerContainer otherwise returns false
	 */
	private boolean checkMatchValidity(TennisMatch newMatch) {
		boolean isValid = false;
		if (getPlayer(newMatch.getIdPlayer1()) != null && getPlayer(newMatch.getIdPlayer2()) != null) {
			isValid = true;
		}
		return isValid;
	}

	/*
	 * Updates score after a match is added Gets both players in match and updates
	 * their win/lose ration as well
	 */
	private void updateScore(TennisMatch newMatch) {
		TennisPlayer player1 = getPlayer(newMatch.getIdPlayer1());
		TennisPlayer player2 = getPlayer(newMatch.getIdPlayer2());
		int winner = newMatch.getWinner();
		switch (winner) {
		case PLAYER1WON:
			player1.setWins(player1.getWins() + 1);
			player2.setLosses(player2.getLosses() + 1);
			break;
		case PLAYER2WON:
			player2.setWins(player2.getWins() + 1);
			player1.setLosses(player1.getLosses() + 1);
			break;
		default:
			// In case if a match is draw it doesn't update either of the
			// player's win/lose ratio
			break;
		}
		// Sets win/lose ratio
		player1.setWinLossRatio(player1.getWins() + "/" + player1.getLosses());
		player2.setWinLossRatio(player2.getWins() + "/" + player2.getLosses());
	}
}
