package kr.kosa.bowl;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import kr.kosa.bowl.exception.OrderException;
import kr.kosa.bowl.file.SnackFileHandler;
import lombok.Data;

@Data
public class Order {
	private Map<String, Integer> orderMap;
	private Map<String, Snack> snackMap;
	private Scanner sc;
	SnackFileHandler sf = new SnackFileHandler();
	
	// 생성자
	public Order() {
		sc = new Scanner(System.in);
		snackMap = sf.readSnackMap(); // 파일 읽기
		orderMap = new HashMap<>();
	}

	// 상품 주문
	public Map<String, Integer> orderMenu() {
		while (true) {
			System.out.println("\n✨🌟====== 주문 메뉴에 오신 걸 환영합니다. ======🌟✨");
			System.out.println("입력하신 번호로 메뉴를 선택해주세요.");
			System.out.println("1. 전체 메뉴 조회");
			System.out.println("2. 장바구니에 메뉴 담기");
			System.out.println("3. 현재 장바구니 현황 보기");
			System.out.println("4. 주문을 종료하고 게임으로 돌아가기");

			try {
				int command = Integer.parseInt(sc.nextLine().trim());

				switch (command) {
				case 1:
					printSnackMap();
					break;
				case 2:
					orderSnack(); // Scanner를 인자로 넘김
					break;
				case 3:
					printOrderMap();
					break;
				case 4:
					System.out.println("주문이 모두 종료되었습니다.");
					sf.saveSnackMap(snackMap); // 변경 사항 저장
					return orderMap;
				default:
					throw new OrderException("잘못된 번호입니다. 1~4 사이의 숫자를 입력하세요.");
				}
			} catch (NumberFormatException e) {
				System.err.println("[ERROR] 숫자 입력이 필요합니다.");
			} catch (OrderException e) {
				System.err.println("[ERROR] " + e.getMessage());
			}
		}
	}

	// 전체 메뉴 출력
	private void printSnackMap() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n========== [ 전체 상품 조회] ==========\n");
		sb.append(String.format("%-11s | %-11s | %-11s\n", "상품명", "가 격", "수 량"));
		sb.append("---------------------------------------\n");
		for (String key : snackMap.keySet()) {
			Snack snack = snackMap.get(key);
			sb.append(String.format("%-12s | %-13s | %-12s\n", snack.getSnackName(), snack.getSnackPrice(),
					snack.getSnackCnt()));
		}
		sb.append("=======================================\n");
		System.out.println(sb);
	}

	// 장바구니 현황 출력
	private void printOrderMap() {
		if (orderMap.isEmpty()) {
			System.out.println("[INFO] 장바구니가 비어 있습니다.");
			return;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("\n========== [ 장바구니 현황 ] ==========\n");

		// 헤더
		sb.append(String.format("%-20s | %s\n", "메뉴", "수량"));
		sb.append("---------------------------------------\n");

		// 데이터 출력
		for (Map.Entry<String, Integer> order : orderMap.entrySet()) {
			sb.append(String.format("%-20s | %d\n", order.getKey(), order.getValue()));
		}

		sb.append("=======================================\n");

		System.out.println(sb);
	}

	// 장바구니에 간식 추가
	private void orderSnack() {
		try {
			System.out.println("추가하실 메뉴명을 입력해주세요.");
			String snackName = sc.nextLine().trim();

			if (!snackMap.containsKey(snackName)) {
				throw new OrderException("존재하지 않는 메뉴입니다: " + snackName);
			}

			System.out.println("주문하실 수량을 입력해주세요.");
			int snackCnt = Integer.parseInt(sc.nextLine().trim());

			Snack orderedSnack = snackMap.get(snackName); // 주문받은 메뉴

			if (snackCnt > orderedSnack.getSnackCnt()) { // 재고 부족
				throw new OrderException("죄송합니다. 해당 메뉴는 " + orderedSnack.getSnackCnt() + "개만 남아 있습니다.");
			}

			// 재고 차감
			orderedSnack.setSnackCnt(orderedSnack.getSnackCnt() - snackCnt);
			snackMap.put(snackName, orderedSnack);

			// 주문 내역에 반영
			orderMap.put(snackName, orderMap.getOrDefault(snackName, 0) + snackCnt);

			System.out.println("[INFO] 주문이 완료되었습니다: " + snackName + " " + snackCnt + "개");

		} catch (NumberFormatException e) {
			System.err.println("[ERROR] 숫자를 입력해야 합니다.");
		} catch (OrderException e) {
			System.err.println("[ERROR] " + e.getMessage());
		}
	}
}