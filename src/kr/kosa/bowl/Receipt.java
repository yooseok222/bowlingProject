package kr.kosa.bowl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class Receipt implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8194165132L;

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
		this.snackMap = SnackFile.readSnackFile();
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
	public void showReceipt() {
		StringBuilder sb = new StringBuilder();
		sb.append("---영수증---\n");
		sb.append(lane.getSelectedAt()).append("\n");
		sb.append("총 인원 수 : ").append(lane.getHeadCnt()).append("\n");
		sb.append("총 대여한 신발 수 : ").append(lane.getShoesCnt()).append("\n");
		sb.append("---지금까지 주문하신 메뉴 내역---\n");
		for (Map.Entry<String, Integer> m : mergedOrders.entrySet()) {
			sb.append(m.getKey()).append(" : ").append(m.getValue()).append("개\n");
		}
		sb.append("---\n");
		sb.append("총 정산 금액 : ").append(totalFee).append("원 입니다.\n");
		System.out.println(sb);
	}
}
