package kr.kosa.bowl.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import kr.kosa.bowl.Receipt;

/**
 * 영수증 파일 처리를 위한 구체 클래스
 */
/**
 * 영수증 파일 처리를 위한 구체 클래스
 */
public class ReceiptFileHandler extends FileSaver{
    private static final String RECEIPT_FILE_PATH = "receipt.txt";
    private static final String RECEIPT_LOG_FILE_PATH = "receipt_log.txt";
    
    public ReceiptFileHandler() {
        super(RECEIPT_FILE_PATH);
    }

    /**
     * 특정 파일 경로를 사용하는 생성자
     */
    public ReceiptFileHandler(String filePath) {
        super(filePath);
    }
    
    @Override
    protected void initializeFile() {
        // 빈 영수증 리스트로 초기화
        List<Receipt> emptyList = new ArrayList<>();
        writeData(emptyList);
    }
    
    @Override
	public void writeData(Object data) {
        try (FileOutputStream fos = new FileOutputStream(RECEIPT_FILE_PATH);
             BufferedOutputStream bos = new BufferedOutputStream(fos);
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            
            oos.writeObject(data);
            
        } catch (IOException e) {
            System.err.println("[ERROR] 영수증 파일 저장 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    protected Object readData() {
        try (FileInputStream fis = new FileInputStream(RECEIPT_FILE_PATH);
             BufferedInputStream bis = new BufferedInputStream(fis);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            
            return ois.readObject(); 
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[ERROR] 영수증 파일 읽기 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<Receipt>(); // 오류 발생 시 빈 리스트 반환
        }
    }
    
    @Override
    protected boolean validateData(Object data) {
        return super.validateData(data);
//        		&& 
//              (data instanceof Receipt || 
//               data instanceof List && ((List<?>) data).isEmpty() || 
//               ((List<?>) data).get(0) instanceof Receipt);
    }
    
//    /**
//     * 영수증 데이터 저장 편의 메서드
//     */
//    public void saveReceipt(Receipt receipt) {
//        // Receipt 객체를 ReceiptData로 변환
//        ReceiptData receiptData = convertToReceiptData(receipt);
//        
//        // 기존 영수증 리스트 읽기
//        List<ReceiptData> receiptList = readReceiptList();
//        
//        // 신규 영수증 추가
//        receiptList.add(receiptData);
//        
//        // 파일에 저장
//        saveToFile(receiptList);
//        
//        // 영수증 로그 파일에도 저장 (누적 영수증 기록)
//        appendToReceiptLog(receiptData);
//        
//        System.out.println("[INFO] 영수증이 성공적으로 저장되었습니다.");
//    }
    
//    /**
//     * 영수증 로그 파일에 영수증 추가 저장 (텍스트 형식)
//     */
//    private void appendToReceiptLog(ReceiptData receiptData) {
//        try (FileWriter fw = new FileWriter(RECEIPT_LOG_FILE_PATH, true);
//             BufferedWriter bw = new BufferedWriter(fw);
//             PrintWriter pw = new PrintWriter(bw)) {
//            
//            LocalDateTime now = LocalDateTime.now();
//            String timestamp = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//            
//            pw.println("=== 영수증 " + receiptData.getReceiptId() + " ===");
//            pw.println("저장 시간: " + timestamp);
//            pw.println("레인 번호: " + receiptData.getLaneNumber());
//            pw.println("총 인원: " + receiptData.getHeadCount() + "명");
//            pw.println("대여 신발: " + receiptData.getShoesCount() + "켤레");
//            pw.println("게임 수: " + receiptData.getGameCount() + "게임");
//            
//            pw.println("--- 주문 내역 ---");
//            for (Map.Entry<String, Integer> entry : receiptData.getOrderItems().entrySet()) {
//                pw.println(entry.getKey() + ": " + entry.getValue() + "개");
//            }
//            
//            pw.println("총 금액: " + receiptData.getTotalFee() + "원");
//            pw.println("==================");
//            pw.println();  // 영수증 간 빈 줄 추가
//            
//        } catch (IOException e) {
//            System.err.println("[ERROR] 영수증 로그 파일 저장 중 오류 발생: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
    
//    /**
//     * Receipt 객체를 직렬화 가능한 ReceiptData 객체로 변환
//     */
//    private ReceiptData convertToReceiptData(Receipt receipt) {
//        ReceiptData data = new ReceiptData();
//        
//        // Receipt에서 데이터 추출
//        data.setLaneNumber(receipt.getLane().getLaneNum());
//        data.setHeadCount(receipt.getLane().getHeadCnt());
//        data.setShoesCount(receipt.getLane().getShoesCnt());
//        data.setGameCount(receipt.getLane().getGameCnt());
//        data.setTotalFee(receipt.getTotalFee());
//        
//        // 영수증 ID 생성 (현재 시간 기반)
//        String receiptId = "R" + System.currentTimeMillis();
//        data.setReceiptId(receiptId);
//        
//        // 주문 항목 통합
//        Map<String, Integer> mergedOrders = new HashMap<>();
//        List<Map<String, Integer>> orderMenuList = receipt.getLane().getOrderMenuList();
//        
//        for (Map<String, Integer> map : orderMenuList) {
//            for (Map.Entry<String, Integer> entry : map.entrySet()) {
//                mergedOrders.put(entry.getKey(), 
//                    mergedOrders.getOrDefault(entry.getKey(), 0) + entry.getValue());
//            }
//        }
//        
//        data.setOrderItems(mergedOrders);
//        return data;
//    }
//    
    /**
     * 모든 영수증 읽기
     */
    @SuppressWarnings("unchecked")
    public List<Receipt> readReceiptList() {
        Object data = loadFromFile();
        if (data instanceof List) {
            return (List<Receipt>) data;
        }
        return new ArrayList<>();
    }
    
//    /**
//     * 특정 영수증 ID로 영수증 조회
//     */
//    public ReceiptData findReceiptById(String receiptId) {
//        List<Receipt> receiptList = readReceiptList();
//        for (Receipt receipt : receiptList) {
//            if (receipt.getReceiptId().equals(receiptId)) {
//                return receipt;
//            }
//        }
//        return null;
//    }
    
    public Receipt loadReceipt() {
        Object data = loadFromFile();
        if (data instanceof Receipt) {
            return (Receipt) data;
        }
        return null; // 또는 적절한 예외 처리
    }

}

