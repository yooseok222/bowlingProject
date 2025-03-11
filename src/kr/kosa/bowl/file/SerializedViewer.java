package kr.kosa.bowl.file;

import kr.kosa.bowl.Profit;
import kr.kosa.bowl.Receipt;
import java.io.*;
import java.util.List;
import java.util.Map;

public class SerializedViewer {
    public static void main(String[] args) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("profit.txt"))) {
            Object obj = ois.readObject();
            System.out.println("객체 클래스: " + obj.getClass().getName());
            
            if (obj instanceof Profit) {
                Profit profit = (Profit) obj;
                List<Receipt> receipts = profit.getReceiptList();
                
                if (receipts == null || receipts.isEmpty()) {
                    System.out.println("영수증 리스트가 비어있습니다.");
                    return;
                }
                
                System.out.println("총 영수증 수: " + receipts.size());
                System.out.println("==== 영수증 목록 ====");
                
                int index = 1;
                for (Receipt receipt : receipts) {
                    System.out.println("\n--- 영수증 #" + index + " ---");
                    System.out.println("날짜: " + receipt.getLane().getSelectedAt());
                    System.out.println("인원수: " + receipt.getLane().getHeadCnt());
                    System.out.println("신발 개수: " + receipt.getLane().getShoesCnt());
                    System.out.println("게임 비용: " + receipt.getGameFee());
                    System.out.println("신발 비용: " + receipt.getShoesFee());
                    System.out.println("총 비용: " + receipt.getTotalFee());
                    
                    // 주문 내역 출력
                    Map<String, Integer> orders = receipt.getMergedOrders();
                    if (orders != null && !orders.isEmpty()) {
                        System.out.println("주문 내역:");
                        for (Map.Entry<String, Integer> entry : orders.entrySet()) {
                            System.out.println("  - " + entry.getKey() + ": " + entry.getValue() + "개");
                        }
                    } else {
                        System.out.println("주문 내역 없음");
                    }
                    
                    index++;
                }
            } else {
                System.out.println("객체가 Profit 타입이 아닙니다.");
            }
        } catch (Exception e) {
            System.out.println("오류 발생: " + e.getClass().getName());
            System.out.println("오류 메시지: " + e.getMessage());
            e.printStackTrace();
        }
    }
}