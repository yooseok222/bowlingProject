package kr.kosa.bowl;

import java.util.Scanner;

public class Menu {

	static Lane[] lanes = new Lane[6];

	Menu() {
		for (int i = 0; i < 6; i++) {
			lanes[i] = new Lane();
			lanes[i].setClean(true);
		}
	}

	public void start() {
		Scanner sc = new Scanner(System.in);

		while (true) {
			System.out.println("1. 볼링게임하기\n9. 관리자 메뉴화면\n0. 종료");
			int input = Integer.parseInt(sc.nextLine());
			switch (input) {
			case 9:
				Manager manager = new Manager();
				manager.validateManager();
				break;
			case 1:
				selectLane(sc);
				break;
			case 0:
				System.out.println("안녕히 가세요!");
				break;
			default:
				System.out.println("0~2만 입력하세요.");
			}

		}
	}

	private void selectLane(Scanner sc) {
		System.out.println("레인을 선택해주세요.(1~6)");
		while (true) {
			printLaneAvail();
			try {
				int laneNum = Integer.parseInt(sc.nextLine());

				if (laneNum <= 6 && laneNum >= 1) {
					lanes[laneNum].useLane();
					lanes[laneNum].setClean(false);
					break;
				} else {
					System.out.println("1~6만 입력하실 수 있습니다.");
				}
			} catch (NumberFormatException e) {
				System.out.println("1~6만 입력하실 수 있습니다.");
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
