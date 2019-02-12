package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String USERNAME = "colll78";
    private static final String PASSWORD = System.getenv("DB_PASSWORD");
    private static final String CONN = "jdbc:mysql://localhost/devcrawler";

    public static Connection getConnection() {
	try {
	    System.out.println(PASSWORD);
	    return DriverManager.getConnection(CONN, USERNAME, PASSWORD);
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return null;
    }
    
    public static Connection getKeyConnection() {
	String connect = "jdbc:mysql://localhost/devcrawler";
	try {
	    return DriverManager.getConnection(connect, USERNAME, PASSWORD);
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return null;
    }
}
