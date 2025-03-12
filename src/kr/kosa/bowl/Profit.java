package kr.kosa.bowl;

import java.io.Serializable;
import java.util.ArrayList;
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
}
