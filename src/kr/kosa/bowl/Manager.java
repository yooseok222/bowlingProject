package kr.kosa.bowl;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import kr.kosa.bowl.security.ConfigLoader;
import kr.kosa.bowl.storage.SnackFileIO;
import kr.kosa.bowl.util.AbstractFileIO;
import kr.kosa.bowl.util.SHA256Util;

public class Manager {
	
	Scanner sc = new Scanner(System.in);
 
	transient AbstractFileIO<Map<String, Snack>> fileIO = new SnackFileIO();
	Map<String, Snack> snackMenu = fileIO.loadFile(); 
	

	String adminEmail = ConfigLoader.getProperty("ADMIN_EMAIL");
	String adminPw = ConfigLoader.getProperty("ADMIN_PW");
	
	/** 이메일 유효성 검사 - 정규표현식*/
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
    
    
    /** 이메일 검사 */
    public boolean verifyEmail(String inputEmail) {
    	return inputEmail.equals(adminEmail);
    }
    
    /** 비밀번호 검사 */
    public boolean verifyPassword(String inputPw) {
    	return SHA256Util.getSHA256Hash(inputPw).equals(adminPw);
    }
    
    
	/** 관리자 인증 */
	public void validateManager() {
		
		if(adminEmail == null || adminEmail.isEmpty()) {
    		throw new IllegalStateException("이메일 설정이 누락되었습니다. 나중에 다시 시도해주세요.");
    	}
		
		if(adminPw == null || adminPw.isEmpty()) {
    		throw new IllegalStateException("비밀번호 설정이 누락되었습니다. 나중에 다시 시도해주세요.");
    	}
		
		
		boolean isCorrect = false;
		
		do {
 
			System.out.println("관리자 이메일을 입력하세요 : "); 
			String inputEmail = sc.nextLine();
			
			if(isValidEmail(inputEmail)) {
				System.out.println("비밀번호를 입력하세요 : ");  
				String inputPw = sc.nextLine();	 
				isCorrect = isValidEmail(inputEmail) && verifyPassword(inputPw) ? true : false;
				if(!isCorrect) System.err.println("잘못 입력하셨습니다. 다시 입력해주세요.");
			}else {
				System.err.println("이메일 형식이 맞지 않습니다. 다시 입력해주세요");
			}
		}while(!isCorrect);
		
		getAdminMenu();
	};
	
	/** 관리자 메뉴 */
	private void getAdminMenu() { 
		
		while(true) {
			System.out.println("관리자 메뉴입니다.");
			System.out.println("1. 레인 청소 | 2. 상품 관리 | 3. 전체 매출 조회 | 4. 리뷰관리 | 5. 금지어 관리 | 0. 초기 화면으로 돌아가기");
	
			String inputMenu = sc.nextLine();
			
			switch(inputMenu) {
				case "1" : cleanLane();
					break;
				case "2" : getSnackMenu();
					break;
				case "3" : getProfit();
					break;
				case "4" : getReviewList();
					break;
				case "5" : manageBannedWords();
					break;
				case "0" : 
					return;
				default : System.err.println("잘못 입력하셨습니다. 다시 입력해주세요.");
					break;
			}
		}
	
	}
	



	/** 레인 청소 */
	private void cleanLane() {
		System.out.println("전체 레인 상태 조회");

		int input = 0;
		boolean flag = false;
		do { 
			Menu.printLaneAvail();
			
			System.out.println("청소할 레인의 번호를 입력해주세요"); 
			System.out.println("0번을 누르시면 관리자 메뉴로 돌아갑니다");
			input = Integer.parseInt(sc.nextLine())-1;
			 
			if(input == -1) { 
				flag = true;
				break;
			}else {
				if(Menu.lanes[input].isClean() == true) {
					System.out.println("🚫🧹 이미 청소된 레인입니다. 다시 입력해주세요.");
				}else {
					Menu.lanes[input].setClean(true);
					System.out.println("\n🧹✨ 쓱싹쓱싹 - 청소가 완료되었습니다 ✨🧹");
					System.out.println();
				}				
			}
		}while(!flag);
	
	};

	/** 상품 관리 메뉴 */
	private void getSnackMenu() {
		
		System.out.println("상품 관리 메뉴 진입");
		
		while(true) {
		
			System.out.println("상품 관리 페이지입니다.");
			System.out.println("1. 전체 상품 조회 | 2. 상품 추가 | 3. 상품 수정 | 4. 상품 삭제 | 5. 상품 검색 | 0. 관리자 메뉴로 돌아가기");
			
			String inputMenu = sc.nextLine();
			
			switch(inputMenu) {
				case "1" : getSnackList();
					break;
				case "2" : addSnack();
					break;
				case "3" : updateSnack();
					break;
				case "4" : delSnack();
					break;
				case "5" : getSnackByName();
					break;
				case "0" : 
					return;
				default : System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
					break;
			}
		}	
	}

