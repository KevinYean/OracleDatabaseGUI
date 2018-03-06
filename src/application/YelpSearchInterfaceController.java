package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class YelpSearchInterfaceController {

	// -----------Database Connection
	private Connection connection;

	///// ----------User------------//
	@FXML
	private Button userQueryButton;
	@FXML
	private TableView userTable;

	// Sample
	private final ObservableList<YelpID> data = FXCollections.observableArrayList(
			new YelpID("Jacob", "Smith", "jacob.smith@example.com"),
			new YelpID("Isabella", "Johnson", "isabella.johnson@example.com"),
			new YelpID("Ethan", "Williams", "ethan.williams@example.com"),
			new YelpID("Emma", "Jones", "emma.jones@example.com"),
			new YelpID("Michael", "Brown", "michael.brown@example.com"));

	public YelpSearchInterfaceController() {
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

	public void OnClickUserSearchButton(ActionEvent event) throws SQLException {

		AddRowUserTable();
		System.out.println("Column added");

		/*
		 * 35 This will create a Statement that return ResultSets which is: 36 1.
		 * Scrollablle (can use ResultSet.previous() or ResultSet.absolute()) 37 2.
		 * Read-only (cannot call ResultSet.updateXXX() to change the content) 38
		 */

		// Sample Test
		Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		stmt.setFetchSize(10);
		ResultSet r = stmt.executeQuery("SELECT * FROM YelpUser");
		System.out.println(r.getFetchSize());
		ResultSetMetaData meta = r.getMetaData();

		System.out.println(r.getFetchSize());
		while(r.next()) {
			for (int col = 1; col <= meta.getColumnCount(); col++) {
				//System.out.print("\"" + r.getString(col) + "\",");
				YelpID test = new YelpID(r.getString("userid"));
				data.add(test);
			}
			System.out.println();
		}
	}

	public void AddRowUserTable() {
		// Removes all column to allow for new Query
		userTable.getColumns().clear();

		TableColumn firstNameCol = new TableColumn("Name ");
		firstNameCol.setMinWidth(100);
		firstNameCol.setCellValueFactory(new PropertyValueFactory<YelpID, String>("firstName"));

		TableColumn lastNameCol = new TableColumn("Last Name");
		lastNameCol.setMinWidth(100);
		lastNameCol.setCellValueFactory(new PropertyValueFactory<YelpID, String>("lastName"));

		TableColumn emailCol = new TableColumn("Email");
		emailCol.setMinWidth(200);
		emailCol.setCellValueFactory(new PropertyValueFactory<YelpID, String>("email"));

		userTable.setItems(data);
		userTable.getColumns().addAll(firstNameCol, lastNameCol, emailCol);

	}

	// ------/
	public static class YelpID {

		private final SimpleStringProperty name;
		private final SimpleStringProperty lastName;
		private final SimpleStringProperty email;

		private YelpID(String fName, String lName, String email) {
			this.name = new SimpleStringProperty(fName);
			this.lastName = new SimpleStringProperty(lName);
			this.email = new SimpleStringProperty(email);
		}
		
		private YelpID(String newName) {
			name = new SimpleStringProperty(newName);
			this.lastName = new SimpleStringProperty("test");
			this.email = new SimpleStringProperty("test");
		}

		public String getFirstName() {
			return name.get();
		}

		public void setFirstName(String fName) {
			name.set(fName);
		}

		public String getLastName() {
			return lastName.get();
		}

		public void setLastName(String fName) {
			lastName.set(fName);
		}

		public String getEmail() {
			return email.get();
		}

		public void setEmail(String fName) {
			email.set(fName);
		}
	}

}
