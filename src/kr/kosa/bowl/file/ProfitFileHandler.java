package kr.kosa.bowl.file;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import kr.kosa.bowl.Profit;
import kr.kosa.bowl.Receipt;

/**
 * 정산 내역 파일 처리를 위한 구체 클래스
 */
public class ProfitFileHandler extends FileSaver {
    private static final String PROFIT_FILE_PATH = "profit.txt"; // 영수증을 저장할 파일

    public ProfitFileHandler() {
        super(PROFIT_FILE_PATH);
    }

    @Override
    protected void initializeFile() {
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            Profit emptyProfit = Profit.getInstance();
            writeData(emptyProfit);
        }
    }

    @Override
    protected void writeData(Object data) {
        if (!(data instanceof Profit)) {
            System.err.println("[ERROR] 올바른 데이터 형식이 아닙니다. Profit 인스턴스가 필요합니다.");
            return;
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(data);
            System.out.println("[INFO] 정산 데이터가 성공적으로 저장되었습니다.");
        } catch (IOException e) {
            System.err.println("[ERROR] 정산 파일 저장 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Object readData() {
        File file = new File(PROFIT_FILE_PATH);
        if (!file.exists() || file.length() == 0) {
            return new Profit(); // 파일이 없으면 새 Profit 인스턴스 반환
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PROFIT_FILE_PATH))) {
            Object obj = ois.readObject();
            if (obj instanceof Profit) {
                return obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[ERROR] 정산 파일 읽기 중 오류 발생: " + e.getMessage());
        }
        return new Profit(); // 오류 발생 시 새 Profit 인스턴스 반환
    }

    /** 영수증 리스트 저장 */
    public void saveReceiptsToFile(List<Receipt> receipts) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PROFIT_FILE_PATH))) {
            oos.writeObject(receipts);
            System.out.println("[INFO] 영수증 리스트가 성공적으로 저장되었습니다.");
        } catch (IOException e) {
            System.err.println("[ERROR] 영수증 리스트 저장 중 오류 발생: " + e.getMessage());
        }
    }

    /** 영수증 리스트 불러오기 */
    public List<Receipt> loadReceiptsFromFile() {
        File file = new File(PROFIT_FILE_PATH);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>(); // 파일이 없으면 빈 리스트 반환
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PROFIT_FILE_PATH))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                return (List<Receipt>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[ERROR] 영수증 리스트 불러오기 중 오류 발생: " + e.getMessage());
        }
        return new ArrayList<>(); // 오류 발생 시 빈 리스트 반환
    }
}
