package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import account.Account;
import hashing.keyGen;

public class ActivationKeyTable {

    /**
     * Add a touple (email, key) to the ActivationKey database.
     * 
     * @param email
     */
    public static void addKey(String email, String key) {
	String insertQ = "INSERT INTO activationkeys (emailAddress, activationKey) VALUES (?, ?)";
	try (Connection con = DBConnection.getConnection();
		PreparedStatement stmt = con.prepareStatement(insertQ, ResultSet.TYPE_SCROLL_INSENSITIVE,
			ResultSet.CONCUR_READ_ONLY);) {
	    stmt.setString(1, email);
	    stmt.setString(2, key);
	    stmt.execute();
	} catch (SQLException e) {
	    
	}
    }
    
    /**PRECONDITIONS: Account with that activation key must be pending activation, and keyGen can never generate the same key twice.
     * POSTCONDITIONS: If preconditions are met, the account associated with the specified activation key becomes a validated user. 
     * 
     * @param email the address to validate.
     * @return true if the code exists in activation keys database, false otherwise. 
     */
    public static boolean validateKey(String key) {
	String isAccQ = "SELECT emailAddress, activationKey FROM activationkeys WHERE activationKey =  ?";

	ResultSet rs = null;
	try (Connection con = DBConnection.getKeyConnection();
		PreparedStatement stmt = con.prepareStatement(isAccQ, ResultSet.TYPE_SCROLL_INSENSITIVE,
			ResultSet.CONCUR_READ_ONLY);) {
	    stmt.setString(1, key);
	    rs = stmt.executeQuery();

	    if (rs.next()) {
		if (rs.getString("activationKey").equals(key)) {
		    Accounts.setRegistered(rs.getString("emailAddress"));
		    return true;
		} else {
		    return false;
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    if (rs != null) {
		try {
		    rs.close();
		} catch (SQLException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	}
	return false;
    }
}
