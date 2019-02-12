package account;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import hashing.keyGen;

public class Account {
    private String username;
    private String password;
    private String email;
    private boolean activated;

    public Account(String user, String pass) {
	username = user;
	password = pass;
	email = "philipdisarro@gmail.com";
	activated = false;
    }

    public Account(String user, String pass, String mail) {
	username = user;
	password = pass;
	email = mail;
	activated = false;
    }

    public Account(String user, String pass, String mail, boolean activ) {
	username = user;
	password = pass;
	email = mail;
	activated = activ;
    }

    public String getUsername() {
	return this.username;
    }

    public String getPassword() {
	return this.password;
    }

    public String getEmail() {
	return email;
    }

    public boolean isActivated() {
	return activated;
    }

    public void setUsername(String toBeUser) {
	this.username = toBeUser;
    }

    public void setPassword(String toBePass) {
	this.password = toBePass;
    }

    public void getEmail(String mailAddress) {
	email = mailAddress;
    }

    public String toString() {
	return "Username: " + username + " " + "Password: " + password + " Email: " + email + "\n";
    }

}
