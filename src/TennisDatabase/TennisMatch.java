/*
 * Muhammad Suleman
 * CS_102_201902
 * Instructor: Giuseppe Turini
 */
package TennisDatabase;

import java.util.Calendar;
import java.util.GregorianCalendar;

/*
 * Class containing members that corresponds to the tennis match and methods related to tennis matches 
 */
public class TennisMatch implements TennisMatchInterface {

	private String idPlayer1; // 1st player ID
	private String idPlayer2; // 2nd Player ID
	private int dateYear; // Year of match date
	private int dateMonth; // Month of match date
	private int dateDay; // day of match date
	private String dateString; // String that combines year, month and day in YYYY/MM/DD format
	private String tournament; // Location of match
	private String matchScore; // Match score
	private int winner; // Indicates who is the winner in match. It is either 1 or 2

	// Constructor
	public TennisMatch(String idPlayer1, String idPlayer2, int dateYear, int dateMonth, int dateDay, String tournament,
			String matchScore) {
		this.idPlayer1 = idPlayer1;
		this.idPlayer2 = idPlayer2;
		this.dateYear = dateYear;
		this.dateMonth = dateMonth;
		this.dateDay = dateDay;
		this.setDateString(dateYear + "/" + dateMonth + "/" + dateDay);
		this.tournament = tournament;
		this.matchScore = matchScore;
		this.winner = processMatch(matchScore);
	}

	/*
	 * Compares the date of this.TennisMatch object and TennisMatch o Returns 0 if
	 * this.TennisMatch == TennisMatch o Returns 1 if this.TennisMatch > TennisMatch
	 * o Returns -1 if this.TennisMatch < TennisMatch o
	 */
	@Override
	public int compareTo(TennisMatch o) {
		Calendar date1 = new GregorianCalendar(getDateYear(), getDateMonth(), getDateDay());
		Calendar date2 = new GregorianCalendar(o.getDateYear(), o.getDateMonth(), o.getDateDay());
		return date1.compareTo(date2);
	}

	/*
	 * Returns idPlayer1
	 */
	@Override
	public String getIdPlayer1() {
		return idPlayer1;
	}

	/*
	 * Returns idPlayer2
	 */
	@Override
	public String getIdPlayer2() {
		return idPlayer2;
	}

	/*
	 * Returns dateYear
	 */
	@Override
	public int getDateYear() {
		return dateYear;
	}

	/*
	 * Returns dateMonth
	 */
	@Override
	public int getDateMonth() {
		return dateMonth;
	}

	/*
	 * Returns dateDay
	 */
	@Override
	public int getDateDay() {
		return dateDay;
	}

	/*
	 * returns the dateString
	 */
	public String getDateString() {
		return dateString;
	}

	/*
	 * sets dateString
	 */
	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	/*
	 * Returns tournament
	 */
	@Override
	public String getTournament() {
		return tournament;
	}

	/*
	 * Returns matchScore
	 */
	@Override
	public String getMatchScore() {
		return matchScore;
	}

	/*
	 * Returns winner
	 */
	@Override
	public int getWinner() {
		return winner;
	}

	/*
	 * Separates the individual matches in score in passes it to
	 * processMatchScoreRec which returns setScore indicating how many times each
	 * player won. Example (2,1) 2 > 1 than thus player1 won and returns 1 if
	 * player2 wins returns 2 else 0
	 */
	private int processMatch(String matchScore) throws TennisDatabaseRuntimeException {
		int[] setScore = processMatchScoreRec(matchScore.split(","));
		int setsPlayer1 = setScore[0];
		int setsPlayer2 = setScore[1];
		if (setsPlayer1 > setsPlayer2) {
			return 1;
		} else if (setsPlayer1 < setsPlayer2) {
			return 2;
		} else {
			return 0;
		}
	}

	/*
	 * Takes the matchScore such as 6-7,7-9,5-4 Takes the first match 6-7 in this
	 * case passes it to calculateMatchSet(6-7) and passes the rest to
	 * processMatchScoreRec({"7-9","5-4"}) until matchScore array contains only one
	 * element which is passed to calculateMatchSet(5-4) Adds the setScore of all
	 * individual matches to determine overall score
	 */
	private int[] processMatchScoreRec(String[] matchScore) throws TennisDatabaseRuntimeException {
		int[] setScore = { 0, 0 };
		// If match score contains only one element
		if (matchScore.length == 1) {
			setScore = calculateMatchSet(matchScore[0]);
		} else {
			// Takes matchScore and store it in temp (except first element of matchScore)
			String[] temp = new String[matchScore.length - 1];
			for (int i = 1; i < matchScore.length; i++) {
				temp[i - 1] = matchScore[i];
			}
			// Passes first element of matchScore to calculateMatchSet and rest to
			// processMatchScoreRec
			setScore = addTwoIntArray(calculateMatchSet(matchScore[0]), processMatchScoreRec(temp));
		}
		return setScore;
	}

	/*
	 * Adds two array element by element returns the sum of both array
	 */
	private int[] addTwoIntArray(int[] array1, int[] array2) {
		int[] sumArray = new int[array1.length];
		for (int i = 0; i < sumArray.length; i++) {
			sumArray[i] = array1[i] + array2[i];
		}
		return sumArray;
	}

	/*
	 * Takes individual matches and checks who won returns the setScore of
	 * individual matches
	 */
	private int[] calculateMatchSet(String matchSet) throws TennisDatabaseRuntimeException {
		int[] setScore = { 0, 0 };
		String[] temp = matchSet.split("-");
		try {
			if (Integer.parseInt(temp[0]) > Integer.parseInt(temp[1])) {
				setScore[0]++;
			} else if (Integer.parseInt(temp[0]) < Integer.parseInt(temp[1])) {
				setScore[1]++;
			}
			return setScore;
		} catch (Exception e) {
			throw new TennisDatabaseRuntimeException("Score format is incorrect");
		}
	}

	/*
	 * Output match objects in string format so matches can be exported to a file
	 * The format is MATCH/idPlayer1/idPlayer2/dateString/tournament/matchScore
	 */
	public String toString() {
		return String.format("MATCH/%s/%s/%04d%02d%02d/%s/%s", this.idPlayer1, this.idPlayer2, this.dateYear,
				this.dateMonth, this.dateDay, this.tournament, this.matchScore);
	}
}
