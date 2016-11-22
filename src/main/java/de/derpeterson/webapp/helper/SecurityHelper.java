package de.derpeterson.webapp.helper;

import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import de.derpeterson.webapp.model.User;

public enum SecurityHelper {

	INSTANCE;
	
	private static final String ALGORITHM = "AES";
    private static final byte[] keyValue = new byte[] { '[', '8', '@', 'b', 'a', '4', 'd', '5', '4', 'x', ']', 'w', '0', 'l', 'w', 'c'};
	
	private SecurityHelper() {}

	/**
	 * Method to encrypt a string
	 * @param String String to encrypt
	 * @return String Encrypted string
	 * @throws GeneralSecurityException 
	 */
	public String encrypt(String string) throws GeneralSecurityException {
		try {
			Key aesKey = new SecretKeySpec(keyValue, ALGORITHM);
	        Cipher cipher = Cipher.getInstance(ALGORITHM);
	        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
	        byte[] encryptedByteValue = cipher.doFinal(string.getBytes(Charset.forName("UTF-8")));
	        String encryptedValue = new Base64().encodeToString(encryptedByteValue);
	        return encryptedValue;
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw e;
		} finally {
			
		}
	}
	
	/**
	 * Method to decrypt a string
	 * @param String Encrypted string to decrypt
	 * @return String Decrypted string
	 * @throws GeneralSecurityException 
	 */
	public String decrypt(String encryptedValue) throws GeneralSecurityException {
		try {
			Key aesKey = new SecretKeySpec(keyValue, ALGORITHM);
	        Cipher cipher = Cipher.getInstance(ALGORITHM);
	        cipher.init(Cipher.DECRYPT_MODE, aesKey);
	        byte[] encryptedByteValue =  new Base64().decode(encryptedValue);
	        byte[] decryptedValue = cipher.doFinal(encryptedByteValue);
	        return new String(decryptedValue);
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw e;
		} finally {
			
		}
	}

	public String generateUserToken(boolean urlSafe, User user) {
		String returnString = null;
		if(Objects.nonNull(user)) {
			returnString = new Base64(urlSafe).encodeToString(DigestUtils.sha256(UUID.randomUUID().toString().toUpperCase()  + "|" + user.getUsername() + "|" + user.getPassword())).trim();
		}
		return returnString;
	}
}