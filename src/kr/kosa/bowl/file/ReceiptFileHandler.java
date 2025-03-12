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
    }
    
    
    public void saveReceiptToFile(Receipt receipt) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(RECEIPT_FILE_PATH))) {
            oos.writeObject(receipt);
            System.out.println("[INFO] 영수증이 성공적으로 저장되었습니다.");
        } catch (IOException e) {
            System.err.println("[ERROR] 영수증 저장 중 오류 발생: " + e.getMessage());
        }
    }

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
    

    public Receipt loadReceipt() {
        Object data = loadFromFile();
        if (data instanceof Receipt) {
            return (Receipt) data;
        }
        return null; // 또는 적절한 예외 처리
    }

}

