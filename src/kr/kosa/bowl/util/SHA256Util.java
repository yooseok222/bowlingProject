package kr.kosa.bowl.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SHA256Util {
	
	//SHA-256 해시 값을 계산하는 메서드
	public static String getSHA256Hash(String data) {

		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
//			digest.update(salt);
			
			byte[] byteData = data.getBytes();
			
			byte [] hashBytes = digest.digest(byteData);
			
			StringBuilder hexString = new StringBuilder();
			for(byte b : hashBytes) {
				String hex = Integer.toHexString(0xff & b);
				if(hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}
			
			return hexString.toString().toUpperCase();
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	
//	//Salt를 생성하는 메서드
//	public static byte[] generateSalt() {
//		SecureRandom random = new SecureRandom();
//		byte[] salt = new byte[16];
//		random.nextBytes(salt);
//		return salt;
//	}

	//바이트 배열을 16진수 문자열로 변환하는 메서드
	private static String bytesToHex(byte[] bytes) {
		StringBuilder hexString = new StringBuilder();
		for(byte b : bytes) {
			String hex = Integer.toHexString(0xff & b);
			if(hex.length() == 1) hexString.append('0');
			hexString.append(hex);
		}
		
		return hexString.toString().toUpperCase();
		
	}
	
}
