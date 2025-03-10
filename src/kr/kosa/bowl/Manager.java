package kr.kosa.bowl;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Manager {

	Scanner sc = new Scanner(System.in);
	
	SnackFileHandler snackFile = new SnackFileHandler();
	Map<String, Snack> snackMenu = snackFile.readSnackMap();
			
			
	
	/** 관리자 인증 */
	public void validateManager() {
		boolean isCorrect = false;
		
		do {
 
			System.out.print("아이디를 입력하세요 : "); 
			String inputId = sc.nextLine();
			
			System.out.print("비밀번호를 입력하세요 : ");
			String inputPw = sc.nextLine();			
			
			isCorrect = (inputId.equals("admin") && inputPw.equals("1234")) ? true : false;
			
			if(!isCorrect) System.err.println("잘못 입력하셨습니다. 다시 입력해주세요.");
		
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
				case "0": 
					return;
				default : System.err.println("잘못 입력하셨습니다. 다시 입력해주세요.");
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
		String snackName = null;
		int snackPrice = 0;
		int snackCnt = 0;

		do {
			System.out.print("상품 이름을 입력해주세요 : ");
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
		snackFile.saveSnackMap(snackMenu);
		
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
		
		System.out.print("삭제하실 상품 이름을 입력하세요 : ");
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
					snackFile.saveSnackMap(snackMenu);
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
		System.out.print("수정하실 상품의 이름을 입력해주세요 : ");
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
				case "0" : getAdminMenu();
					break;
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
		System.out.print("검색하실 상품 이름을 입력하세요 : ");
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
			System.out.print("수정하실 상품의 새 이름을 입력해주세요");
			
			newName = sc.nextLine();
			
			if(newName.isBlank()) {
				System.err.println("상품명이 입력되지 않았습니다.");
			}else {
				Snack snackNameChanged = snackMenu.remove(snackName);
				if(snackNameChanged != null) {
					snackNameChanged.setSnackName(newName);			
					snackMenu.put(newName, snackNameChanged);
					snackFile.saveSnackMap(snackMenu);
				}
				System.out.println("상품 이름 수정이 완료되었습니다.");	
			}	
		
		}while(newName.isBlank());
		
		
	}
	
	/** 상품 가격 수정
	 * @param snackName */
	private void updateSnackPrice(String snackName) {
		System.out.print("수정하실 상품의 새 가격을 입력해주세요 : ");
		try {
			int newPrice = Integer.parseInt(sc.nextLine());			
			Snack snackPriceChanged = snackMenu.get(snackName);
			if(snackPriceChanged != null) {
				snackPriceChanged.setSnackPrice(newPrice);
				snackMenu.put(snackName, snackPriceChanged);
				snackFile.saveSnackMap(snackMenu);
			}
			System.out.println("상품 가격 수정이 완료되었습니다.");
		} catch (NumberFormatException e) {
			System.err.println("가격이 입력되지 않았습니다.");
		}

	}
	
	/** 전체 매출 조회 */
	private void getProfit() {
		System.out.println("전체 매출 조회 페이지"); 
	};
}
