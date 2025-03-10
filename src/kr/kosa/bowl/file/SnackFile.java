//package kr.kosa.bowl;
//
//import java.io.*;
//import java.util.HashMap;
//import java.util.Map;
//
//public class SnackFile {
//
//	private static final String FILE_PATH = "snack.txt";
// 
//	// 초기 메뉴 설정 (파일이 없을 때만 실행)
//	static void initSnackFile() {
//		File file = new File(FILE_PATH);
//		if (!file.exists()) {
//			System.out.println("[INFO] snack.txt가 없어 초기 데이터 생성.");
//			makeSnackFile();
//		}
//	}
//
//	// 초기 메뉴 저장
//	static void makeSnackFile() {
//		Map<String, Snack> snackMap = new HashMap<>();
//		snackMap.put("맥주", new Snack("맥주", 5000, 100));
//		snackMap.put("땅콩", new Snack("땅콩", 3000, 100));
//
//		writeToFile(snackMap);
//	}
//
//	// 주문 후 파일에 저장하는 오버로딩 메서드
//	static void makeSnackFile(Map<String, Snack> snackMap) {
//		writeToFile(snackMap);
//	}
//
//	// 파일에 데이터 저장
//	private static void writeToFile(Map<String, Snack> snackMap) {
//		try (FileOutputStream fos = new FileOutputStream(FILE_PATH);
//				BufferedOutputStream bos = new BufferedOutputStream(fos);
//				ObjectOutputStream oos = new ObjectOutputStream(bos)) {
//
//			oos.writeObject(snackMap);
//			System.out.println("[INFO] 파일 저장 완료.");
//
//		} catch (IOException e) {
//			System.err.println("[ERROR] 파일 저장 중 오류 발생: " + e.getMessage());
//			e.printStackTrace();
//		}
//	}
//
//	// 파일에서 데이터 불러오기
//	static Map<String, Snack> readSnackFile() {
//		File file = new File(FILE_PATH);
//
//		if (!file.exists()) {
//			System.out.println("[INFO] snack.txt가 없어 기본 데이터 생성 후 읽음.");
//			makeSnackFile();
//		}
//
//		try (FileInputStream fis = new FileInputStream(file);
//				BufferedInputStream bis = new BufferedInputStream(fis);
//				ObjectInputStream ois = new ObjectInputStream(bis)) {
//
//			return (Map<String, Snack>) ois.readObject();
//
//		} catch (IOException | ClassNotFoundException e) {
//			System.err.println("[ERROR] 파일 읽기 중 오류 발생: " + e.getMessage());
//			e.printStackTrace();
//		}
//
//		return new HashMap<>(); // 오류 발생 시 빈 맵 반환
//	}
//}


