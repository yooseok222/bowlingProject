package kr.kosa.bowl;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import lombok.Data;

@Data
public class Order {
	private List<Integer> orderList;
	private Scanner sc;

	// 생성자
	public Order() {
		orderList = new ArrayList<>();
		sc = new Scanner(System.in);
	}

	// 상품 주문
	public void orderMenu() {
		while (true) {
			System.out.println("주문 메뉴에 오신 걸 환영합니다.");
			System.out.println("입력하신 번호로 메뉴를 선택해주세요.");
			System.out.println("1. 전체 메뉴 조회");
			System.out.println("2. 장바구니에 메뉴 담기");
			System.out.println("3. 현재 장바구니 현황 보기");
			System.out.println("4. 주문을 종료하고 게임으로 돌아가기");

			int command = Integer.parseInt(sc.nextLine());

			switch (command) {
			case 1:
				printSnackList();
				break;
			case 2:
				orderSnack();
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				System.out.println("주문이 모두 종료되었습니다.");
				sc.close();
				return;
			}
		}
	}

	// 전체 메뉴 출력
	private void printSnackList() {

	}

	// 장바구니에 간식 추가
	private void orderSnack() {
		System.out.println("추가하실 메뉴명을 입력해주세요.");
		String snackName = sc.nextLine();
		System.out.println("주문하실 수량을 입력해주세요.");
		int snackCnt = Integer.parseInt(sc.nextLine());

	}
}