package kr.kosa.bowl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.kosa.bowl.file.FileSaver;
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
	
	public List<Receipt> getReceiptList() {
	    return new ArrayList<>(this.receiptList); // 방어적 복사본 반환
	}
	
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
	public int getMonthlyProfit(String inputMonth) {
		int month = Integer.parseInt(inputMonth);

		return receiptList.stream().filter(receipt -> getMonth(receipt.getLane().getSelectedAt()) == month)
				.mapToInt(Receipt::getTotalFee).sum();
		
		//게임비
//		return receiptList.stream().filter(receipt -> getMonth(receipt.getLane().getSelectedAt()) == month)
//				.mapToInt(Receipt::getGameFee).sum();
	
		//대화비
//		return receiptList.stream().filter(receipt -> getMonth(receipt.getLane().getSelectedAt()) == month)
//				.mapToInt(Receipt::getShoesFee).sum();
	
//		//간식비용
//		return receiptList.stream().filter(receipt -> getMonth(receipt.getLane().getSelectedAt()) == month)
//				.flatMap(map -> entrySet().stream()).map(Map.Entry::getValue).sum();
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
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();

	    sb.append("총 영수증 수: ").append(receiptList.size()).append("\n");
	    sb.append("==== 영수증 목록 ====\n");

	    int index = 1;
	    for (Receipt receipt : receiptList) {
	        sb.append("\n--- 영수증 #").append(index).append(" ---\n");
	        sb.append("레인 번호: ").append(Integer.toString(receipt.getLane().getLaneNum())).append("\n");
	        sb.append("날짜: ").append(receipt.getLane().getSelectedAt()).append("\n");
	        sb.append("인원수: ").append(receipt.getLane().getHeadCnt()).append("\n");
	        sb.append("신발 개수: ").append(receipt.getLane().getShoesCnt()).append("\n");
	        sb.append("게임 비용: ").append(receipt.getGameFee()).append("\n");
	        sb.append("신발 비용: ").append(receipt.getShoesFee()).append("\n");
	        sb.append("총 비용: ").append(receipt.getTotalFee()).append("\n");

	        // 주문 내역 출력
	        Map<String, Integer> orders = receipt.getMergedOrders();
	        if (orders != null && !orders.isEmpty()) {
	            sb.append("주문 내역:\n");
	            for (Map.Entry<String, Integer> entry : orders.entrySet()) {
	                sb.append(" - ").append(entry.getKey()).append(": ").append(entry.getValue()).append("개\n");
	            }
	        } else {
	            sb.append("주문 내역 없음\n");
	        }

	        index++;
	    }
	    
	    return sb.toString();
	}
}