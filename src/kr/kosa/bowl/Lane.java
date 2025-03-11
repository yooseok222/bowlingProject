package kr.kosa.bowl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import kr.kosa.bowl.factory.OrderFactory;
import kr.kosa.bowl.factory.ReceiptFactory;
import lombok.Data;

@Data
public class Lane {
	private int laneNum; // 레인넘버
	private int headCnt; // 인원수
	private int shoesCnt; // 신발갯수
	private boolean isClean; // 청소여부
	private String selectedAt; // 레인선택시간
	private int gameCnt; // 게임카운트
	private Game game; // 게임객체
	private List<Map<String, Integer>> orderMenuList;
	private Profit profit;

	Scanner sc = new Scanner(System.in);

	/* Lane 생성자 */
	public Lane(Profit profit) {
		this.profit = profit;
		this.game = new Game();
		this.orderMenuList = new ArrayList<>();
		this.selectedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
		this.gameCnt = 0;

	}

	/* 🎳 레인 사용 메서드 (Menu에서 호출됨) */
	public void useLane() {
		if (!isClean) { // 레인이 사용 중이면
			System.err.println("⚠ 현재 레인은 사용 중입니다. 다른 레인을 선택해주세요.");
			return;
		}

		System.out.printf("\n🎳 %d번 레인을 사용합니다...\n", laneNum);
		this.isClean = false; // 사용 중으로 변경
		startLane(); // 게임 시작

	}

	/* 🎮 게임을 시작하는 메서드 */
	public void startLane() {
		inputHeadAndShoes(); // 인원 및 신발 선택

		boolean isGameFinished = false; // 게임이 끝났는지 확인하는 플래그

		while (!isGameFinished) {
			isGameFinished = selectSnackOrBowl(); // 간식 또는 게임 선택
		}
		// 게임이 진행된 경우에만 영수증을 추가
	    Receipt receipt = showReceiptInLane();
	    if (receipt != null) {
	        profit.addReceipt(receipt);
	    }
		//profit.addReceipt(showReceiptInLane()); // 영수증을 출력하고 바로 총매출에 추가
	}

	// 1. 인원수 입력 및 신발선택
	private void inputHeadAndShoes() {
		// 인원수 입력 (예외 처리 완료)
		while (true) {
			try {
				System.out.print("인원수를 입력하세요 (최대 4명) : ");
				this.headCnt = Integer.parseInt(sc.nextLine().trim());
				if (this.headCnt < 1 || this.headCnt > 4) {
					System.out.println("인원수는 1~4명 입니다. 다시 입력하세요.");
				} else {
					break;
				}
			} catch (NumberFormatException e) {
				System.out.println("유효한 숫자를 입력하세요.");
			}
		}

		// 신발 갯수 입력 (예외 처리 완료)
		while (true) {
			try {
				System.out.print("신발 갯수를 입력하세요 : ");
				this.shoesCnt = Integer.parseInt(sc.nextLine().trim());
				// 신발 개수가 인원 수보다 많으면 다시 입력 요구
				if (this.shoesCnt < 0 || this.shoesCnt > this.headCnt) {
					System.out.println("신발 갯수는 최소 0개, 최대 " + this.headCnt + "개까지 가능합니다. 다시 입력하세요.");
				} else {
					break;
				}
			} catch (NumberFormatException e) {
				System.out.println("유효한 숫자를 입력하세요.");
			}
		}

	}

	// 2. 간식먹을지 게임할지 메서드
	private boolean selectSnackOrBowl() {

		while (true) {
			try {
				System.out.println("\n✨🌟====== 선택 메뉴 ======🌟✨");
				System.out.println("🍿 1. 간식 구매");
				System.out.println("🎮 2. 게임 시작");
				System.out.println("🧾 3. 결제 및 종료");
				System.out.println("================================");
				System.out.print("🔹 원하는 옵션을 선택하세요 (1, 2, 3) ▶ ");

				int cmd = Integer.parseInt(sc.nextLine().trim());

				if (cmd == 1) {
					// 간식
					selectSnack();
					return false; // 게임 종료 아님
				} else if (cmd == 2) {
					// 게임시작
					selectBowl();
					return false; // 게임 종료 아님
				} else if (cmd == 3) {
	                if (gameCnt == 0) { // 게임을 진행하지 않았을 경우
	                    System.err.println("⚠ 게임을 진행하지 않아 결제를 진행할 수 없습니다.");
	                    return false; // 결제하지 않고 메뉴로 돌아감
	                }
	                return true; // 게임 종료
	            } else {
					System.out.println("⚠ 1, 2 또는 3을 입력해주세요.");
				}
			} catch (NumberFormatException e) {
				System.out.println("유효한 숫자를 입력하세요.");
			}
		}
	}

	// 2-1.간식 메서드
	private void selectSnack() {
		Order order = OrderFactory.createOrder();
		Map<String, Integer> orderedMenu = order.orderMenu();
		orderMenuList.add(orderedMenu);

	}

	// 2-2.게임 메서드
	private void selectBowl() {
		gameCnt++;
		game.start(this.headCnt, this.shoesCnt);

		// 게임이 끝난 후 다시 선택하도록 루프 유지
		System.out.println("\n🎳 게임이 끝났습니다! 추가 게임을 진행하거나 결제를 진행하세요.");
	}

	/* 3. 결제 및 영수증 출력 */
	private Receipt showReceiptInLane() {
		// 게임을 진행한 경우에만 영수증 생성
		if (gameCnt > 0) {
			System.out.println("\n🧾 영수증을 생성합니다...");
			Receipt receipt = ReceiptFactory.createReceipt(this); // 현재 Lane 객체를 전달
			receipt.showReceipt();
			return receipt;
		} else {
			System.out.println("⚠ 게임을 진행하지 않아 게임 비용이 청구되지 않습니다.");
			return null; // 영수증을 생성하지 않음
		}
	}

}
