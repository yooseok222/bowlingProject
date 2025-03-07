package kr.kosa.bowl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import kr.kosa.bowl.factory.OrderFactory;
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

	Scanner sc = new Scanner(System.in);

	/* Lane 생성자 */
	public Lane() {

		this.game = new Game();
		this.orderMenuList = new ArrayList<>();

	}

	/* 🎮 게임을 시작하는 메서드 */
    public void startLane() {
        inputHeadAndShoes();  // 인원 및 신발 선택
        
        boolean isGameFinished = false; // 게임이 끝났는지 확인하는 플래그

        while (!isGameFinished) {  
            isGameFinished = selectSnackOrBowl();  // 간식 또는 게임 선택
        }

        showReceipt();  // 🎯 모든 과정이 끝난 후 영수증 출력
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
					continue;
				}
				break;
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
				if (this.shoesCnt < 1 || this.shoesCnt > this.headCnt) {
					System.out.println("신발 갯수는 최소 1개, 최대 " + this.headCnt + "개까지 가능합니다. 다시 입력하세요.");
					continue;
				}
				break;
			} catch (NumberFormatException e) {
				System.out.println("유효한 숫자를 입력하세요.");
			}
		}
		orderMenuList = new ArrayList<>();

		// this.selectedAt =
		// LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));

	}

	// 2. 간식먹을지 게임할지 메서드
	private boolean selectSnackOrBowl() {
		while (true) {
			try {
				System.out.println("\n========== 선택 ==========");
				System.out.println("1. 간식 구매 🍿");
				System.out.println("2. 게임 시작 🎳"); 
				System.out.println("3. 결제 및 종료 🧾");
                System.out.print("선택하세요 (1, 2 또는 3) : ");
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
		orderMenuList = new ArrayList<>();
		Order order = OrderFactory.createOrder();
		Map<String, Integer> orderedMenu = order.orderMenu();
		orderMenuList.add(orderedMenu);

	}

	// 2-2.게임 메서드
	private void selectBowl() {
		game.start(this.headCnt, this.shoesCnt);

		
		// 게임이 끝난 후 다시 선택하도록 루프 유지
		System.out.println("\n🎮 게임이 종료되었습니다! 다시 선택해주세요.");
	}
	
	/* 3. 결제 및 영수증 출력 */
    private void showReceipt() {
        System.out.println("\n🧾 영수증을 생성합니다...");
        Receipt receipt = new Receipt(this); // 현재 Lane 객체를 전달
        receipt.showReceipt();
    }

}
