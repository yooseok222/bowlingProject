//package kr.kosa.bowl;
//
//import java.security.Key;
//import java.security.KeyFactory;
//import java.security.KeyPair;
//import java.security.KeyPairGenerator;
//import java.security.NoSuchAlgorithmException;
//import java.security.spec.RSAPrivateKeySpec;
//import java.security.spec.RSAPublicKeySpec;
//
//import javax.crypto.Cipher;
//
//public class RSAKeyGenerator {
//	
//	public void Generate(String key) {
//		KeyPairGenerator keyPairGenerator;
//		
//		Key publicKey;
//		Key privateKey;
//		
//		try {
//			//공개키 및 개인키 생성
//			keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//			keyPairGenerator.initialize(2048);
//			
//			KeyPair keyPair = keyPairGenerator.genKeyPair();
//			publicKey = keyPair.getPublic(); //공개키
//			privateKey = keyPair.getPrivate(); //개인키
//			
//			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//			RSAPublicKeySpec publicKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
//			RSAPrivateKeySpec privateKeySpec = keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class); 
//		
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	
//	}
//	
//	public boolean Encrypt(String inputPw) {
//		//암호화
//		Cipher cipher = Cipher.getInstance("RSA");
//		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//		byte[] arrCipherData = cipher.doFinal(inputPw.getBytes());
//		String strCipher = new String(arrCipherData);
//		
//		//복호화
//		cipher.init(Cipher.DECRYPT_MODE, privateKey);
//		byte[] arrData = cipher.doFinal(arrCipherData);
//		String strResult = new String(arrData);
//	}
//	
//	public void decrypt
//	
//}
