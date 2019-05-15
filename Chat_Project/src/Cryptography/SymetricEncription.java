package Cryptography;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class SymetricEncription {
	public String symetricEncription(String publicKey, String msg) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		byte[] key = publicKey.getBytes();//... secret sequence of bytes
		byte[] dataToSend = msg.getBytes();

		Cipher c = Cipher.getInstance("AES");
		SecretKeySpec k =
		  new SecretKeySpec(key, "AES");
		c.init(Cipher.ENCRYPT_MODE, k);
		byte[] encryptedData = c.doFinal(dataToSend);
		return encryptedData.toString();
	}
	
	public String symetricDecryption(String publicKey, String msg) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		byte[] key = publicKey.getBytes();//... we know the secret!
		byte[] encryptedData = publicKey.getBytes();//... received from Alice

		Cipher c = Cipher.getInstance("AES");
		SecretKeySpec k =
		  new SecretKeySpec(key, "AES");
		c.init(Cipher.DECRYPT_MODE, k);
		byte[] data = c.doFinal(encryptedData);
		return data.toString();
	}
}
