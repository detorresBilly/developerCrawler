package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.DBConnection;
import account.Account;

public class Accounts {
    static ArrayList<Account> accList = new ArrayList<>();
    private static String SQL = "SELECT * FROM accounts WHERE email =  ?";

    /**
     * Gets the username associated with the account from the database
     * 
     * PRECONDITION: The email must be associated with an existing account in the
     * database POSTCONDITIONS: No side effects.
     * 
     * @param email
     * @return the username associated with the specified email
     */
    public static String getUserName(String email) {
	String userNameQ = "SELECT username FROM accounts WHERE email =  ?";
	String accName = null;
	ResultSet rs = null;
	try (Connection con = DBConnection.getConnection();
		PreparedStatement stmt = con.prepareStatement(userNameQ, ResultSet.TYPE_SCROLL_INSENSITIVE,
			ResultSet.CONCUR_READ_ONLY);) {
	    stmt.setString(1, email);
	    rs = stmt.executeQuery();

	    while (rs.next()) {
		accName = rs.getString("username");
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
	return accName;
    }

    public static Account getAccount(String email) {
	Account acc;
	ResultSet rs = null;
	try (Connection con = DBConnection.getConnection();
		PreparedStatement stmt = con.prepareStatement(SQL, ResultSet.TYPE_SCROLL_INSENSITIVE,
			ResultSet.CONCUR_READ_ONLY);) {
	    stmt.setString(1, email);
	    rs = stmt.executeQuery();

	    while (rs.next()) {
		acc = new Account(rs.getString("username"), rs.getString("passHash"), rs.getString("email"),
			rs.getBoolean("isAuthenticated"));
		return acc;
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
	return null;
    }

    public static void addAccount(Account acc) {
	String insertQ = "INSERT INTO accounts (email, passHash, username) VALUES (?, ?, ?)";
	try (Connection con = DBConnection.getConnection();
		PreparedStatement stmt = con.prepareStatement(insertQ, ResultSet.TYPE_SCROLL_INSENSITIVE,
			ResultSet.CONCUR_READ_ONLY);) {
	    System.out.println(acc.getPassword());
	    stmt.setString(1, acc.getEmail());
	    stmt.setString(2, acc.getPassword());
	    stmt.setString(3, acc.getUsername());
	    stmt.execute();

	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    public static boolean isValidated(String email) {
	ResultSet rs = null;
	try (Connection con = DBConnection.getConnection();
		PreparedStatement stmt = con.prepareStatement(SQL, ResultSet.TYPE_SCROLL_INSENSITIVE,
			ResultSet.CONCUR_READ_ONLY);) {
	    stmt.setString(1, email);
	    rs = stmt.executeQuery();

	    while (rs.next()) {
		if (rs.getString("isAuthenticated").equals("0")) {
		    return false;
		} else {
		    return true;
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

    /**
     * Returns 0 if successful login Returns 1 if an account exists with that email
     * but the password was wrong Returns 2 if no account exists with that email
     * otherwise returns -1
     * 
     * @param email
     * @return
     */
    public static int isAccount(String email, String password) {
	String isAccQ = "SELECT email, passHash FROM accounts WHERE email =  ?";

	ResultSet rs = null;
	try (Connection con = DBConnection.getConnection();
		PreparedStatement stmt = con.prepareStatement(isAccQ, ResultSet.TYPE_SCROLL_INSENSITIVE,
			ResultSet.CONCUR_READ_ONLY);) {
	    stmt.setString(1, email);
	    rs = stmt.executeQuery();

	    if (rs.next()) {
		if (rs.getString("passHash").equals(password)) {
		    return 0;
		} else {
		    return 1;
		}
	    } else {
		return 2;
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
	return -1;
    }

    /**
     * Checks if an account with the email already exists
     * 
     * @param email
     *            checks if an account with email is registered in the database
     * @return true if the email is already used, false if no account exists with
     *         that email
     */
    public static boolean accAlreadyExists(String email) {
	String isAccQ = "SELECT email FROM accounts WHERE email =  ?";

	ResultSet rs = null;
	try (Connection con = DBConnection.getConnection();
		PreparedStatement stmt = con.prepareStatement(SQL, ResultSet.TYPE_SCROLL_INSENSITIVE,
			ResultSet.CONCUR_READ_ONLY);) {
	    stmt.setString(1, email);
	    rs = stmt.executeQuery();

	    if (rs.next()) {
		return true;
	    } else {
		return false;
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

    public static void setRegistered(String email) {
	String registerQ = "UPDATE accounts SET isAuthenticated = '1' WHERE email = ?";
	try (Connection con = DBConnection.getConnection(); PreparedStatement stmt = con.prepareStatement(registerQ);) {
	    stmt.setString(1, email);
	    stmt.execute();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    public static void failedLoginAttempt(String email) {
	String loginAttemptQ = "UPDATE accounts SET failedLogin = failedLogin + 1 WHERE email = ?";
	try (Connection con = DBConnection.getConnection();
		PreparedStatement stmt = con.prepareStatement(loginAttemptQ);) {
	    stmt.setString(1, email);
	    stmt.execute();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }
}
