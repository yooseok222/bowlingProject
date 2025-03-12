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
            //System.out.println("🚀 금지어 로드 완료: " + bannedWords.size() + "개");
        } catch (IOException e) {
            //System.err.println("❌ 금지어 파일을 읽을 수 없습니다: " + e.getMessage());
        }
    }
    
 // 게시글 필터링 함수 (부분 문자열 검사)
    public static String checkBannedWords(String postContent) {
        for (String bannedWord : bannedWords) {
            if (postContent.contains(bannedWord)) { // 부분 문자열 검사
                return "🚨 금지어가 포함된 게시글입니다.";
            }
        }
        return postContent; // 금지어 없으면 원래 게시글 반환
    }
    
    public static void main(String[] args) {
        String post1 = "이 게시글에는 욕설 꺼져가 포함되어 있습니다.";
        String post2 = "안녕하세요";

        System.out.println(checkBannedWords(post1));
        System.out.println(checkBannedWords(post2));
    }
}