	/** 상품 추가 */
	private void addSnack() {
		
		System.out.println("상품을 추가합니다.");
		String snackName = null;
		int snackPrice = 0;
		int snackCnt = 0;

		do {
			System.out.println("상품 이름을 입력해주세요 : ");
			snackName = sc.nextLine();	
			if(snackName.isBlank()) {
				System.err.println("상품 이름이 입력되지 않았습니다.");
			}
		}while(snackName.isBlank());

		
		String inputKind = null;
		
		inputKind = "상품 가격";
		snackPrice = validateNumber(inputKind);
		
		inputKind = "상품 수량";
		snackCnt = validateNumber(inputKind);
				
		Snack snack = new Snack(snackName, snackPrice, snackCnt);
		
		snackMenu.put(snackName, snack);
		
		fileIO.saveFile(snackMenu);
		
		System.out.println("상품이 추가되었습니다.");
	};
	 
	
	/** 상품 수량, 가격 입력 검증 */ 
	private int validateNumber(String inputKind) {
		
		int result = 0;
		boolean isValid = false;
		
		do {
			System.out.print(inputKind + "을 입력해주세요 : ");
			
			String input= sc.nextLine();
			
			if(input.isBlank()) {
				System.err.println(inputKind + " 값이 입력되지 않았습니다.");
			}else { 
				try {
					result = Integer.parseInt(input);
					
					if(result <= 0) {
						System.err.println(inputKind + "은 0보다 커야 합니다.");
					}else { 
						isValid = true;
					
						return result;
					}
				}catch (NumberFormatException e) {
					System.err.println(inputKind + "은 숫자만 입력해주세요.");
				}
			}
		}while(!isValid);
		
		return 0;
	}

	/** 상품 삭제 */
	private void delSnack() {  
		
		System.out.println("삭제하실 상품 이름을 입력하세요 : ");
		String snackName = sc.nextLine();

		boolean doesExist = getSnackByName(snackName);
		
		if(!doesExist) {
			System.err.println("결과가 없습니다.");
		}else {
			boolean escape = false;
			do {
				System.out.println(snackName + "을 삭제하시겠습니까? Y/N");
				String answer = sc.nextLine();
				System.out.println(answer.toLowerCase());
				if(answer.toUpperCase().equals("Y")) {
					snackMenu.remove(snackName);
//					sf.saveSnackMap(snackMenu);
					fileIO.saveFile(snackMenu);
					System.out.println("상품이 삭제되었습니다.");
					escape = true;
				}else if(answer.toUpperCase().equals("N")){ 
					System.out.println("상품 삭제를 취소하셨습니다.");
					escape = true;
				}else {
					System.err.println("잘못 입력하셨습니다.");
				}	
			}while(escape = false);
			
		}
	}; 
	
	/** 상품 수정(오버로딩) */
	private void updateSnack() {
		System.out.println("상품 수정 페이지");
		System.out.println("수정하실 상품의 이름을 입력해주세요 : ");
		String snackName = sc.nextLine();
		
		boolean doesExist = false;
		
		doesExist = getSnackByName(snackName); 
		
		if(!doesExist) {
			System.err.println("결과가 없습니다.");
		}else {
			System.out.println("수정하실 부분을 알려주세요");
			System.out.println("1. 이름 수정 | 2. 가격 수정 | 0. 관리자 메뉴로 돌아가기");
			String input = sc.nextLine();
			switch(input) {
				case "1" : updateSnackName(snackName);
					break;
				case "2" : updateSnackPrice(snackName);
					break;
				case "0" : 
					return;
				default : System.err.println("잘못 입력하셨습니다.");
					break;
			}
		}  
	};


