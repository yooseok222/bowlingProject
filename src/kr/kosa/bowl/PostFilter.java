package kr.kosa.bowl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class PostFilter {
	// 금지어 목록을 저장할 Set
	private static final Set<String> bannedWords = new HashSet<>();

	// 파일에서 금지어 목록 로드
	static {
		loadBannedWords("banned_words.txt");
	}

	// 금지어 파일 로드 메서드
	private static void loadBannedWords(String filePath) {
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				bannedWords.add(line.trim()); // 공백 제거 후 저장
			}
		} catch (IOException e) {
		}
	}

	// 금지어 포함 여부 확인 (초성만 이루어진 단어도 필터링)
	public static boolean containsBannedWords(String postContent) {
		for (String bannedWord : bannedWords) {
			if (postContent.contains(bannedWord)) {
				return true; // 금지어 포함됨
			}
		}
		return false; // 금지어 없음
	}

}
