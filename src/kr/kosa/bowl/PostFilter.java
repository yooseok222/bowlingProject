package kr.kosa.bowl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class PostFilter {
	// 금지어 목록을 저장할 Set
    private static final Set<String> bannedWords = new HashSet<>();
    private static final String FILE_PATH = "banned_words.txt";
    
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
    
    /*public static void main(String[] args) {
        String post1 = "이 게시글에는 욕설 꺼져가 포함되어 있습니다.";
        String post2 = "안녕하세요";

        System.out.println(checkBannedWords(post1));
        System.out.println(checkBannedWords(post2));
    }*/
    
 	/** 금지어 목록 출력 */
    public static void printBannedWords() {
        if (bannedWords.isEmpty()) {
            System.out.println("등록된 금지어가 없습니다.");
        } else {
            System.out.println("=== 등록된 금지어 목록 ===");
            int count = 1;
            for (String word : bannedWords) {
                System.out.println(count + ". " + word);
                count++;
            }
            System.out.println("총 " + bannedWords.size() + "개의 금지어가 등록되어 있습니다.");
        }
    }
    
 	/** 금지어를 파일에 추가하는 메서드 */
	public static boolean addBannedWordToFile(String word) {
	    if (word == null || word.trim().isEmpty()) {
	        System.out.println("❌ 금지어가 입력되지 않았습니다.");
	        return false;
	    }
	    
	    if (bannedWords.contains(word.trim())) {
	        System.out.println("❌ 이미 등록된 금지어입니다: " + word);
	        return false;
	    }
	    
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) { // true는 append 모드
	        writer.write(word.trim());
	        writer.newLine(); // 새 줄 추가
	        
	        bannedWords.add(word.trim());
	        
	        System.out.println("✅ 새 금지어가 추가되었습니다: " + word);
	        return true;
	    } catch (IOException e) {
	        System.err.println("❌ 금지어 파일에 추가하는 중 오류 발생: " + e.getMessage());
	        return false;
	    }
	}
	
	/** 금지어를 파일에서 삭제하는 메서드 */
	public static boolean delBannedWordToFile(String word) {
	    if (word == null || word.trim().isEmpty()) { 
	        System.out.println("❌ 금지어가 입력되지 않았습니다.");
	        return false;
	    } 
	    
	    String trimmedWord = word.trim();
	    
	    if (!bannedWords.contains(trimmedWord)) {
	        System.out.println("❌ 등록되지 않은 금지어입니다: " + word);
	        return false;
	    }
	    
	    bannedWords.remove(trimmedWord);
	    
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) { // false는 덮어쓰기 모드
	        for (String bannedWord : bannedWords) {
	            writer.write(bannedWord);
	            writer.newLine();
	        }
	        
	        System.out.println("✅ 금지어가 삭제되었습니다: " + word);
	        return true;
	    } catch (IOException e) {
	        System.err.println("❌ 금지어 파일에서 삭제하는 중 오류 발생: " + e.getMessage());
	        return false;
	    }
	}
}
