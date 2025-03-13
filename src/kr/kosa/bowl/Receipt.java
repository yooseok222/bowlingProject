package kr.kosa.bowl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.kosa.bowl.storage.SnackFileIO;
import kr.kosa.bowl.util.AbstractFileIO;
import lombok.Data;

@Data
public class Receipt implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8194163212332L;

	private final int gameFee;
	private final int shoesFee;

	private int totalFee;
	private final Lane lane; // 불변성 보장
	private Map<String, Snack> snackMap;
	private List<Map<String, Integer>> orderMenuList;
	private Map<String, Integer> mergedOrders; // 통합 주문 내역

	// 생성자
	public Receipt(Lane lane) {
		this.gameFee = 5000;
		this.shoesFee = 2500;
		this.lane = lane;

		AbstractFileIO<Map<String, Snack>> fileIO = new SnackFileIO();
		this.snackMap = fileIO.loadFile();

		orderMenuList = lane.getOrderMenuList();
		this.totalFee = calculateTotalFee();
		mergeOrders();
	}

	// 총 정산 금액
	private int calculateTotalFee() {
		int total = 0; // 멱등성을 위해 로컬 변수 사용
		total += lane.getHeadCnt() * lane.getGameCnt() * gameFee; // 볼링 게임 비용
		total += lane.getShoesCnt() * shoesFee; // 신발 대여 비용

		// 음식 비용
		for (Map<String, Integer> map : orderMenuList) {
			for (Map.Entry<String, Integer> m : map.entrySet()) {
				int menuPrice = snackMap.get(m.getKey()).getSnackPrice();
				int menuCnt = m.getValue();
				total += menuPrice * menuCnt;
			}
		}
		return total;
	}

	// 영수증 상 모든 주문 내역을 하나의 맵으로 통합 (한 번만 수행)
	private void mergeOrders() {
		if (mergedOrders == null) { // 이미 계산된 적 없으면 실행
			mergedOrders = new HashMap<>();
			for (Map<String, Integer> map : orderMenuList) {
				for (Map.Entry<String, Integer> entry : map.entrySet()) {
					mergedOrders.put(entry.getKey(), mergedOrders.getOrDefault(entry.getKey(), 0) + entry.getValue());
				}
			}
		}
	}

	// 영수증 내역 출력
	public String showReceipt() {
		StringBuilder sb = new StringBuilder();
		sb.append("=============================================\n");
		sb.append("                🎳 영 수 증 🎳              \n");
		sb.append("=============================================\n\n");
		sb.append(String.format("거래 일시 : %s\n", lane.getSelectedAt()));
		sb.append(String.format("레인 번호 : %s\n", lane.getLaneNum()));
		sb.append(String.format("총 인원 수 : %-2d명\n", lane.getHeadCnt()));
		sb.append(String.format("신발 대여 : %-2d켤레\n", lane.getShoesCnt()));
		sb.append(String.format("게임 횟수 : %-2d번\n\n", lane.getGameCnt()));

		sb.append("---------------------------------------------\n");
		sb.append(String.format("%-20s %6s %10s\n", "상품명", "수량", "가격"));
		sb.append("---------------------------------------------\n");

		for (Map.Entry<String, Integer> entry : mergedOrders.entrySet()) {
			String itemName = entry.getKey();
			int quantity = entry.getValue();
			int price = snackMap.get(itemName).getSnackPrice() * quantity;
			sb.append(String.format("%-20s %6d %,10d 원\n", itemName, quantity, price));
		}

		sb.append("---------------------------------------------\n");
		sb.append(String.format("%-20s %6s %,10d 원\n", "게임 비용", lane.getHeadCnt(),
				lane.getHeadCnt() * lane.getGameCnt() * gameFee));
		sb.append(String.format("%-18s %6s %,10d 원\n", "신발 대여 비용", lane.getShoesCnt(), lane.getShoesCnt() * shoesFee));
		sb.append("---------------------------------------------\n");

		sb.append(String.format("총 결제 금액 : %24s 원\n", String.format("%,10d", totalFee)));
		sb.append("=============================================\n");
		sb.append("        💖 이용해주셔서 감사합니다 💖        \n");
		sb.append("=============================================\n");

		System.out.println(sb);
		return sb.toString();
	}
}