	/** 전체 상품 조회*/
	private void getSnackList() { 
		
	    System.out.println("전체 상품 조회 페이지");
	    System.out.println("====================================================================");
	    System.out.printf(" %-12s | %-12s | %-12s\n", "상품명", "가 격", "수 량");
	    System.out.println("====================================================================");		

	    for(Map.Entry<String,Snack> e : snackMenu.entrySet()) {
	    	System.out.printf(" %-12s | %-12s | %-12s\n", e.getValue().getSnackName(), e.getValue().getSnackPrice(), e.getValue().getSnackCnt());
		}
	    
	    System.out.println("====================================================================");
	}
	
	
	/** 이름으로 상품 조회 */
	private void getSnackByName() {
		System.out.println("검색하실 상품 이름을 입력하세요 : ");
		String snackName = sc.nextLine(); 
		
		 for(Map.Entry<String,Snack> e : snackMenu.entrySet()) {
			if(e.getKey().contains(snackName)) { 
				System.out.println("상품명 : " + e.getValue().getSnackName() + 
						" | 가격 : " + e.getValue().getSnackPrice() + 
						" | 수량 : " + e.getValue().getSnackCnt());
			}
		}
	}; 
	
	 
	/** 상품 수정시 수정할 상품 검색  
	 * @param snackName */
	private boolean getSnackByName(String snackName) {
		boolean result = false;
		for(Map.Entry<String,Snack> e : snackMenu.entrySet()) {
			if(snackName.equals(e.getKey())){
				result = true;
			}
		}
	   return result;
	}; 
	
	
	/** 상품 이름 수정
	 * @param getSnack, snackName */
	private void updateSnackName(String snackName) {
		
		
		String newName = "";
		
		do {
			System.out.println("수정하실 상품의 새 이름을 입력해주세요");
			 
			newName = sc.nextLine();
			 
			if(newName.isBlank()) {
				System.err.println("상품명이 입력되지 않았습니다.");
			}else {
				Snack snackNameChanged = snackMenu.remove(snackName);
				if(snackNameChanged != null) {
					snackNameChanged.setSnackName(newName);			
					snackMenu.put(newName, snackNameChanged);
					fileIO.saveFile(snackMenu);
//					sf.saveSnackMap(snackMenu);  
				}
				System.out.println("상품 이름 수정이 완료되었습니다.");	
			}	
		
		}while(newName.isBlank());
		
		
	}
	
	/** 상품 가격 수정
	 * @param snackName */
	private void updateSnackPrice(String snackName) {
		System.out.println("수정하실 상품의 새 가격을 입력해주세요 : ");
		try {
			int newPrice = Integer.parseInt(sc.nextLine());			
			Snack snackPriceChanged = snackMenu.get(snackName);
			if(snackPriceChanged != null) {
				snackPriceChanged.setSnackPrice(newPrice);
				snackMenu.put(snackName, snackPriceChanged);
				fileIO.saveFile(snackMenu);
//				sf.initializeFile();
			}
			System.out.println("상품 가격 수정이 완료되었습니다.");
		} catch (NumberFormatException e) {
			System.err.println("가격이 입력되지 않았습니다.");
		}

	} 
	 
	/** 매출 조회 */
	private void getProfit() {
		System.out.println("매출 조회 페이지");
		System.out.println("1. 전체 매출 조회 | 2. 월별 매출 조회 | 3. 가장 많이 팔린 메뉴 0. 관리자 메뉴로 돌아가기 ");
		String inputMenu = sc.nextLine();
		
		switch(inputMenu) {
			case "1" : getProfitAll();
				break;
			case "2" : getProfitByMonth();
				break;
			case "3" : getTopSellingMenu();
				break;
			case "4" : 
				break;
			case "5" : 
				break;
			case "0" : 
				return;
			default : System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
				break;
		}
	}


	/** 전체 매출 조회 */
	private void getProfitAll() {
	    System.out.println("\n📊 전체 매출 조회 페이지");

	    Profit profit = Profit.getInstance();
	    profit.showReceiptList(); // 🔹 영수증 형식으로 출력
	    
	    Map<String, Integer> profitDetails = Profit.getInstance().getTotalProfitDetails();
	    
	    System.out.println("\n================ 전체 기간 매출 내역 =================");
	    System.out.println("게임비 매출: " + String.format("%,d", profitDetails.get("게임비")) + "원");
	    System.out.println("신발대여비 매출: " + String.format("%,d", profitDetails.get("신발대여비")) + "원");
	    System.out.println("간식 매출: " + String.format("%,d", profitDetails.get("간식매출")) + "원");
	    System.out.println("-----------------------------------------------");
	    System.out.println("총 매출액: " + String.format("%,d", profitDetails.get("총매출")) + "원");
	    System.out.println("===================================================");
	}

	
	/** 월별 매출 조회 */
	private void getProfitByMonth() {
		
		String inputMonth = "";

		do {
			System.out.println("조회를 원하시는 월을 입력해주세요. 예) 3월 매출 조회 시 -> 3");
			inputMonth = sc.nextLine();
			
			if(inputMonth.isBlank()) {
				System.err.println("상품명이 입력되지 않았습니다.");
			}else {
				 Map<String, Integer> profitDetails = Profit.getInstance().getMonthlyProfitDetails(inputMonth);
		            
		            System.out.println("\n================ " + inputMonth + "월 매출 내역 =================");
		            System.out.println("게임비 매출: " + String.format("%,d", profitDetails.get("게임비")) + "원");
		            System.out.println("신발대여비 매출: " + String.format("%,d", profitDetails.get("신발대여비")) + "원");
		            System.out.println("간식 매출: " + String.format("%,d", profitDetails.get("간식매출")) + "원");
		            System.out.println("-----------------------------------------------");
		            System.out.println(inputMonth + "월 총 매출액: " + 
		                String.format("%,d", profitDetails.get("총매출")) + "원");
		            System.out.println("===================================================");
				
			}	
		
		}while(inputMonth.isBlank());
	
	}

