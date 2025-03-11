package kr.kosa.bowl.file;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import kr.kosa.bowl.Profit;

/**
 * 정산 내역 파일 처리를 위한 구체 클래스
 */
public class ProfitFileHandler extends FileSaver {
    private static final String PROFIT_FILE_PATH = "profit.txt";
    
    public ProfitFileHandler() {
        super(PROFIT_FILE_PATH);
    }
    
    public ProfitFileHandler(String filePath) {
        super(filePath);
    }
    
    @Override
    protected void initializeFile() {
        // 초기 Profit 객체 생성
        Profit emptyProfit = Profit.getInstance();
        writeData(emptyProfit); 
    }
    

    @Override
    protected void writeData(Object data) {
        if (!(data instanceof Profit)) {
            System.err.println("[ERROR] 올바른 데이터 형식이 아닙니다. Profit 인스턴스가 필요합니다.");
            return;
        }
        
        try (FileOutputStream fos = new FileOutputStream(filePath);
             BufferedOutputStream bos = new BufferedOutputStream(fos);
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            
            oos.writeObject(data);
            System.out.println("[INFO] 정산 데이터가 성공적으로 저장되었습니다.");
            
        } catch (IOException e) {
            System.err.println("[ERROR] 정산 파일 저장 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
     
    @Override
	public Object readData() {
        try (FileInputStream fis = new FileInputStream(PROFIT_FILE_PATH);
             BufferedInputStream bis = new BufferedInputStream(fis);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            
            Object obj = ois.readObject();
            if (obj instanceof Profit) {
                System.out.println("[INFO] 정산 데이터를 성공적으로 불러왔습니다.");
                return obj;
            } else {
                System.err.println("[ERROR] 파일에서 읽은 객체가 Profit 인스턴스가 아닙니다.");
                return Profit.getInstance(); // 기본 인스턴스 반환
            }
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[ERROR] 정산 파일 읽기 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return Profit.getInstance(); // 오류 발생 시 새 인스턴스 반환
        }
    }
    
    @Override
    protected boolean validateData(Object data) {
        if (data instanceof Profit) {
            return true; // Profit 인스턴스는 허용
        } else if (data instanceof List) {
            // List<Receipt>도 허용하도록 부모 클래스의 메서드 사용
            return super.validateData(data);
        }
        return false;
    }

    /**
     * 정산 데이터 저장 편의 메서드
     */
    public void saveProfit() {
        Profit profit = Profit.getInstance();
        saveToFile(profit);
    }
    
    /**
     * 정산 데이터 로드 편의 메서드
     */
    public Profit loadProfit() { 
        Object data = loadFromFile();
        if (data instanceof Profit) {
            // 싱글톤 패턴 유지를 위해 인스턴스 복원
            Profit loadedProfit = (Profit) data;
            Profit.updateInstance(loadedProfit); // 이 메서드는 Profit 클래스에 추가해야 함
            return Profit.getInstance();
        }
        return Profit.getInstance(); // 기본값 반환
    }
}