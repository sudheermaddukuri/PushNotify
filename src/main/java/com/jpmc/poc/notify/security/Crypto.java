package com.jpmc.poc.notify.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;
import org.springframework.util.Base64Utils;

import com.jpmc.poc.notify.broker.BrokerInstance;


public class Crypto {
	private static final Logger logger = Logger.getLogger(BrokerInstance.class);
	
	public static String encrypt(String key, String initVector, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            
            byte[] keyBytes = key.getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            keyBytes = sha.digest(keyBytes);
            keyBytes = Arrays.copyOf(keyBytes, 16); // use only first 128 bit

            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
            
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());

            return new String(Base64Utils.encode(encrypted));
        } catch (Exception ex) {
            logger.error(ex.getCause());
        }

        return null;
    }

    public static String decrypt(String key, String initVector, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            
            byte[] keyBytes = key.getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            keyBytes = sha.digest(keyBytes);
            keyBytes = Arrays.copyOf(keyBytes, 16); // use only first 128 bit

            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
            
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);

            byte[] original = cipher.doFinal(Base64Utils.decode(encrypted.getBytes()));

            return new String(original);
        } catch (Exception ex) {
        	logger.error(ex.getCause());
        }

        return null;
    }


    public static String encode(String key) {
    	try {
			return new String(Base64Utils.encode(key.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getCause());
		}
    	return null;
    }
    
    public static String decode(String key) {
    	try {
			return new String(Base64Utils.decode(key.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getCause());
		}
    	return null;
    }
    
	/*public static void main(String[] args) {
		String key = "CA Push Notification Key"; // 128 bit key
		String token = "CA Push Notification Token";
        String initVector = "RandomInitVector"; // 16 bytes IV

        String eKey = encode(key);
        //System.out.println("Encoded Key: " + eKey);
        
        String dKey = decode(eKey);
        //System.out.println("Decoded Key: " + dKey);
        
        
        //String encryptedToken = encrypt(dKey, initVector, token);
        String encryptedToken = "EHRopVA9xX3n1UVdbEsONf1fCx/pJPIQJc0Zr8=";
        System.out.println("Encrypted Token: " + encryptedToken);
        
        String decryptedToken = decrypt(dKey, initVector, encryptedToken); 
        System.out.println("Decrypted Token: " + decryptedToken);
        
        if(token != null && token.equals(decryptedToken))
        	System.out.println("PASS");
        else
        	System.out.println("FAIL");
    }*/

}