	/** 가장 많이 팔린 메뉴 */
	private void getTopSellingMenu() {
	
		List<Map.Entry<String, Integer>> top3Menus = Profit.getInstance().getTop3Menus();
	    Map<String, Snack> snackMap = fileIO.loadFile();  // 스낵 정보를 담고 있는 맵 가져오기
	    
	    System.out.println("\n================ 매출액 기준 베스트 간식 TOP 3 ================");
	    
	    if (top3Menus.isEmpty()) {
	        System.out.println("아직 판매된 간식이 없습니다.");
	    } else {
	        for (int i = 0; i < top3Menus.size(); i++) {
	            Map.Entry<String, Integer> entry = top3Menus.get(i);
	            String menuName = entry.getKey();
	            int quantity = entry.getValue();
	            int price = snackMap.get(menuName).getSnackPrice(); // 해당 간식의 가격 가져오기
	            int totalSales = price * quantity; // 총 매출액 계산
	            
	            System.out.printf("%d위: %-15s - 총 %3d개 판매 - 매출액: %,10d원\n", 
	                i + 1, menuName, quantity, totalSales);
	        }
	    }
	    
	    System.out.println("==========================================================");
	}
	
	/** 리뷰 목록 불러오기 */
	private void getReviewList() {
		
		//리뷰 목록 불러오는 메서드
		ReviewList.getInstance().showReviewList();
		
		
		//리뷰 메뉴
		String inputMonth = "";

		while(true) {
			
			System.out.println("원하시는 메뉴를 선택해주세요");
			System.out.println("1. 리뷰 댓글 달기 | 0. 관리자 메뉴로 돌아가기");
			
			String inputMenu = sc.nextLine();
			
			switch(inputMenu) {
				case "1" : addCommentToReview();
					break;
				case "0" : 
					return;
				default : System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
					break;
			}
		}
	}



	/** 리뷰에 댓글 달기 */
	private void addCommentToReview() {
		
		System.out.println("댓글을 달고 싶으신 글의 번호를 입력해주세요.");
		
		int inputLaneNum = Integer.parseInt(sc.nextLine());
	
		//댓글달기
		System.out.println("댓글 내용을 입력해주세요.");
		String inputReply = sc.nextLine(); 
		
		int reviewNum = ReviewList.getInstance().getLastReviewNum();
		
		ReviewList.getInstance().addReview(new Review(reviewNum, inputLaneNum, inputReply));
		ReviewList.getInstance().saveToFile();
		
		try { 
			System.err.println("댓글이 등록되었습니다.");
		}catch(Exception e){
			System.err.println("댓글 등록 중 오류가 발생했습니다."); 
		} 
	} 
	
	/** 금지어 관리 */
	private void manageBannedWords() {

		while(true) {
		
			System.out.println("금지어 관리 페이지입니다.");
			System.out.println("1. 금지어 목록 보기 | 2. 금지어 추가 | 3. 금지어 삭제 | 0. 관리자 메뉴로 돌아가기");
			
			String inputMenu = sc.nextLine();
			
			switch(inputMenu) {
				case "1" : PostFilter.printBannedWords();
					break;
				case "2" : addBanndWord();
					break;
				case "3" : delBannedWord();
					break;
				case "0" : 
					return;
				default : System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
					break;
			}
		}	
	}

	/** 금지어 추가 */ 
	private void addBanndWord() {
		
		System.out.println("추가하실 금지어를 입력해주세요.");
		String inputMenu = sc.nextLine();
		
		PostFilter.addBannedWordToFile(inputMenu);
		
	}


	/** 금지어 삭제 */
	private void delBannedWord() {
		System.out.println("삭제하실 금지어를 입력해주세요.");
		String inputMenu = sc.nextLine();
		
		PostFilter.delBannedWordToFile(inputMenu);
	}



}
