package kr.kosa.bowl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Receipt {
	private final int gameFee;
	private final int shoesFee;

	private int totalFee;
	private Lane lane;
	private Map<String, Snack> snackMap;
	private List<Map<String, Integer>> orderMenuList;

	// 생성자
	public Receipt(Lane lane) {
		this.gameFee = 5000;
		this.shoesFee = 2500;
		this.lane = lane;
		this.snackMap = SnackFile.readSnackFile();
		orderMenuList = lane.getOrderMenuList();
		this.totalFee = calculateTotalFee();
	}

	// 총 정산 금액
	private int calculateTotalFee() {
		totalFee += lane.getHeadCnt() * (lane.getGameCnt() + 1) * gameFee; // 볼링 게임 비용
		totalFee += lane.getShoesCnt() * shoesFee; // 신발 대여 비용

		// 음식 비용
		for (Map<String, Integer> map : orderMenuList) {
			for (Map.Entry<String, Integer> m : map.entrySet()) {
				int menuPrice = snackMap.get(m.getKey()).getSnackPrice();
				int menuCnt = m.getValue();

				totalFee += menuPrice * menuCnt;
			}
		}

		return totalFee;
	}

	// 영수증 내역 출력
	public void showReceipt() {
		StringBuilder sb = new StringBuilder();
		sb.append("---영수증---\n");
		sb.append("총 인원 수 : ").append(lane.getHeadCnt()).append("\n");
		sb.append("총 대여한 신발 수 : ").append(lane.getShoesCnt()).append("\n");
		sb.append("---지금까지 주문하신 메뉴 내역---\n");

		// 영수증 상 모든 주문 내역을 하나의 맵으로 통합
		Map<String, Integer> mergedOrders = new HashMap<>();

		for (Map<String, Integer> map : orderMenuList) {
			for (Map.Entry<String, Integer> entry : map.entrySet()) {
				mergedOrders.put(entry.getKey(), mergedOrders.getOrDefault(entry.getKey(), 0) + entry.getValue());
			}
		}

		for (Map.Entry<String, Integer> m : mergedOrders.entrySet()) {
			sb.append(m.getKey()).append(" : ").append(m.getValue()).append("개\n");
		}
		sb.append("---\n");
		sb.append("총 정산 금액 : ").append(totalFee).append("원 입니다.\n");
		System.out.println(sb);
	}
}
