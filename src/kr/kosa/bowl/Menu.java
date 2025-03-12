package kr.kosa.bowl;

import java.util.Scanner;

public class Menu {

	static Lane[] lanes = new Lane[6];
	private Profit profit; // 총 수익 -> 파일 존재 유무에 따라 초기화된 후 싱글톤 패턴으로 관리
	private ReviewList reviewList; // 총 리뷰 -> 파일 존재 유무에 따라 초기화된 후 싱글톤 패턴으로 관리
	private Snack snack;
	
	private Scanner sc;

	Menu() {
		sc = new Scanner(System.in);
		profit = Profit.getInstance();
		reviewList = ReviewList.getInstance();
		snack = Snack.getInstance();
		
		for (int i = 0; i < 6; i++) {
			lanes[i] = new Lane(profit, reviewList);
			lanes[i].setClean(true);
		}
	}

	public void start() {

		while (true) {
			System.out.println("1. 볼링게임하기\n2. 리뷰 조회하기\n9. 관리자 메뉴화면\n0. 종료");

			try {
				int input = Integer.parseInt(sc.nextLine());
				switch (input) {
				case 9:
					Manager manager = new Manager();
					manager.validateManager();
					break;
				case 1:
					selectLane();
					break;
				case 2:
					gotoReview();
					break;
				case 0:
					System.out.println("안녕히 가세요!");
					return;
				default:
					System.err.println("잘못된 숫자를 입력하셨습니다.");
				}
			} catch (NumberFormatException e) {
				System.err.println("0 ~ 1, 9만 입력 가능합니다.");
			}

		}
	}

	public void gotoReview() {

		System.out.println("1. 최신순으로 리뷰보기\n2. 별점순으로 리뷰보기\n0. 메뉴로 돌아가기");
		try {
			int input = Integer.parseInt(sc.nextLine());

			switch (input) {
			case 1:
				reviewList.showReviewList();
				break;
			case 2:
				reviewList.showSortByStarReviewList();
				break;
			case 0:
				return;
			default:
				System.err.println("0~2만 입력 가능합니다.\n기존 메뉴로 돌아갑니다.");
				break;
			}
		} catch (NumberFormatException e) {
			System.err.println("0~2만 입력 가능합니다.\n기존 메뉴로 돌아갑니다.");
		}

	}

	private void selectLane() {
		System.out.println("레인을 선택해주세요.(1~6)");
		while (true) {
			printLaneAvail();
			try {
				int laneNum = Integer.parseInt(sc.nextLine());

				if (laneNum <= 6 && laneNum >= 1) {
					lanes[laneNum - 1].setLaneNum(laneNum); // laneNum 설정
					lanes[laneNum - 1].useLane();
					break;
				} else {
					System.err.println("1~6만 입력하실 수 있습니다.");
				}
			} catch (NumberFormatException e) {
				System.err.println("1~6만 입력하실 수 있습니다.");
			}

		}

	}

	static void printLaneAvail() {
		System.out.println("=====================================");
		for (int i = 0; i < lanes.length; i++) {
			System.out.printf("%10d %10s\n", i + 1, lanes[i].isClean() ? "사용가능" : "사용불가(청소필요)");
		}
		System.out.println("=====================================");
	}
}
