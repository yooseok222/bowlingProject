package kr.kosa.bowl.security;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
	private static Properties properties = new Properties();

	static {
		try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("credentials.properties")) {
			if (input != null) {
				properties.load(input);
			} else {
				System.err.println("속성 파일을 찾을 수 없습니다!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getProperty(String key) {
		return properties.getProperty(key);
	}

}
