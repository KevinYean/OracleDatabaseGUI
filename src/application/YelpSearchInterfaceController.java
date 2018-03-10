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
import javafx.scene.control.ComboBox;
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
	@FXML
	private Button queryReview;
	@FXML
	private TableView<Business> businessTableView;
	@FXML
	private TableView<Reviews> businessReviewTableView;
	@FXML
	private Text reviewsByBusiness;
	@FXML
	private ComboBox From;
	@FXML
	private ComboBox FromHours;
	@FXML
	private ComboBox To;
	@FXML
	private ComboBox ToHours;
	@FXML
	private DatePicker fromDate;
	@FXML
	private DatePicker toDate;
	@FXML
	private ChoiceBox starsBusinessBox;
	@FXML
	private TextField starsBusinessCount;
	@FXML
	private ChoiceBox votesBusinessBox;
	@FXML
	private TextField votesBusinessCount;
	@FXML
	private ChoiceBox numbCheckinBox;
	@FXML
	private TextField numbCheckinField;

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
	private ObservableList<Business> businessData = FXCollections.observableArrayList();
	private ObservableList<Reviews> businessReviewData = FXCollections.observableArrayList();

	private int fromDay = -1;
	private int fromHours = -1;
	private int toDay = -1;
	private int toHours = -1;

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

	public void CheckBusinessCheckin() {
		// ReadValues from From ComboBox
		if (From.getValue() != null) {
			switch (From.getValue().toString()) {
			case "Monday":
				fromDay = 1;
				break;
			case "Tuesday":
				fromDay = 2;
				break;
			case "Wednesday":
				fromDay = 3;
				break;
			case "Thursday":
				fromDay = 4;
				break;
			case "Friday":
				fromDay = 5;
				break;
			case "Saturday":
				fromDay = 6;
				break;
			case "Sunday":
				fromDay = 0;
				break;
			}
		}

		// ReadValues from From ComboBox
		if (FromHours.getValue() != null) {
			switch (FromHours.getValue().toString()) {
			case "00:00-01:00":
				fromHours = 0;
				break;
			case "01:00-02:00":
				fromHours = 1;
				break;
			case "02:00-03:00":
				fromHours = 2;
				break;
			case "03:00-04:00":
				fromHours = 3;
				break;
			case "04:00-05:00":
				fromHours = 4;
				break;
			case "05:00-06:00":
				fromHours = 5;
				break;
			case "06:00-07:00":
				fromHours = 6;
				break;
			case "07:00-08:00":
				fromHours = 7;
				break;
			case "08:00-09:00":
				fromHours = 8;
				break;
			case "09:00-10:00":
				fromHours = 9;
				break;
			case "10:00-11:00":
				fromHours = 10;
				break;
			case "11:00-12:00":
				fromHours = 11;
				break;
			case "12:00-13:00":
				fromHours = 12;
				break;
			case "13:00-14:00":
				fromHours = 13;
				break;
			case "14:00-15:00":
				fromHours = 14;
				break;
			case "15:00-16:00":
				fromHours = 15;
				break;
			case "16:00-17:00":
				fromHours = 16;
				break;
			case "17:00-18:00":
				fromHours = 17;
				break;
			case "18:00-19:00":
				fromHours = 18;
				break;
			case "19:00-20:00":
				fromHours = 19;
				break;
			case "20:00-21:00":
				fromHours = 20;
				break;
			case "21:00-22:00":
				fromHours = 21;
				break;
			case "22:00-23:00":
				fromHours = 22;
				break;
			case "23:00-00:00":
				fromHours = 23;
				break;
			}
		}

		// ReadValues from From ComboBox
		if (To.getValue() != null) {
			switch (To.getValue().toString()) {
			case "Monday":
				toDay = 1;
				break;
			case "Tuesday":
				toDay = 2;
				break;
			case "Wednesday":
				toDay = 3;
				break;
			case "Thursday":
				toDay = 4;
				break;
			case "Friday":
				toDay = 5;
				break;
			case "Saturday":
				toDay = 6;
				break;
			case "Sunday":
				toDay = 0;
				break;
			}
		}

		// ReadValues from From ComboBox
		if (ToHours.getValue() != null) {
			switch (ToHours.getValue().toString()) {
			case "00:00-01:00":
				toHours = 0;
				break;
			case "01:00-02:00":
				toHours = 1;
				break;
			case "02:00-03:00":
				toHours = 2;
				break;
			case "03:00-04:00":
				toHours = 3;
				break;
			case "04:00-05:00":
				toHours = 4;
				break;
			case "05:00-06:00":
				toHours = 5;
				break;
			case "06:00-07:00":
				toHours = 6;
				break;
			case "07:00-08:00":
				toHours = 7;
				break;
			case "08:00-09:00":
				toHours = 8;
				break;
			case "09:00-10:00":
				toHours = 9;
				break;
			case "10:00-11:00":
				toHours = 10;
				break;
			case "11:00-12:00":
				toHours = 11;
				break;
			case "12:00-13:00":
				toHours = 12;
				break;
			case "13:00-14:00":
				toHours = 13;
				break;
			case "14:00-15:00":
				toHours = 14;
				break;
			case "15:00-16:00":
				toHours = 15;
				break;
			case "16:00-17:00":
				toHours = 16;
				break;
			case "17:00-18:00":
				toHours = 17;
				break;
			case "18:00-19:00":
				toHours = 18;
				break;
			case "19:00-20:00":
				toHours = 19;
				break;
			case "20:00-21:00":
				toHours = 20;
				break;
			case "21:00-22:00":
				toHours = 21;
				break;
			case "22:00-23:00":
				toHours = 22;
				break;
			case "23:00-00:00":
				toHours = 23;
				break;
			}
		}
		System.out.println("FromDay " + fromDay);
		System.out.println("FromHours " + fromHours);
		System.out.println("ToDay " + toDay);
		System.out.println("ToHours " + toHours);
	}

	public void OnClickBusinessSearchButton(ActionEvent event) throws SQLException, ParseException {

		ClearBusinessUserTable();

		// Query

		// -------------Dates---------------/
		// For Dates
		String businessQueryBeg = "SELECT * " + "FROM BUSINESS " + "WHERE businessID in " + "(SELECT businessID "
				+ "FROM REVIEWS " + "WHERE reviewsID in (";
		String totalWhereQuery = "";
		String businessQueryEnd = "))";
		int dateSelected = 0;

		if (fromDate.getValue() != null) {
			dateSelected++;
			String date = fromDate.getValue().toString();
			totalWhereQuery += "SELECT reviewsID " + "FROM Reviews  " + "WHERE reviewDate > TO_DATE('" + date
					+ "','yyyy-mm-dd')";
		}

		if (toDate.getValue() != null) {
			String date = toDate.getValue().toString();
			if (dateSelected != 0) {
				totalWhereQuery += " INTERSECT ";

			}
			totalWhereQuery += "SELECT reviewsID " + "FROM Reviews  " + "WHERE reviewDate < TO_DATE('" + date
					+ "','yyyy-mm-dd')";
			dateSelected++;
		}

		if (!starsBusinessCount.getText().trim().isEmpty()) {
			String operator = starsBusinessBox.getValue().toString();
			String starValue = starsBusinessCount.getText();

			if (dateSelected != 0) {
				totalWhereQuery += " INTERSECT ";
			}
			totalWhereQuery += "SELECT reviewsID " + "FROM Reviews  " + "WHERE stars " + operator + " '" + starValue
					+ "' ";

			dateSelected++;
		}

		if (!votesBusinessCount.getText().trim().isEmpty()) {
			System.out.println("HERE");
			String operator = votesBusinessBox.getValue().toString();
			String voteValue = votesBusinessCount.getText();

			if (dateSelected != 0) {
				totalWhereQuery += " INTERSECT ";
			}
			totalWhereQuery += "SELECT reviewsID " + "FROM Reviews  " + "WHERE voteCount " + operator + " '" + voteValue
					+ "' ";

			dateSelected++;
		}

		// ------------Checkin-------------------/
		CheckBusinessCheckin();
		// For Dates
		String businessCheckBeg = "SELECT * " + "FROM BUSINESS " + "WHERE BusinessID in (SELECT businessID FROM (";
		String totalCheckQuery = " "; // Beginning
		String businessCheckEnd = ") Group by businessID )";
		int checkSelected = 0;

		// From Day
		if (fromDay != -1) {
			if (checkSelected == 0) {
				totalCheckQuery += " SELECT * " + "FROM CHECKIN " + "WHERE day >= " + fromDay;
			} else {
				totalCheckQuery += "SELECT * " + "FROM Checkin " + "WHERE day >= " + fromDay;
			}
			checkSelected++;
		}

		// From Hours
		if (fromHours != -1) {
			if (checkSelected != 0 && fromDay != -1) {
				totalCheckQuery += " AND hours >= " + fromHours;
			} else {
				totalCheckQuery += "SELECT * " + "FROM Checkin " + "WHERE hours >= " + fromHours;
			}
			checkSelected++;
		}

		// To Day
		if (toDay != -1) {
			if (fromDay != -1 || fromHours != -1) {
				totalCheckQuery += "";
			}

			if (checkSelected == 0) {
				totalCheckQuery += " SELECT * " + "FROM CHECKIN " + "WHERE day <= " + toDay;
			} else {
				totalCheckQuery += " AND day <= " + toDay;
			}
			checkSelected++;
		}

		// To hours
		if (toHours != -1) {
			if (toDay == -1 && (fromHours != -1 || fromDay != -1)) {
				totalCheckQuery += "  ";
			}
			if (checkSelected == 0) {
				totalCheckQuery += "SELECT * " + "FROM Checkin " + "WHERE hours <= " + toHours;
			} else {
				totalCheckQuery += " AND hours <= " + toHours;
			}
			checkSelected++;
		}

		if (!numbCheckinField.getText().trim().isEmpty()) {
			businessCheckBeg = "SELECT * FROM business where businessID in (SELECT businessID " + "FROM  " + "(SELECT sum(checkCount) as totalCheck, businessID FROM( ";
			String operator = numbCheckinBox.getValue().toString();
			String numbCheck = numbCheckinField.getText();
			if (checkSelected == 0) {

				totalCheckQuery += "SELECT *" + "	FROM CHECKIN";

				checkSelected++;
				
			}
			businessCheckEnd = ") Group by businessID ) WHERE totalCheck " + operator + " " + numbCheck +")";
		}

		// -------------Main----------/
		// For Main
		String businessMainQuery = "";

		// If Main Category is empty
		List<String> mainCategoryList = new ArrayList<String>();
		{

			// Go through every children in
			for (int i = 0; i < categoryPane.getChildren().size(); i++) {
				CheckBox temp = (CheckBox) categoryPane.getChildren().get(i);
				if (temp.isSelected()) {
					mainCategoryList.add(temp.getText());
				}
			}
			// Select All if no main Categories have been selected
			if (mainCategoryList.size() == 0) {
				businessMainQuery = "SELECT * FROM Business";
			}

		}

		// --Sub
		List<String> subCategoryList = new ArrayList<String>();
		{
			// Go through every subcategory names
			for (int i = 0; i < subCategoryPane.getChildren().size(); i++) {
				CheckBox temp = (CheckBox) subCategoryPane.getChildren().get(i);
				if (temp.isSelected()) {
					subCategoryList.add(temp.getText());
				}
			}

			for (int i = 0; i < subCategoryList.size(); i++) {
				if (i == 0) {
					businessMainQuery = "SELECT * FROM Business WHERE businessID in "
							+ "(SELECT businessID FROM BusinessWithCategory WHERE categoryName = '"
							+ subCategoryList.get(i) + "' ";
				} else {
					businessMainQuery += "AND businessID in "
							+ "(SELECT businessID FROM BusinessWithCategory WHERE categoryName = '"
							+ subCategoryList.get(i) + "' ";
				}
			}
			for (int i = 0; i < subCategoryList.size(); i++) {
				businessMainQuery += ")";
			}

			// Select all business that belong all main categories if no sub categories

			// been picked
			if (subCategoryList.size() == 0 && mainCategoryList.size() > 0) {
				for (int i = 0; i < mainCategoryList.size(); i++) {
					if (i == 0) {
						businessMainQuery = "SELECT * FROM Business WHERE businessID in "
								+ "(SELECT businessID FROM BusinessWithCategory WHERE categoryName = '"
								+ mainCategoryList.get(i) + "' ";
					} else {
						businessMainQuery += "AND businessID in "
								+ "(SELECT businessID FROM BusinessWithCategory WHERE categoryName = '"
								+ mainCategoryList.get(i) + "' ";
					}
				}
				for (int i = 0; i < mainCategoryList.size(); i++) {
					businessMainQuery += ")";
				}
			}
		}

		/*
		 * //Right brackets for(int i = 0 ; i < selected; i++){ totalWhereQuery +=")"; }
		 */

		// --Execute Query
		{
			// Execute Select SQL Statement
			Statement stmt;
			String totalQuery = "";
			if (dateSelected != 0) {
				totalQuery = businessQueryBeg + totalWhereQuery + businessQueryEnd;
				totalQuery += " INTERSECT ";
			}

			if (checkSelected != 0) {
				totalQuery += businessCheckBeg + totalCheckQuery + businessCheckEnd;
				totalQuery += " INTERSECT ";
			}

			totalQuery += businessMainQuery;

			try {
				stmt = connection.createStatement();
				System.out.println(totalQuery);
				ResultSet r = stmt.executeQuery(totalQuery); // Inserts the query into ShowQuery
				ResultSetMetaData meta = r.getMetaData();
				int i = 0;
				while (r.next()) {
					Business test = new Business(r.getString("businessID"), r.getString("city"), r.getString("state"),
							r.getInt("reviewsCount"), r.getString("name"), r.getInt("stars"));
					businessData.add(test);
					// System.out.println("From " + r.getDate("reviewDate").toString() + " Count " +
					// r.getInt("voteCount") + " Stars " + r.getInt("stars"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			businessQueryText.setText(totalQuery);
			AddBusinessTable();
		}
	}

	public void AddBusinessTable() {
		System.out.println("Columns added");

		// Name
		TableColumn nameCol = new TableColumn("Name");
		nameCol.setMinWidth(150);
		nameCol.setCellValueFactory(new PropertyValueFactory<Business, String>("name"));
		// City
		TableColumn cityCol = new TableColumn("City");
		cityCol.setMinWidth(100);
		cityCol.setCellValueFactory(new PropertyValueFactory<Business, String>("city"));
		// State
		TableColumn stateCol = new TableColumn("State");
		stateCol.setMinWidth(100);
		stateCol.setCellValueFactory(new PropertyValueFactory<Business, String>("state"));
		// ReviewsCount
		TableColumn reviewsCol = new TableColumn("Review Count");
		reviewsCol.setMinWidth(20);
		reviewsCol.setCellValueFactory(new PropertyValueFactory<Business, Float>("reviewsCount"));
		// StarsCol
		TableColumn starsCol = new TableColumn("Stars");
		starsCol.setMinWidth(20);
		starsCol.setCellValueFactory(new PropertyValueFactory<Business, Float>("stars"));
		// ID
		TableColumn idCol = new TableColumn("ID");
		idCol.setMinWidth(30);
		idCol.setCellValueFactory(new PropertyValueFactory<Business, Float>("businessID"));

		businessTableView.setItems(businessData);
		businessTableView.getColumns().addAll(nameCol, cityCol, stateCol, reviewsCol, starsCol, idCol);
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
				sqlQuery += "SELECT subName " + "FROM Subcategory " + "WHERE mainCategory = '" + mainCategoryList.get(i)
						+ "'";
			} else {
				sqlQuery += " AND subName in(" + "SELECT subName " + "FROM Subcategory " + "WHERE mainCategory = '"
						+ mainCategoryList.get(i) + "'";
			}
		}
		for (int i = 1; i < mainCategoryList.size(); i++) {
			sqlQuery += ")";
		}

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

	public void ClearBusinessUserTable() {
		// Removes all column to allow for new Query
		businessTableView.getColumns().clear();
		businessTableView.getColumns().clear();
		businessData = FXCollections.observableArrayList();
		// TimeUnit.MINUTES.sleep(1);
	}

	/**
	 * Reset all Default Buttons/Text/etc in BusinessTab.
	 */
	public void SetDefault() {
		// Go through every children in categoryPane
		for (int i = 0; i < categoryPane.getChildren().size(); i++) {
			CheckBox temp = (CheckBox) categoryPane.getChildren().get(i);
			temp.setSelected(false);
		}
		businessQueryText.setText(null);
		From.setValue(null);
		FromHours.setValue(null);
		To.setValue(null);
		ToHours.setValue(null);
		businessReviewTableView.getColumns().clear();
		businessReviewData = FXCollections.observableArrayList();
		businessTableView.getColumns().clear();
		fromDay = -1;
		fromHours = -1;
		toDay = -1;
		toHours = -1;

	}

	public void OnBusinessRowClicked() {
		System.out.println(businessTableView.getSelectionModel().getSelectedItem().toString());
		Business yelpBusiness = businessTableView.getSelectionModel().getSelectedItem();
		System.out.println(yelpBusiness.getBusinessID());
		System.out.println(businessTableView.getSelectionModel().getSelectedCells());
		reviewsByBusiness.setText(
				"Reviews for Business: " + yelpBusiness.getName() + "(" + yelpBusiness.getBusinessID().trim() + ")");
		try {
			AddRowBusinessReview(yelpBusiness.getBusinessID());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Adds Row Business
	 * 
	 * @param userID
	 * @throws SQLException
	 */
	public void AddRowBusinessReview(String businessID) throws SQLException {
		System.out.println("No Error");
		businessReviewTableView.getColumns().clear();
		businessReviewData = FXCollections.observableArrayList();

		businessID = businessID.trim(); // Removes whitespace
		System.out.println(businessID);
		String sqlQuery = "SELECT * " + "FROM Reviews WHERE businessID = '" + businessID + "'";

		// Execute Select SQL Statement
		Statement stmt = connection.createStatement();
		System.out.println(sqlQuery);
		ResultSet r = stmt.executeQuery(sqlQuery); // Inserts the query into ShowQuery
		ResultSetMetaData meta = r.getMetaData();

		while (r.next()) {
			// System.out.println("Stars " + r.getInt("stars"));
			// System.out.println("Stars " + r.getString("stars"));
			Statement stmt2 = connection.createStatement();
			String sqlQueryName = "SELECT name FROM YelpUser WHERE userID = '" + r.getString("userid").trim() + "'";
			ResultSet r2 = stmt2.executeQuery(sqlQueryName); // Inserts the query into ShowQuery
			r2.next();
			String username = r2.getString("name");
			stmt2.close();

			// System.out.println("Hey" + r2.getString(0));
			Reviews test = new Reviews(r.getString("userid"), r.getString("reviewsID"),
					r.getDate("reviewDate").toString(), r.getString("businessID"), r.getLong("stars"),
					r.getLong("voteCount"), username);
			businessReviewData.add(test);
		}
		AddBusinessReviewsTable();
	}

	public void AddBusinessReviewsTable() {
		System.out.println("Columns added");

		// MembersSince
		TableColumn userNameCol = new TableColumn("Reviewer Date");
		userNameCol.setMinWidth(100);
		userNameCol.setCellValueFactory(new PropertyValueFactory<Reviews, String>("userName"));
		// Name
		TableColumn reviewIDCol = new TableColumn("Review ID");
		reviewIDCol.setMinWidth(150);
		reviewIDCol.setCellValueFactory(new PropertyValueFactory<Reviews, String>("reviewsID"));
		// UserID
		TableColumn reviewDateCol = new TableColumn("Review Date");
		reviewDateCol.setMinWidth(100);
		reviewDateCol.setCellValueFactory(new PropertyValueFactory<Reviews, String>("reviewDate"));
		// ReviewsCount
		TableColumn starsCol = new TableColumn("Stars");
		starsCol.setMinWidth(20);
		starsCol.setCellValueFactory(new PropertyValueFactory<Reviews, Float>("stars"));
		// FriendCount
		TableColumn voteCol = new TableColumn("Vote Count");
		voteCol.setMinWidth(20);
		voteCol.setCellValueFactory(new PropertyValueFactory<Reviews, Float>("voteCount"));

		businessReviewTableView.setItems(businessReviewData);
		businessReviewTableView.getColumns().addAll(userNameCol, reviewIDCol, reviewDateCol, starsCol, voteCol);
	}

	// --------------------------------User--------------------------//

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
					r.getLong("voteCount"), "");
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
		private final SimpleStringProperty userName;

		private Reviews(String newUserID, String newReviewsID, String date, String newBusinessID, float newStars,
				float newVoteCount, String newUserName) {

			this.userID = new SimpleStringProperty(newUserID);
			this.reviewsID = new SimpleStringProperty(newReviewsID);
			this.reviewDate = new SimpleStringProperty(date);
			this.businessID = new SimpleStringProperty(newBusinessID);
			this.stars = new SimpleFloatProperty(newStars);
			this.voteCount = new SimpleFloatProperty(newVoteCount);
			this.userName = new SimpleStringProperty(newUserName);
		}

		//// UserName
		public String getUserName() {
			return userName.get();
		}

		public void setUserName(String fName) {
			userName.set(fName);
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

	public static class Business {
		private final SimpleStringProperty businessID;
		private final SimpleStringProperty city;
		private final SimpleStringProperty state;
		private final SimpleFloatProperty reviewsCount;
		private final SimpleStringProperty name;
		private final SimpleFloatProperty stars;

		private Business(String newBusinessID, String newCity, String newState, float reviewsCount, String newName,
				float newStars) {

			this.businessID = new SimpleStringProperty(newBusinessID);
			this.city = new SimpleStringProperty(newCity);
			this.state = new SimpleStringProperty(newState);
			this.reviewsCount = new SimpleFloatProperty(reviewsCount);
			this.name = new SimpleStringProperty(newName);
			this.stars = new SimpleFloatProperty(newStars);
		}

		// BusinessID
		public String getBusinessID() {
			return businessID.get();
		}

		public void setBusinessID(String newReviewsCount) {
			businessID.set(newReviewsCount);
		}

		// City
		public String getCity() {
			return city.get();
		}

		public void setCity(String fName) {
			city.set(fName);
		}

		// State
		public String getState() {
			return state.get();
		}

		public void setState(String fName) {
			state.set(fName);
		}

		// ReviewsCount
		public Float getReviewsCount() {
			return reviewsCount.get();
		}

		public void setReviewCount(float newReviewsCount) {
			reviewsCount.set(newReviewsCount);
		}

		// Name
		public String getName() {
			return name.get();
		}

		public void setName(String newReviewsCount) {
			name.set(newReviewsCount);
		}

		// Stars
		public Float getStars() {
			return stars.get();
		}

		public void setStars(float newReviewsCount) {
			stars.set(newReviewsCount);
		}

	}
}
