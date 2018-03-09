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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class YelpSearchInterfaceController {

	// -----------Database Connection
	private Connection connection;

	// -------------Business---------//
	private boolean businessTabbool;

	@FXML
	private TextArea businessQueryText;

	@FXML
	private AnchorPane categoryPane;

	@FXML
	private AnchorPane subCategoryPane;
	
	@FXML
	private Button clearBut;

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
	@FXML
	private Text reviewsByText;
	@FXML
	private TableView<Reviews> userReviewTable;

	// Samples
	private ObservableList<YelpID> data = FXCollections.observableArrayList();
	private ObservableList<Reviews> reviewData = FXCollections.observableArrayList();

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
	public void OnBusinessTabClicled() {
		if (businessTabbool == false) {
			businessTabbool = true;
			AddCheckCategoryBoxes();
		}
	}

	/**
	 * Adds Category
	 */
	public void AddCheckCategoryBoxes() {

		String sqlQuery = "SELECT * " + "FROM BusinessCategory";

		// Execute Select SQL Statement
		Statement stmt;
		try {
			stmt = connection.createStatement();

			System.out.println(sqlQuery);
			ResultSet r = stmt.executeQuery(sqlQuery); // Inserts the query into ShowQuery
			ResultSetMetaData meta = r.getMetaData();
			int i = 0;
			while (r.next()) {
				CheckBox test = new CheckBox();
				test = new CheckBox();
				test.selectedProperty().addListener(new ChangeListener<Boolean>() {
					@Override
					public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
						// TODO Auto-generated method stub
						CategorySelected();
					}
				});
				test.setLayoutY(i);
				test.setText(r.getString("categoryName"));
				categoryPane.getChildren().add(test);
				i = i + 20;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// categoryPane.getChildren().add(test);
		// test.add
	}

	/**
	 * Check Category
	 */
	public void CategorySelected() {

		List<String> mainCategoryList = new ArrayList<String>();

		// Go through every children in
		for (int i = 0; i < categoryPane.getChildren().size(); i++) {
			CheckBox temp = (CheckBox) categoryPane.getChildren().get(i);
			if (temp.isSelected()) {
				mainCategoryList.add(temp.getText());
			}
		}
		AddSubCategories(mainCategoryList);
	}

	/**
	 * Adds SubCategory
	 */
	public void AddSubCategories(List<String> mainCategoryList) {
		// Clear
		subCategoryPane.getChildren().clear();

		String sqlQuery = "";

		for (int i = 0; i < mainCategoryList.size(); i++) {
			if (i == 0) {
				sqlQuery += "SELECT subName "
						+ "FROM Subcategory "
						+ "WHERE mainCategory = '" + mainCategoryList.get(i) + "'";
			} else {
				sqlQuery += " AND subName in("
						+ "SELECT subName "
						+ "FROM Subcategory "
						+ "WHERE mainCategory = '" + mainCategoryList.get(i) + "'";
			}
		}
		for(int i = 1 ; i <mainCategoryList.size(); i++) {
			sqlQuery += ")";
		}

		System.out.println(sqlQuery);
		businessQueryText.setText(sqlQuery);
		
		// Execute Select SQL Statement
		Statement stmt;
		try {
			if (mainCategoryList.size() > 0) {
				stmt = connection.createStatement();

				System.out.println(sqlQuery);
				ResultSet r = stmt.executeQuery(sqlQuery); // Inserts the query into ShowQuery
				ResultSetMetaData meta = r.getMetaData();
				int i = 0;
				while (r.next()) {
					CheckBox test = new CheckBox();
					test = new CheckBox();
					test.selectedProperty().addListener(new ChangeListener<Boolean>() {
						@Override
						public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
							// TODO Auto-generated method stub
							// CategorySelected();
						}
					});
					test.setLayoutY(i);
					test.setText(r.getString("subName"));
					subCategoryPane.getChildren().add(test);
					i = i + 20;
				}
				System.out.println(i);
				subCategoryPane.setPrefSize(50, i);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void SetDefault() {
		// Go through every children in
				for (int i = 0; i < categoryPane.getChildren().size(); i++) {
					CheckBox temp = (CheckBox) categoryPane.getChildren().get(i);
					temp.setSelected(false);
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
			YelpID test = new YelpID(r.getString("userid"), r.getString("name"), r.getDate("yelpingSince").toString(),
					r.getInt("reviewsCount"), r.getInt("friendCount"), r.getInt("averageStars"));
			data.add(test);
		}

		AddRowUserTable();
	}

	public void ClearRowsUserTable() {
		// Removes all column to allow for new Query
		userTable.getColumns().clear();
		userReviewTable.getColumns().clear();
		data = FXCollections.observableArrayList();
		reviewData = FXCollections.observableArrayList();
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
		userTable.getColumns().addAll(nameCol, userIDCol, memberSinceCol, reviewsCountCol, friendCountCol, avgStarsCol);
	}

	public void OnUserRowClicked() {
		// System.out.println("Clicked");
		System.out.println(userTable.getSelectionModel().getSelectedItem().toString());
		YelpID yelpTest = userTable.getSelectionModel().getSelectedItem();
		System.out.println(yelpTest.getUserID());
		System.out.println(userTable.getSelectionModel().getSelectedCells());
		reviewsByText.setText("Reviews by User: " + yelpTest.getName() + "(" + yelpTest.getUserID().trim() + ")");
		try {
			AddRowsReviews(yelpTest.getUserID());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void AddRowsReviews(String userID) throws SQLException {
		userReviewTable.getColumns().clear();
		reviewData = FXCollections.observableArrayList();

		userID = userID.trim(); // Removes whitespace
		System.out.println(userID);
		String sqlQuery = "SELECT * " + "FROM Reviews WHERE userID = '" + userID + "'";

		// Execute Select SQL Statement
		Statement stmt = connection.createStatement();
		System.out.println(sqlQuery);
		ResultSet r = stmt.executeQuery(sqlQuery); // Inserts the query into ShowQuery
		ResultSetMetaData meta = r.getMetaData();

		while (r.next()) {
			System.out.println("Stars " + r.getInt("stars"));
			System.out.println("Stars " + r.getString("stars"));
			Reviews test = new Reviews(r.getString("userid"), r.getString("reviewsID"),
					r.getDate("reviewDate").toString(), r.getString("businessID"), r.getLong("stars"),
					r.getLong("voteCount"));
			reviewData.add(test);
		}
		AddUserReviewsTable();
	}

	public void AddUserReviewsTable() {
		System.out.println("Columns added");

		// Name
		TableColumn reviewIDCol = new TableColumn("Review ID");
		reviewIDCol.setMinWidth(150);
		reviewIDCol.setCellValueFactory(new PropertyValueFactory<Reviews, String>("reviewsID"));
		// UserID
		TableColumn reviewDateCol = new TableColumn("Review Date");
		reviewDateCol.setMinWidth(100);
		reviewDateCol.setCellValueFactory(new PropertyValueFactory<Reviews, String>("reviewDate"));
		// MembersSince
		TableColumn businessIDCol = new TableColumn("Business ID");
		businessIDCol.setMinWidth(100);
		businessIDCol.setCellValueFactory(new PropertyValueFactory<Reviews, String>("businessID"));
		// ReviewsCount
		TableColumn starsCol = new TableColumn("Stars");
		starsCol.setMinWidth(20);
		starsCol.setCellValueFactory(new PropertyValueFactory<Reviews, Float>("stars"));
		// FriendCount
		TableColumn voteCol = new TableColumn("Vote Count");
		voteCol.setMinWidth(20);
		voteCol.setCellValueFactory(new PropertyValueFactory<Reviews, Float>("voteCount"));

		userReviewTable.setItems(reviewData);
		userReviewTable.getColumns().addAll(reviewIDCol, reviewDateCol, businessIDCol, starsCol, voteCol);
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

	public static class Reviews {
		private final SimpleStringProperty userID;
		private final SimpleStringProperty reviewsID;
		private final SimpleStringProperty reviewDate;
		private final SimpleStringProperty businessID;
		private final SimpleFloatProperty stars;
		private final SimpleFloatProperty voteCount;

		private Reviews(String newUserID, String newReviewsID, String date, String newBusinessID, float newStars,
				float newVoteCount) {

			this.userID = new SimpleStringProperty(newUserID);
			this.reviewsID = new SimpleStringProperty(newReviewsID);
			this.reviewDate = new SimpleStringProperty(date);
			this.businessID = new SimpleStringProperty(newBusinessID);
			this.stars = new SimpleFloatProperty(newStars);
			this.voteCount = new SimpleFloatProperty(newVoteCount);
			System.out.println("Star count" + stars);
			System.out.println("Vote count" + voteCount);
		}

		//// User
		public String getUserID() {
			return userID.get();
		}

		public void setUserID(String fName) {
			userID.set(fName);
		}

		// ReviewsID
		public String getReviewsID() {
			return reviewsID.get();
		}

		public void setReviewsID(String fName) {
			reviewsID.set(fName);
		}

		// ReviewDate
		public String getReviewDate() {
			return reviewDate.get();
		}

		public void setReviewDate(String fName) {
			reviewDate.set(fName);
		}

		// BusinessID
		public String getBusinessID() {
			return businessID.get();
		}

		public void setBusinessID(String newReviewsCount) {
			businessID.set(newReviewsCount);
		}

		// Stars
		public Float getStars() {
			return stars.get();
		}

		public void setStars(float newReviewsCount) {
			stars.set(newReviewsCount);
		}

		// Votes
		public Float getVoteCount() {
			return voteCount.get();
		}

		public void setVoteCount(int newReviewsCount) {
			voteCount.set(newReviewsCount);
		}
	}
}
