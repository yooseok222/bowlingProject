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
	public Lane(Game game) {
		
		this.game = game;  // 게임 객체 초기화
        this.orderMenuList = new ArrayList<>();

        inputHeadAndShoes();  // 인원수 및 신발 갯수 입력 메서드 호출
        selectGameOrSnack();  // 간식 또는 게임 선택 메서드 호출
    }
	
	/*인원수 및 신발 갯수 입력 메서드*/
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

	/*간식 또는 게임 선택 메서드*/
	private void selectGameOrSnack() {

		while (true) {
			try {
				System.out.println("1. 간식구매");
				System.out.println("2. 게임시작");
				int gameOrSnackChoice = Integer.parseInt(sc.nextLine().trim());

				if (gameOrSnackChoice == 1) {
					orderMenuList = new ArrayList<>();
					Order order = OrderFactory.createOrder();
					Map<String, Integer> orderedMenu = order.orderMenu();
					orderMenuList.add(orderedMenu);
					System.out.println("간식이 추가되었습니다.");
					// 일단 주석
					game.Start(this.headCnt, this.shoesCnt);
				} else if (gameOrSnackChoice == 2) {
					game.Start(this.headCnt, this.shoesCnt);
				} else {
					System.out.println("1 또는 2를 입력해주세요.");
					continue;
				}
			} catch (NumberFormatException e) {
				System.out.println("유효한 숫자를 입력하세요.");
			}
		}
	}

	/*
	 * @Override public String toString() { return "headCnt : " + headCnt +
	 * " shoesCnt : " + shoesCnt + " selectedAt : " + selectedAt; }
	 */
	/*
	 * // 인원수 선택 및 신발대여 여부 public void useLane() { // 메뉴 주문할때 마다 해줘야함 메뉴를 가지고오고 싶으면
	 * 사용 orderMenuList = new ArrayList<>(); Order order =
	 * OrderFactory.createOrder(); Map<String, Integer> orderedMenu =
	 * order.orderMenu(); orderMenuList.add(orderedMenu);
	 * 
	 * 
	 * }
	 */

}
