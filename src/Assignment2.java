
/*
 * Muhammad Suleman
 * CS_102_201902
 * Instructor: Giuseppe Turini
 */
import java.io.File;
import java.time.LocalDate;
import java.util.Optional;

import TennisDatabase.TennisDatabase;
import TennisDatabase.TennisDatabaseException;
import TennisDatabase.TennisDatabaseRuntimeException;
import TennisDatabase.TennisMatch;
import TennisDatabase.TennisPlayer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/*
 * Main class 
 * Includes user interface and controls for user
 */
public class Assignment2 extends Application {

	private TennisDatabase tdb; // TennisDatabase object
	private Label messageBox; // Displays messages and warnings
	private TableView<TennisPlayer> playerTable; // Displays all tennis players
	private TableView<TennisMatch> matchesTable; // Displays all tennis matches

	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage primaryStage) {
		tdb = new TennisDatabase();
		try {
			BorderPane root = new BorderPane();

			// Load File button
			Button loadFile = new Button("Load file");
			loadFile.setOnAction(myHandler);

			// Export File Button
			Button exportFile = new Button("Export to file");
			exportFile.setOnAction(myHandler);

			// Reset Button Database
			Button resetDatabase = new Button("Reset Database");
			resetDatabase.setOnAction(e -> {
				// Creates an alert box to warn user that they are deleting database
				Alert warningBox = new Alert(AlertType.CONFIRMATION);
				// Styling Alert dialog
				DialogPane dialogPane = warningBox.getDialogPane();
				Stage dialogStage = (Stage) dialogPane.getScene().getWindow();
				dialogStage.getIcons().add(new Image("file:icon.png"));
				dialogPane.getStylesheets().add(getClass().getResource("alertBox.css").toExternalForm());
				dialogPane.getStyleClass().add("myDialog");
				warningBox.setTitle("Reset Database");
				// Asking user for confirmation and wait for user reply
				warningBox.setContentText("Are you sure you want to reset database? This action is irrevesible!");
				Optional<ButtonType> userFeedback = warningBox.showAndWait();
				if (userFeedback.get() == ButtonType.OK) {
					// Reseting database
					tdb.reset();
					updateTennisPlayerTable();
					updateTennisMatchesTable();
					displayNormalMessageInMessageBox("Database reset was successful!");
				} else {
					// If user selects no for answer
					clearMessageBox();
				}
			});
			ToolBar toolBar = new ToolBar(loadFile, exportFile, resetDatabase);
			toolBar.setPadding(new Insets(3.5, 2, 3.5, 2));
			root.setTop(toolBar);

			// Tennis Players Label
			Label playerTableLbl = new Label("Players Table");
			// Tennis Players Table
			playerTable = new TableView<TennisPlayer>();
			// id column
			TableColumn<TennisPlayer, String> idCol = new TableColumn<TennisPlayer, String>("Player ID");
			idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
			// firstName column
			TableColumn<TennisPlayer, String> firstNameCol = new TableColumn<TennisPlayer, String>("First Name");
			firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
			// lastName column
			TableColumn<TennisPlayer, String> lastNameCol = new TableColumn<TennisPlayer, String>("Last Name");
			lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
			// birthYear column
			TableColumn<TennisPlayer, Integer> birthYearCol = new TableColumn<TennisPlayer, Integer>("Birth Year");
			birthYearCol.setCellValueFactory(new PropertyValueFactory<>("birthYear"));
			// country column
			TableColumn<TennisPlayer, String> countryCol = new TableColumn<TennisPlayer, String>("Country");
			countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
			// Win/Loss column
			TableColumn<TennisPlayer, String> winLoseCol = new TableColumn<TennisPlayer, String>("Win/Loss");
			winLoseCol.setCellValueFactory(new PropertyValueFactory<>("winLossRatio"));
			// Adding columns to table
			playerTable.getColumns().addAll(idCol, firstNameCol, lastNameCol, birthYearCol, countryCol, winLoseCol);
			playerTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			// Adding playerTableLbl & playerTable to VBox
			VBox playerVBox = new VBox(playerTableLbl, playerTable);
			playerVBox.setPadding(new Insets(0, 2.5, 5, 2.5));
			VBox.setVgrow(playerTableLbl, Priority.ALWAYS);
			VBox.setVgrow(playerTable, Priority.ALWAYS);

			// Matches Label
			Label matchTableLbl = new Label("Matches Table");
			// Tennis TableView
			matchesTable = new TableView<TennisMatch>();
			// Date Column
			TableColumn<TennisMatch, String> dateCol = new TableColumn<TennisMatch, String>("Date");
			dateCol.setCellValueFactory(new PropertyValueFactory<>("dateString"));
			// First Player Column
			TableColumn<TennisMatch, String> firstPlayerCol = new TableColumn<TennisMatch, String>("First Player");
			firstPlayerCol.setCellValueFactory(new PropertyValueFactory<>("idPlayer1"));
			// Second Player Column
			TableColumn<TennisMatch, String> secondPlayerCol = new TableColumn<TennisMatch, String>("Second Player");
			secondPlayerCol.setCellValueFactory(new PropertyValueFactory<>("idPlayer2"));
			// Location Column
			TableColumn<TennisMatch, String> tournamentCol = new TableColumn<TennisMatch, String>("Tournament");
			tournamentCol.setCellValueFactory(new PropertyValueFactory<>("tournament"));
			// Score Column
			TableColumn<TennisMatch, String> scoreCol = new TableColumn<TennisMatch, String>("Score");
			scoreCol.setCellValueFactory(new PropertyValueFactory<>("matchScore"));
			// Adding all columns
			matchesTable.getColumns().addAll(dateCol, firstPlayerCol, secondPlayerCol, tournamentCol, scoreCol);
			matchesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			// Adding matchTableLbl & matchesTable to VBox
			VBox matchVBox = new VBox(matchTableLbl, matchesTable);
			matchVBox.setPadding(new Insets(0, 2.5, 5, 0));
			VBox.setVgrow(matchTableLbl, Priority.ALWAYS);
			VBox.setVgrow(matchesTable, Priority.ALWAYS);

			// HBox at Center BorderPane
			HBox centreHBox = new HBox(playerVBox, matchVBox);
			// Adding both VBox to HBox with is then added to BorderPane Center
			HBox.setHgrow(playerVBox, Priority.ALWAYS);
			HBox.setHgrow(matchVBox, Priority.ALWAYS);
			root.setCenter(centreHBox);

			// Add Player TextFields and buttons
			Label insertPlayerLbl = new Label("Insert Player");
			TextField idTxt = new TextField();
			idTxt.setPromptText("Player ID");
			idTxt.setTooltip(new Tooltip("Insert Player ID"));
			TextField firstNameTxt = new TextField();
			firstNameTxt.setPromptText("First Name");
			firstNameTxt.setTooltip(new Tooltip("Insert Player's First Name"));
			TextField lastNameTxt = new TextField();
			lastNameTxt.setPromptText("Last Name");
			lastNameTxt.setTooltip(new Tooltip("Insert Player's Last Name"));
			TextField birthYearTxt = new TextField();
			birthYearTxt.setPromptText("Birth Year");
			birthYearTxt.setTooltip(new Tooltip("Birth Year"));
			birthYearTxt.textProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
				if (!newValue.matches("\\d*")) {
					// If user enters anything besides integer. It gets removes
					birthYearTxt.setText(newValue.replaceAll("[^\\d]", ""));
				}
			});
			TextField countryTxt = new TextField();
			countryTxt.setPromptText("Country");
			countryTxt.setTooltip(new Tooltip("Insert Player's Country"));
			Button addPlayer = new Button("Add Player");
			addPlayer.setTooltip(new Tooltip("Insert Player in Player's Table"));
			addPlayer.setOnAction(e -> {
				// Checks if data is missing
				boolean dataMissing = checkTextFieldsData(idTxt, firstNameTxt, lastNameTxt, birthYearTxt, countryTxt);
				if (!dataMissing) {
					try {
						// Tries to insert data and displays message as well as clears TextFields
						tdb.insertPlayer(idTxt.getText(), firstNameTxt.getText(), lastNameTxt.getText(),
								Integer.parseInt(birthYearTxt.getText()), countryTxt.getText());
						updateTennisPlayerTable();
						displayNormalMessageInMessageBox(idTxt.getText() + " inserted successfully");
						clearAllTextFields(idTxt, firstNameTxt, lastNameTxt, birthYearTxt, countryTxt);
					} catch (NumberFormatException | TennisDatabaseException e1) {
						// If insertion fails
						displayErrorInMessageBox("Failed to insert " + idTxt.getText());
					}
				}
			});

			// Delete Player TextFields and buttons
			Label deletePlayerLbl = new Label("Delete Player");
			TextField deleteIDTxt = new TextField();
			deleteIDTxt.setPromptText("Player ID");
			deleteIDTxt.setTooltip(new Tooltip("Player ID to be deleted"));
			Button deletePlayer = new Button("Delete Player");
			deletePlayer.setTooltip(new Tooltip("Deletes Player from Player Table"));
			deletePlayer.setOnAction(e -> {
				boolean dataMissing = checkTextFieldsData(deleteIDTxt);
				if (dataMissing) {
					displayErrorInMessageBox("Please enter player ID to delete player!");
				} else {
					try {
						// Tries to delete player but gives an error message if deletion failed
						tdb.deletePlayer(deleteIDTxt.getText());
						displayNormalMessageInMessageBox(deleteIDTxt.getText() + " successfully deleted!");
						updateTennisPlayerTable();
						updateTennisMatchesTable();
						clearAllTextFields(deleteIDTxt);
					} catch (TennisDatabaseRuntimeException e1) {
						displayErrorInMessageBox("Error: Could not delete " + deleteIDTxt.getText());
					}
				}
			});

			// Add Match TextFields and buttons
			Label addMatchLbl = new Label("Add Match");
			DatePicker matchDateSeletor = new DatePicker();
			matchDateSeletor.setTooltip(new Tooltip("Select Match Date"));
			matchDateSeletor.setPromptText("MM/DD/YYYY");
			matchDateSeletor.setEditable(false);
			TextField player1IdTxt = new TextField();
			player1IdTxt.setPromptText("Player 1 ID");
			player1IdTxt.setTooltip(new Tooltip("ID of Player 1"));
			TextField player2IdTxt = new TextField();
			player2IdTxt.setPromptText("Player 2 ID");
			player2IdTxt.setTooltip(new Tooltip("ID of Player 2"));
			TextField tournamentTxt = new TextField();
			tournamentTxt.setPromptText("Tournament");
			tournamentTxt.setTooltip(new Tooltip("Location of Match"));
			TextField scoreTxt = new TextField();
			scoreTxt.setPromptText("Score");
			scoreTxt.setTooltip(new Tooltip("Score of match"));
			Button addMatch = new Button("Add a match");
			addMatch.setTooltip(new Tooltip("Adds match in Tennis Match table"));
			addMatch.setOnAction(e -> {
				// Checks for missing data
				boolean dataMissing = checkTextFieldsData(player1IdTxt, player2IdTxt, tournamentTxt, scoreTxt);
				LocalDate matchDate = matchDateSeletor.getValue();
				if (matchDate == null) {
					// If users forgot to enter date
					matchDateSeletor.getStylesheets().clear();
					matchDateSeletor.getStylesheets().add(this.getClass().getResource("warning.css").toExternalForm());
					displayErrorInMessageBox("Please enter missing information!");
				}
				if (!dataMissing && matchDateSeletor.getValue() != null) {
					// clears error message since correct date is inserted
					matchDateSeletor.getStylesheets().clear();
					matchDateSeletor.getStylesheets()
							.add(this.getClass().getResource("application.css").toExternalForm());
					try {
						// Tries to insert match and gives errors message if insertion fails
						tdb.insertMatch(player1IdTxt.getText(), player2IdTxt.getText(), matchDate.getYear(),
								matchDate.getMonthValue(), matchDate.getDayOfMonth(), tournamentTxt.getText(),
								scoreTxt.getText());
						updateTennisPlayerTable();
						updateTennisMatchesTable();
						clearAllTextFields(player1IdTxt, player2IdTxt, tournamentTxt, scoreTxt);
						matchDateSeletor.setValue(null);
						displayNormalMessageInMessageBox("Tennis Match Inserted successfully!");
					} catch (TennisDatabaseException | TennisDatabaseRuntimeException e1) {
						displayErrorInMessageBox("Could not insert Tennis Match");
					}
				}
			});

			// GridPane
			// Adds all buttons and textFields from add player, delete player and add match
			// to a grid pane
			GridPane grid = new GridPane();
			grid.setPadding(new Insets(0, 0, 4, 4));
			grid.setVgap(10);
			grid.setHgap(20);
			GridPane.setConstraints(insertPlayerLbl, 0, 0);
			GridPane.setConstraints(idTxt, 0, 1);
			GridPane.setConstraints(firstNameTxt, 1, 1);
			GridPane.setConstraints(lastNameTxt, 2, 1);
			GridPane.setConstraints(birthYearTxt, 0, 2);
			GridPane.setConstraints(countryTxt, 1, 2);
			GridPane.setConstraints(addPlayer, 2, 2);
			GridPane.setConstraints(deletePlayerLbl, 0, 3);
			GridPane.setConstraints(deleteIDTxt, 0, 4);
			GridPane.setConstraints(deletePlayer, 1, 4);
			GridPane.setConstraints(addMatchLbl, 0, 5);
			GridPane.setConstraints(matchDateSeletor, 0, 6);
			GridPane.setConstraints(player1IdTxt, 1, 6);
			GridPane.setConstraints(player2IdTxt, 2, 6);
			GridPane.setConstraints(tournamentTxt, 0, 7);
			GridPane.setConstraints(scoreTxt, 1, 7);
			GridPane.setConstraints(addMatch, 2, 7);
			grid.getChildren().addAll(insertPlayerLbl, idTxt, firstNameTxt, lastNameTxt, birthYearTxt, countryTxt,
					addPlayer, deletePlayerLbl, deleteIDTxt, deletePlayer, addMatchLbl, matchDateSeletor, player1IdTxt,
					player2IdTxt, tournamentTxt, scoreTxt, addMatch);
			// Message box displays message and errors in case an operation fails
			messageBox = new Label("Assignment 2 - Muhammad Suleman");
			messageBox.setPadding(new Insets(4, 0, 6, 4));
			VBox bottomVBox = new VBox(grid, messageBox);
			root.setBottom(bottomVBox);

			Scene scene = new Scene(root, 900, 675);
			scene.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Assignment 2");
			primaryStage.getIcons().add(new Image("file:icon.png"));
			primaryStage.show();
		} catch (Exception e) {
			displayErrorInMessageBox("Error initializing UI");
		}
	}

	/*
	 * Checks if any of the textFields have no data or are blank If the textFields
	 * are blanks it return trues ands sets red border around textFields with
	 * missing data as well as displays message
	 */
	private boolean checkTextFieldsData(TextField... textFields) {
		boolean dataMissing = false;
		for (TextField field : textFields) {
			if (field.getText().isBlank()) {
				field.getStylesheets().clear();
				field.getStylesheets().add(this.getClass().getResource("warning.css").toExternalForm());
				dataMissing = true;
			} else {
				field.getStylesheets().clear();
				field.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
			}
		}
		if (dataMissing) {
			// Displays me
			displayErrorInMessageBox("Please enter missing information!");
		} else {
			clearMessageBox();
		}
		return dataMissing;
	}

	/*
	 * Clears all data from textFields after successful operation
	 */
	private void clearAllTextFields(TextField... textFields) {
		for (TextField field : textFields) {
			field.setText("");
		}
	}

	/*
	 * Listens to the button action of load and export file and performs the
	 * respective operation
	 */
	EventHandler<ActionEvent> myHandler = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			// Gets the name of button pressed
			String buttonName = ((Button) event.getSource()).getText();
			// Creates a FileChooser with default directory is where user executed the program
			FileChooser chooser = new FileChooser();
			chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
			// Limits files to text files
			FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt");
			chooser.getExtensionFilters().add(fileExtensions);
			File file = null;
			Node source = (Node) event.getSource();
			// Loads the user selected file in program
			if (buttonName.equals("Load file")) {
				chooser.setTitle("Load file");
				boolean errorInFile = false;
				file = chooser.showOpenDialog(source.getScene().getWindow());
				if (file != null) {
					try {
						tdb = new TennisDatabase();
						tdb.loadFromFile(file.getAbsolutePath());
					} catch (TennisDatabaseRuntimeException | TennisDatabaseException e) {
						errorInFile = true;
					}
					// Updates tables
					updateTennisPlayerTable();
					updateTennisMatchesTable();
					// If files has error displays message
					if (errorInFile) {
						displayErrorInMessageBox("Error reading " + file.getName() + ". Data might be incomplete!");
					} else {
						displayNormalMessageInMessageBox(file.getName() + " loaded successfully!");
					}
				}
			} else {
				chooser.setTitle("Save file");
				file = chooser.showSaveDialog(source.getScene().getWindow());
				// Save the data to user specified file but gives error if save operation fails
				if (file != null) {
					try {
						tdb.saveToFile(file.getAbsolutePath());
						displayNormalMessageInMessageBox("Data was exported to " + file.getName() + " successfully!");
					} catch (TennisDatabaseException e) {
						displayErrorInMessageBox("Could not save to file!");
					}
				}
			}
		}
	};

	/*
	 * Gets all matches from tennis player container and overrides the table view
	 * with new values
	 */
	private void updateTennisPlayerTable() {
		TennisPlayer[] allPlayers = null;
		try {
			allPlayers = tdb.getAllPlayers();
		} catch (TennisDatabaseRuntimeException e) {
		}
		playerTable.setItems(FXCollections.emptyObservableList());
		if (allPlayers != null)
			playerTable.setItems(FXCollections.observableArrayList(allPlayers));
		playerTable.refresh();
	}

	/*
	 * Gets all matches from tennis match container and overrides the table view
	 * with new values
	 */
	private void updateTennisMatchesTable() {
		TennisMatch[] tennisMatches = null;
		try {
			tennisMatches = tdb.getAllMatches();
		} catch (TennisDatabaseRuntimeException e) {
		}
		matchesTable.setItems(FXCollections.emptyObservableList());
		if (tennisMatches != null)
			matchesTable.setItems(FXCollections.observableArrayList(tennisMatches));
		matchesTable.refresh();
	}

	/*
	 * If a operation is successful displays message in red text
	 */
	private void displayErrorInMessageBox(String message) {
		messageBox.setText(message);
		messageBox.getStyleClass().clear();
		messageBox.getStylesheets().add(getClass().getResource("warning.css").toExternalForm());
		messageBox.getStyleClass().add("warningText");
	}

	/*
	 * If a operation is successful displays message in green text
	 */
	private void displayNormalMessageInMessageBox(String message) {
		messageBox.setText(message);
		messageBox.getStyleClass().clear();
		messageBox.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		messageBox.getStyleClass().add("messageBox");
	}

	/*
	 * Clears message from messageBox
	 */
	private void clearMessageBox() {
		messageBox.setText("");
	}

	public static void main(String[] args) {
		launch(args);
	}
}
