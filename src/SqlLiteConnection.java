import java.sql.*;

/**
 * 
 * @author Bala Subramanyam Lanka
 * Student ID: 2014356
 *
 */

import javax.swing.JOptionPane;

public class SqlLiteConnection {

	public static Connection dbConnector() {

		try {

			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:Studentdb.db");
			return conn;

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
			return null;
		}

	}

}
