package kr.kosa.bowl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.kosa.bowl.file.ProfitFileHandler;

public class Profit implements Serializable {
    private static final long serialVersionUID = 3515613435L;
    private static Profit instance;
    private List<Receipt> receiptList;
    private static final ProfitFileHandler fileHandler = new ProfitFileHandler();

    public Profit() {
        this.receiptList = new ArrayList<>();
    }

    public static Profit getInstance() {
        if (instance == null) {
            instance = new Profit();
            instance.loadReceipts(); // 파일에서 기존 영수증 불러오기
        }
        return instance;
    }

    /** 영수증 추가 후 파일 저장 */
    public void addReceipt(Receipt receipt) {
        receiptList.add(receipt);
        fileHandler.saveReceiptsToFile(receiptList); // 파일에 저장
    }

    /** 저장된 영수증 불러오기 */
    public void loadReceipts() {
        List<Receipt> loadedReceipts = fileHandler.loadReceiptsFromFile();
        if (!loadedReceipts.isEmpty()) {
            this.receiptList = loadedReceipts;
        }
    }

    /** 📌 **영수증 스타일로 전체 매출 출력 (기존 `showReceiptList()` 대체)** */
    public void showReceiptList() {
        if (receiptList.isEmpty()) {
            System.out.println("🔹 저장된 영수증이 없습니다.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("==============================================\n");
        sb.append("               📊  전체 매출 조회  📊           \n");
        sb.append("==============================================\n\n");

        int index = 1;
        for (Receipt receipt : receiptList) {
            sb.append("==============================================\n");
            sb.append(String.format(" 영수증 #%02d\n", index++));
            sb.append("==============================================\n");
            sb.append(String.format(" 거래 일시    : %s\n", receipt.getLane().getSelectedAt()));
            sb.append(String.format(" 총 인원 수   : %d 명\n", receipt.getLane().getHeadCnt()));
            sb.append(String.format(" 신발 대여    : %d 켤레\n", receipt.getLane().getShoesCnt()));
            sb.append(String.format(" 게임 횟수    : %d 번\n\n", receipt.getLane().getGameCnt()));

            sb.append("----------------------------------------------\n");
            sb.append(String.format(" %-20s %6s %12s\n", "상품명", "수량", "가격(₩)"));
            sb.append("----------------------------------------------\n");

            for (Map.Entry<String, Integer> entry : receipt.getMergedOrders().entrySet()) {
                String itemName = entry.getKey();
                int quantity = entry.getValue();
                int price = receipt.getSnackMap().get(itemName).getSnackPrice() * quantity;
                sb.append(String.format(" %-20s %6d %,12d 원\n", itemName, quantity, price));
            }

            sb.append("----------------------------------------------\n");
            sb.append(String.format(" %-20s %6d %,12d 원\n", "게임 비용", receipt.getLane().getHeadCnt(),
                    receipt.getLane().getHeadCnt() * receipt.getLane().getGameCnt() * receipt.getGameFee()));
            sb.append(String.format(" %-20s %6d %,12d 원\n", "신발 대여 비용", receipt.getLane().getShoesCnt(),
                    receipt.getLane().getShoesCnt() * receipt.getShoesFee()));
            sb.append("==============================================\n");
            sb.append(String.format(" 총 결제 금액 : %27s 원\n", String.format("%,12d", receipt.getTotalFee())));
            sb.append("==============================================\n\n");
        }

        System.out.println(sb.toString());
    }
    
    /** 전체 기간 게임비 매출 조회 */
    public int getTotalGameProfit() {
        return receiptList.stream()
                .mapToInt(receipt -> receipt.getGameFee() * receipt.getLane().getHeadCnt() * receipt.getLane().getGameCnt())
                .sum();
    }

    /** 전체 기간 신발 대여비 매출 조회 */
    public int getTotalShoesProfit() {
        return receiptList.stream()
                .mapToInt(receipt -> receipt.getShoesFee() * receipt.getLane().getShoesCnt())
                .sum();
    }

    /** 전체 기간 간식 매출 조회 */
    public int getTotalSnackProfit() {
        return receiptList.stream()
                .mapToInt(receipt -> {
                    int snackTotal = 0;
                    Map<String, Integer> mergedOrders = receipt.getMergedOrders();
                    for (Map.Entry<String, Integer> entry : mergedOrders.entrySet()) {
                        String snackName = entry.getKey();
                        int quantity = entry.getValue();
                        int price = receipt.getSnackMap().get(snackName).getSnackPrice();
                        snackTotal += price * quantity;
                    }
                    return snackTotal;
                })
                .sum();
    }
    
    /** 전체 기간 매출 상세 조회 (각 카테고리별 금액과 총액) */
    public Map<String, Integer> getTotalProfitDetails() {
        Map<String, Integer> profitDetails = new HashMap<>();
        
        int gameProfit = getTotalGameProfit();
        int shoesProfit = getTotalShoesProfit();
        int snackProfit = getTotalSnackProfit();
        int totalProfit = gameProfit + shoesProfit + snackProfit;
        
        profitDetails.put("게임비", gameProfit);
        profitDetails.put("신발대여비", shoesProfit);
        profitDetails.put("간식매출", snackProfit);
        profitDetails.put("총매출", totalProfit);
        
        return profitDetails;
    }
    
    
    /** 월별 전체 매출 조회 */
    public int getMonthlyProfit(String inputMonth) {
        int month = Integer.parseInt(inputMonth);
        return receiptList.stream()
                .filter(receipt -> getMonth(receipt.getLane().getSelectedAt()) == month)
                .mapToInt(Receipt::getTotalFee)
                .sum();
    }
    
    public Map<String, Integer> getMonthlyProfitDetails(String inputMonth) {
        Map<String, Integer> profitDetails = new HashMap<>();
        
        int gameProfit = getMonthlyGameProfit(inputMonth);
        int shoesProfit = getMonthlyShoesProfit(inputMonth);
        int snackProfit = getMonthlySnackProfit(inputMonth);
        int totalProfit = gameProfit + shoesProfit + snackProfit;
        
        profitDetails.put("게임비", gameProfit);
        profitDetails.put("신발대여비", shoesProfit);
        profitDetails.put("간식매출", snackProfit);
        profitDetails.put("총매출", totalProfit);
        
        return profitDetails;
    }

    /** 월별 게임비 매출 조회 */
    public int getMonthlyGameProfit(String inputMonth) {
        int month = Integer.parseInt(inputMonth);
        return receiptList.stream()
                .filter(receipt -> getMonth(receipt.getLane().getSelectedAt()) == month)
                .mapToInt(receipt -> receipt.getGameFee() * receipt.getLane().getHeadCnt() * receipt.getLane().getGameCnt())
                .sum();
    }

    /** 월별 신발 대여비 매출 조회 */
    public int getMonthlyShoesProfit(String inputMonth) {
        int month = Integer.parseInt(inputMonth);
        return receiptList.stream()
                .filter(receipt -> getMonth(receipt.getLane().getSelectedAt()) == month)
                .mapToInt(receipt -> receipt.getShoesFee() * receipt.getLane().getShoesCnt())
                .sum();
    }

    /** 월별 간식 매출 조회 */
    public int getMonthlySnackProfit(String inputMonth) {
        int month = Integer.parseInt(inputMonth);
        return receiptList.stream()
                .filter(receipt -> getMonth(receipt.getLane().getSelectedAt()) == month)
                .mapToInt(receipt -> {
                    int snackTotal = 0;
                    Map<String, Integer> mergedOrders = receipt.getMergedOrders();
                    for (Map.Entry<String, Integer> entry : mergedOrders.entrySet()) {
                        String snackName = entry.getKey();
                        int quantity = entry.getValue();
                        int price = receipt.getSnackMap().get(snackName).getSnackPrice();
                        snackTotal += price * quantity;
                    }
                    return snackTotal;
                })
                .sum();
    }

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
}
