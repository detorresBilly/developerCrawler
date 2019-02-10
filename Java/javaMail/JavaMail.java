package javaMail;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import database.Accounts;
import database.ActivationKeyTable;
import hashing.keyGen;

public class JavaMail {
    // Below is the email account to send mail from.
    final static String mailAddress = "frances.disarra@gmail.com";
    final static String mailPassword = System.getenv("EMAIL_PASS");
    
    /**Emails a message containing the specified content to the specified email address. 
     * 
     * PRECONDITIONS: An account with the specified email address must exist in the
     * database.
     * 
     * @param email
     * @param content
     * @return true if preconditions are met, false if they are violated.
     */
    public static boolean sendMail(String email, String content) {
	String userToMail = Accounts.getUserName(email);;
	if (userToMail == null) {
	    throw new IllegalStateException();
	} else {
	    Properties gmailServer = new Properties();
	    gmailServer.put("mail.smtp.host", "smtp.gmail.com");
	    gmailServer.put("mail.smtp.port", "587");
	    gmailServer.put("mail.smtp.starttls.enable", "true");
	    gmailServer.put("mail.smtp.auth", "true");

	    /*
	     * This isn't needed but I wanted to try to show off some home made phil
	     * lambda's but I learned lambdas are a no no for this because its not an
	     * abstract interface.
	     */
	    Session session = Session.getInstance(gmailServer, new javax.mail.Authenticator() {
		protected PasswordAuthentication getPasswordAuthentication() {
		    return new PasswordAuthentication(mailAddress, mailPassword);
		}
	    });
	    Session mySession = Session.getInstance(gmailServer);

	    try {
		Message message = new MimeMessage(mySession);
		message.setFrom(new InternetAddress(mailAddress));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
		message.setSubject("Dev Crawler");
		message.setText("Dear " + userToMail + ", \n" + content);

		// send the message
		Transport.send(message, mailAddress, mailPassword);

	    } catch (MessagingException e) {
		throw new RuntimeException(e);
	    }
	}
	return true;
    }

    /**Sends a validation email with a key to the specified address.
     * PRECONDITIONS: An UNREGISTERED account with the specified email address must exist in the
     * database.
     * POSTCONDITIONS: adds the email and key to the activation key database.
     * 
     * @param email
     * @return returns true if preconditions are met, and false if they are violated.
     */
    public static boolean sendValidationEmail(String email) {
	String genKey = keyGen.generateRandomSerialNumber16_62();
	String message = "Your account registration key is " + genKey;
	if (!Accounts.isValidated(email)) {
	    if(sendMail(email, message)) {
		ActivationKeyTable.addKey(email, genKey);
		return true;
	    }
	} else {
	    System.err.println("The account is already activated");
	}
	return false;
    }

    public static void main(String[] args) {

    }
}