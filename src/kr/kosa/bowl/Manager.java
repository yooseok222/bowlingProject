package kr.kosa.bowl;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Manager {

	Scanner sc = new Scanner(System.in);

	Map<String, Snack> snackMenu = new LinkedHashMap<>(SnackFile.readSnackFile());
	
	
	/** 관리자 인증 */
	public void validateManager() {
		boolean isCorrect = false;
		
		do {

			System.out.print("아이디를 입력하세요 : "); 
			String inputId = sc.nextLine();
			
			System.out.print("비밀번호를 입력하세요 : ");
			String inputPw = sc.nextLine();			
			
			isCorrect = (inputId.equals("admin") && inputPw.equals("1234")) ? true : false;
			
			if(!isCorrect) System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
		
		}while(!isCorrect);
		
		getAdminMenu();
	};
	
	/** 관리자 메뉴 */
	private void getAdminMenu() { 
		
		while(true) {
			System.out.println("관리자 메뉴입니다.");
			System.out.println("1. 레인 청소 | 2. 상품 관리 | 3. 전체 매출 조회 | 0. 초기 화면으로 돌아가기");
	
			String inputMenu = sc.nextLine();
			
			switch(inputMenu) {
				case "1" : cleanLane();
					break;
				case "2" : getSnackMenu();
					break;
				case "3" : getProfit();
					break;
				default : System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
					break;
			}
		}
	
	}
	
	/** 레인 청소 */
	private void cleanLane() {
		
		System.out.println("쓱싹쓱싹");
	
	};

	/** 상품 관리 메뉴 */
	private void getSnackMenu() {
		
		System.out.println("상품 관리 메뉴 진입");
		
		while(true) {
		
			System.out.println("상품 관리 페이지입니다.");
			System.out.println("1. 전체 상품 조회 | 2. 상품 추가 | 3. 상품 수정 | 4. 상품 삭제 | 5. 상품 검색 0. 관리자 메뉴로 돌아가기");
			
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
				case "0" : getAdminMenu();
					break;
				default : System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
					break;
			}
		}	
	}

	/** 상품 추가(오버로딩) */
	private void addSnack() {
		
		System.out.println("상품을 추가합니다.");

		System.out.print("상품 이름을 입력해주세요 : ");
		String snackName = sc.nextLine();

		System.out.print("상품 가격을 입력해주세요 : ");
		int snackPrice = Integer.parseInt(sc.nextLine());
		
		System.out.print("상품 수량을 입력해주세요 : ");
		int snackCnt = Integer.parseInt(sc.nextLine());
			
		Snack snack = new Snack(snackName, snackPrice, snackCnt);
		
		snackMenu.put(snackName, snack);
		SnackFile.makeSnackFile(snackMenu);
		
		System.out.println("상품 추가되었습니다.");
	};
	

	/** 상품 삭제 */
	private void delSnack() {
		
		System.out.print("삭제하실 상품 이름을 입력하세요 : ");
		String snackName = sc.nextLine();
//		
//		for(int i = 0 ; i<snackList.size(); i++) {
//			if(snackName.equals(snackList.get(i).getSnackName())) {
//				snackList.remove(i);
//			}
//		}
		
		getSnackList(); 
	};
	
	/** 상품 수정(오버로딩) */
	private void updateSnack() {
		System.out.println("");
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
		System.out.print("검색하실 상품 이름을 입력하세요 : ");
		String snackName = sc.nextLine();
		
//		for(Snack e : snackList) {
//			if(snackName.equals(e.getSnackName())) {
//				System.out.println("상품명 : " + e.getSnackName() + " | 가격 : " + e.getSnackPrice() + " | 수량 : " + e.getSnackCnt());
//			}
//		}
	};
	
	/** 전체 매출 조회 */
	private void getProfit() {
		System.out.println("전체 매출 조회 페이지");
	};
}
