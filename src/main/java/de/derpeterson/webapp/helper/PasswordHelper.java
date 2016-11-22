package de.derpeterson.webapp.helper;

import org.mindrot.jbcrypt.BCrypt;

/**
  * Author: Ian Gallagher <igallagher@securityinnovation.com>
  *
  * This code utilizes jBCrypt, which you need installed to use.
  * jBCrypt: http://www.mindrot.org/projects/jBCrypt/
  */

public class PasswordHelper {
  	// Define the BCrypt workload to use when generating password hashes. 10-31 is a valid value.
	private static int workLoad = 12;

	/**
	 * This method can be used to generate a string representing an account password
	 * suitable for storing in a database. It will be an OpenBSD-style crypt(3) formatted
	 * hash string of length=60
	 * The bcrypt workload is specified in the above static variable, a value from 10 to 31.
	 * A workload of 12 is a very reasonable safe default as of 2013.
	 * This automatically handles secure 128-bit salt generation and storage within the hash.
	 * @param passwordPlainText The account's plaintext password as provided during account creation,
	 *			     or when changing an account's password.
	 * @return String - a string of length 60 that is the bcrypt hashed password in crypt(3) format.
	 */
	public static String hashPassword(String passwordPlainText) {
		String salt = BCrypt.gensalt(workLoad);
		String passwordHashed = BCrypt.hashpw(passwordPlainText, salt);
		return(passwordHashed);
	}
	
	/**
	 * This method can be used to secure a string with a given salt.
	 * @param stringToBeSecured The string to be secured
	 * @param salt The salt what will be used
	 * @return String - a string of length 60 that is the bcrypt hashed password in crypt(3) format.
	 */
	public static String hashPassword(String stringToBeSecured, String salt) {
		String passwordHashed = BCrypt.hashpw(stringToBeSecured, salt);

		return(passwordHashed);
	}

	/**
	 * This method can be used to verify a computed hash from a plaintext (e.g. during a login
	 * request) with that of a stored hash from a database. The password hash from the database
	 * must be passed as the second variable.
	 * @param passwordPlainText The account's plaintext password, as provided during a login request
	 * @param storedHash The account's stored password hash, retrieved from the authorization database
	 * @return boolean - true if the password matches the password of the stored hash, false otherwise
	 */
	public static boolean checkPassword(String passwordPlainText, String storedHash) {
		boolean passwordVerified = false;

		if(null == storedHash || !storedHash.startsWith("$2a$"))
			throw new IllegalArgumentException("Invalid hash provided for comparison");

		passwordVerified = BCrypt.checkpw(passwordPlainText, storedHash);

		return(passwordVerified);
	}

	/**
	  * A simple test case for the main method, verify that a pre-generated test hash verifies successfully
	  * for the password it represents, and also generate a new hash and ensure that the new hash verifies
	  * just the same.
	  */
	public static void main(String[] args) {
		String testPassword = "abcdefghijklmnopqrstuvwxyz";
		String testHash = "$2a$06$.rCVZVOThsIa97pEDOxvGuRRgzG64bvtJ0938xuqzv18d3ZpQhstC";

		System.out.println("Testing BCrypt Password hashing and verification");
		System.out.println("Test password: " + testPassword);
		System.out.println("Test stored hash: " + testHash);
		System.out.println("Hashing test password...");
		System.out.println();

		String computedHash = hashPassword(testPassword);
		System.out.println("Test computed hash: " + computedHash);
		System.out.println();
		System.out.println("Verifying that hash and stored hash both match for the test password...");
		System.out.println();

		String compareTest = checkPassword(testPassword, testHash) ? "Passwords Match" : "Passwords do not match";
		String compareComputed = checkPassword(testPassword, computedHash) ? "Passwords Match" : "Passwords do not match";

		System.out.println("Verify against stored hash:   " + compareTest);
		System.out.println("Verify against computed hash: " + compareComputed);

	}

}