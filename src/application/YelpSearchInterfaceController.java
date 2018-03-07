package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class YelpSearchInterfaceController {

	// -----------Database Connection
	private Connection connection;

	// -------------Business---------//
	private boolean businessTabbool;

	@FXML
	private TextArea businessQueryText;

	///// ----------User------------//
	@FXML
	private Button userQueryButton;
	@FXML
	private Button userClearQueryButton;
	@FXML
	private TableView<YelpID> userTable;
	@FXML
	private TextArea userQueryText;
	@FXML
	private DatePicker userDatePicker;
	@FXML
	private TextField userNameTextField;
	@FXML
	private ChoiceBox reviewsChoiceBox;
	@FXML
	private TextField reviewsField;
	@FXML
	private ChoiceBox numberFriendsChoiceBox;
	@FXML
	private TextField numberFriendsField;
	@FXML
	private ChoiceBox avgStarsChoiceBox;
	@FXML
	private TextField avgStarsField;
	@FXML
	private ChoiceBox SelectChoiceBox;

	// Sample
	private ObservableList<YelpID> data = FXCollections.observableArrayList();

	public YelpSearchInterfaceController() {

		businessTabbool = false;

		connection = null;

		try {
			connection = OpenConnection();
			System.out.println("Connected to Database");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void DefaultValues() throws InterruptedException {
		// Sets default value for some objects.
		userDatePicker.setValue(LocalDate.now());

	}

	/**
	 * Method that opens up a connection and returns that session.
	 * 
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	private Connection OpenConnection() throws SQLException, ClassNotFoundException {
		// Load the Oracle database driver \
		DriverManager.registerDriver(new oracle.jdbc.OracleDriver());

		String host = "localhost";
		String port = "1521";
		String dbName = "kevin";
		String userName = "Scott";
		String password = "Tiger123";

		// Construct the JDBC URL
		String dbURL = "jdbc:oracle:thin:@" + host + ":" + port + ":" + dbName;
		return DriverManager.getConnection(dbURL, userName, password);
	}

	// -----------------------------BusinessSearch --------//
	public void OnTest() {
		if (businessTabbool == false) {
			System.out.println("Test");
			businessTabbool = true;
			// businessQueryText.setText("Kevin");
		}
	}

	// --------------------------------User----------------//

	/**
	 * Method executes the Query.
	 * 
	 * @param event
	 * @throws SQLException
	 * @throws ParseException
	 */
	public void OnClickUserSearchButton(ActionEvent event) throws SQLException, ParseException {
		ClearRowsUserTable();

		// Query
		boolean selection = false;
		String selectQuery = SelectChoiceBox.getValue().toString();
		String totalWhereQuery = "";

		// Name
		if (!userNameTextField.getText().trim().isEmpty()) {
			selection = true;
			String name = userNameTextField.getText();
			totalWhereQuery += "WHERE name = '" + name + "' ";
		}

		// Date
		if (userDatePicker.getValue() != null) {
			String date = userDatePicker.getValue().toString();
			if (selection) {
				totalWhereQuery += " " + selectQuery + " yelpingSince > TO_DATE('" + date + "','yyyy-mm-dd')";
			} else {
				selection = true;
				totalWhereQuery += "WHERE yelpingSince > TO_DATE('" + date + "','yyyy-mm-dd')";
			}
		}

		// Count
		if (!reviewsField.getText().trim().isEmpty()) {
			String countOperator = reviewsChoiceBox.getValue().toString();
			String countValue = reviewsField.getText().toString();
			if (selection) {
				totalWhereQuery += " " + selectQuery + " reviewsCount " + countOperator + " " + countValue + " ";
			} else {
				selection = true;
				totalWhereQuery += "WHERE reviewsCount " + countOperator + " " + countValue + " ";
			}
		}

		// Number of Friends
		if (!numberFriendsField.getText().trim().isEmpty()) {
			String countOperator = numberFriendsChoiceBox.getValue().toString();
			String countValue = numberFriendsField.getText().toString();
			if (selection) {
				totalWhereQuery += " " + selectQuery + " friendCount " + countOperator + " " + countValue + " ";
			} else {
				selection = true;
				totalWhereQuery += "WHERE friendCount " + countOperator + " " + countValue + " ";
			}
		}

		// Avg. Stars
		if (!avgStarsField.getText().trim().isEmpty()) {
			String countOperator = avgStarsChoiceBox.getValue().toString();
			String countValue = avgStarsField.getText().toString();
			if (selection) {
				totalWhereQuery += " " + selectQuery + " averageStars " + countOperator + " " + countValue + " ";
			} else {
				selection = true;
				totalWhereQuery += "WHERE averageStars " + countOperator + " " + countValue + " ";
			}
		}

		String sqlQuery = "SELECT * " + "FROM YelpUser " + totalWhereQuery;

		userQueryText.setText(sqlQuery);

		// Execute Select SQL Statement
		Statement stmt = connection.createStatement();
		System.out.println(sqlQuery);
		ResultSet r = stmt.executeQuery(sqlQuery); // Inserts the query into ShowQuery
		ResultSetMetaData meta = r.getMetaData();

		while (r.next()) {
			for (int col = 1; col <= meta.getColumnCount(); col++) {
			}
			YelpID test = new YelpID(r.getString("userid"), 
					r.getString("name"), 
					r.getDate("yelpingSince").toString(),
					r.getInt("reviewsCount"),
					r.getInt("friendCount"),
					r.getInt("averageStars"));
			data.add(test);
		}

		AddRowUserTable();
	}

	public void ClearRowsUserTable() {
		// Removes all column to allow for new Query
		userTable.getColumns().clear();
		data = FXCollections.observableArrayList();
		// TimeUnit.MINUTES.sleep(1);
	}

	public void AddRowUserTable() {
		System.out.println("Columns added");

		// Name
		TableColumn nameCol = new TableColumn("Name");
		nameCol.setMinWidth(150);
		nameCol.setCellValueFactory(new PropertyValueFactory<YelpID, String>("name"));
		// UserID
		TableColumn userIDCol = new TableColumn("UserID ");
		userIDCol.setMinWidth(100);
		userIDCol.setCellValueFactory(new PropertyValueFactory<YelpID, String>("userID"));
		// MembersSince
		TableColumn memberSinceCol = new TableColumn("Member Since");
		memberSinceCol.setMinWidth(200);
		memberSinceCol.setCellValueFactory(new PropertyValueFactory<YelpID, String>("yelpingSince"));
		// ReviewsCount
		TableColumn reviewsCountCol = new TableColumn("Reviews Count");
		reviewsCountCol.setMinWidth(20);
		reviewsCountCol.setCellValueFactory(new PropertyValueFactory<YelpID, Float>("reviewsCount"));
		// FriendCount
		TableColumn friendCountCol = new TableColumn("Friend Count");
		friendCountCol.setMinWidth(20);
		friendCountCol.setCellValueFactory(new PropertyValueFactory<YelpID, Float>("friendCount"));
		// AverageStars
		TableColumn avgStarsCol = new TableColumn("Average Stars");
		avgStarsCol.setMinWidth(20);
		avgStarsCol.setCellValueFactory(new PropertyValueFactory<YelpID, Float>("averageStars"));

		userTable.setItems(data);
		userTable.getColumns().addAll(nameCol, userIDCol, memberSinceCol, reviewsCountCol,friendCountCol,avgStarsCol);

	}

	public void OnUserRowClicked() {
		// System.out.println("Clicked");
		System.out.println(userTable.getSelectionModel().getSelectedItem().toString());
		YelpID yelpTest = userTable.getSelectionModel().getSelectedItem();
		System.out.println(yelpTest.getUserID());
	}

	// ------/
	public static class YelpID {

		private final SimpleStringProperty userID;
		private final SimpleStringProperty name;
		private final SimpleStringProperty yelpingSince;
		private final SimpleFloatProperty reviewsCount;
		private final SimpleFloatProperty friendCount;
		private final SimpleFloatProperty averageStars;

		private YelpID(String newUserID, String newName, String date, int newReviewsCount, int newFriendCount,
				int newAverageStars) {
			this.userID = new SimpleStringProperty(newUserID);
			this.name = new SimpleStringProperty(newName);
			this.yelpingSince = new SimpleStringProperty(date);
			this.reviewsCount = new SimpleFloatProperty(newReviewsCount);
			this.friendCount = new SimpleFloatProperty(newFriendCount);
			this.averageStars = new SimpleFloatProperty(newAverageStars);
		}

		public String getUserID() {
			return userID.get();
		}

		public void setUserID(String fName) {
			userID.set(fName);
		}

		public String getName() {
			return name.get();
		}

		public void setName(String fName) {
			name.set(fName);
		}

		public String getYelpingSince() {
			return yelpingSince.get();
		}

		public void setYelpingSince(String fName) {
			yelpingSince.set(fName);
		}

		public Float getReviewsCount() {
			return reviewsCount.get();
		}

		public void setReviewsCount(int newReviewsCount) {
			reviewsCount.set(newReviewsCount);
		}

		public Float getFriendCount() {
			return friendCount.get();
		}

		public void setFriendCountCount(int newReviewsCount) {
			friendCount.set(newReviewsCount);
		}

		public Float getAverageStars() {
			return averageStars.get();
		}

		public void setAverageStars(int newReviewsCount) {
			averageStars.set(newReviewsCount);
		}
	}

}
