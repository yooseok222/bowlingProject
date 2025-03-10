package kr.kosa.bowl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.kosa.bowl.file.ProfitFileHandler;
import lombok.ToString;

@ToString
public class Profit implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3515613435L;

	private static Profit instance; // 유일한 인스턴스

	private List<Receipt> receiptList;

	// 생성자를 private으로 설정 -> new 연산 불가
	private Profit() {
		this.receiptList = new ArrayList<>();
	}

	// Profit 인스턴스를 제공하는 메소드
	public static Profit getInstance() {
		if (instance == null) {
			instance = new Profit();
		}
		return instance;
	}

	// 영수증 추가
	public void addReceipt(Receipt receipt) {
		receiptList.add(receipt);
	    // 3. 업데이트된 Profit 저장
	    ProfitFileHandler profitHandler = new ProfitFileHandler();
	    profitHandler.saveProfit();
	}

	// 영수증 리스트 출력
	public void showReceiptList() {
		StringBuilder sb = new StringBuilder();
		for (Receipt receipt : receiptList) {
			sb.append(getReceiptSummary(receipt)).append("\n");
		}
		System.out.println(sb);
	}

	// 개별 영수증 요약
	private String getReceiptSummary(Receipt receipt) {
		StringBuilder sb = new StringBuilder();
		sb.append("날짜 : ").append(receipt.getLane().getSelectedAt()).append("\n").append("인원 수 : ")
				.append(receipt.getLane().getHeadCnt()).append("\n").append("신발 개수 : ")
				.append(receipt.getLane().getShoesCnt()).append("\n");

		for (Map.Entry<String, Integer> m : receipt.getMergedOrders().entrySet()) {
			sb.append(m.getKey()).append(" : ").append(m.getValue()).append("개\n");
		}

		sb.append("총 금액 : ").append(receipt.getTotalFee()).append("\n");

		return sb.toString();
	}

	// 특정 월의 총 수익 계산
	public int getMonthlyProfit(int month) {
		return receiptList.stream().filter(receipt -> getMonth(receipt.getLane().getSelectedAt()) == month)
				.mapToInt(Receipt::getTotalFee).sum();
	}

	// 날짜에서 월 추출
	private int getMonth(String date) {
		return Integer.parseInt(date.split("\\.")[1]);
	}

	// 가장 많이 팔린 메뉴 TOP 3
	public List<Map.Entry<String, Integer>> getTop3Menus() {
		Map<String, Integer> totalOrders = new HashMap<>();

		for (Receipt receipt : receiptList) {
			for (Map.Entry<String, Integer> entry : receipt.getMergedOrders().entrySet()) {
				totalOrders.put(entry.getKey(), totalOrders.getOrDefault(entry.getKey(), 0) + entry.getValue());
			}
		}

		return totalOrders.entrySet().stream().sorted((a, b) -> b.getValue() - a.getValue()) // 내림차순 정렬
				.limit(3) // 상위 3개만 선택
				.toList();
	}
	
	public static void updateInstance(Profit loadedProfit) {
	    if (instance == null) {
	        instance = loadedProfit;
	    } else {
	        // 기존 인스턴스에 로드된 데이터 병합
	        instance.receiptList.clear();
	        instance.receiptList.addAll(loadedProfit.receiptList);
	    }
	}
}