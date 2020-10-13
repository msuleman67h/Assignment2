/*
 * Muhammad Suleman
 * CS_102_201902
 * Instructor: Giuseppe Turini
 */
package TennisDatabase;

/*
 * Class containing members corresponding to Tennis Players and its methods
 */
public class TennisPlayer implements TennisPlayerInterface {

	private String id; // Player id
	private String firstName; // Player first name
	private String lastName; // player last name
	private int birthYear; // player birth year
	private String country; // player country
	private int wins; // number of times player won
	private int losses; // number of times player lost
	private String winLossRatio; // win to lose ratio

	// Constructor
	public TennisPlayer(String id, String firstName, String lastName, int birthYear, String country) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthYear = birthYear;
		this.country = country;
		this.winLossRatio = "0/0";
	}

	/*
	 * Compares the id of this.TennisPlayer object and TennisPlayer o Returns 0 if
	 * this.TennisPlayer == TennisPlayer o Returns > 0 if this.TennisPlayer >
	 * TennisPlayer o Returns < 0 if this.TennisPlayer < TennisPlayer o
	 */
	@Override
	public int compareTo(TennisPlayer o) {
		return this.id.toLowerCase().compareTo(o.getId().toLowerCase());
	}

	/*
	 * Returns id
	 */
	@Override
	public String getId() {
		return id;
	}

	/*
	 * Returns firstName
	 */
	@Override
	public String getFirstName() {
		return firstName;
	}

	/*
	 * Returns lastName
	 */
	@Override
	public String getLastName() {
		return lastName;
	}

	/*
	 * Returns birthYear
	 */
	@Override
	public int getBirthYear() {
		return birthYear;
	}

	/*
	 * Returns country
	 */
	@Override
	public String getCountry() {
		return country;
	}

	/*
	 * Returns wins
	 */
	public int getWins() {
		return wins;
	}

	/*
	 * Returns losses
	 */
	public int getLosses() {
		return losses;
	}

	/*
	 * Sets this.wins = wins
	 */
	public void setWins(int wins) {
		this.wins = wins;
	}

	/*
	 * Sets this.losses = losses
	 */
	public void setLosses(int losses) {
		this.losses = losses;
	}

	/*
	 * returns the winLossRatio
	 */
	public String getWinLossRatio() {
		return winLossRatio;
	}

	/*
	 * sets the winLossRatio
	 */
	public void setWinLossRatio(String winLossRatio) {
		this.winLossRatio = winLossRatio;
	}

	/*
	 * return player object in string pattern so it can be exported to a file
	 * pattern PLAYER/id/firstName/lastName/birthYear/country
	 */
	public String toString() {
		return String.format("PLAYER/%s/%s/%s/%d/%s", this.id, this.firstName, this.lastName, this.birthYear,
				this.country);
	}
}
