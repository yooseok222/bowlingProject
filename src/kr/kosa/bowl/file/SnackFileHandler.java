package kr.kosa.bowl.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import kr.kosa.bowl.Snack;
/**
 * 스낵 파일 처리를 위한 구체 클래스
 */
public class SnackFileHandler extends FileSaver {
    
    public SnackFileHandler() {
        super("snack.txt");
    }
    
    public SnackFileHandler(String filePath) {
        super(filePath);
    }
    
    @Override
    protected void initializeFile() {
        Map<String, Snack> snackMap = new HashMap<>();
        snackMap.put("맥주", new Snack("맥주", 5000, 100));
        snackMap.put("땅콩", new Snack("땅콩", 3000, 100));
        writeData(snackMap);
    }
        
    @Override
    protected void writeData(Object data) {
        if (!(data instanceof Map)) {
            System.err.println("[ERROR] 올바른 데이터 형식이 아닙니다.");
            return;
        }
        
        try (FileOutputStream fos = new FileOutputStream(filePath);
             BufferedOutputStream bos = new BufferedOutputStream(fos);
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            
            oos.writeObject(data);
            
        } catch (IOException e) {
            System.err.println("[ERROR] 파일 저장 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    protected Object readData() {
        try (FileInputStream fis = new FileInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(fis);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            
            return ois.readObject();
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[ERROR] 파일 읽기 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        
        return new HashMap<>(); // 오류 발생 시 빈 맵 반환
    }
    
    @Override
    protected boolean validateData(Object data) { 
        return super.validateData(data) && data instanceof Map;
    }
    
    /**
     * 스낵 데이터 맵 저장 편의 메서드
     */
    public void saveSnackMap(Map<String, Snack> snackMap) {
        saveToFile(snackMap);
    }
    
    /**
     * 스낵 데이터 맵 읽기 편의 메서드
     */
    @SuppressWarnings("unchecked")
    public Map<String, Snack> readSnackMap() {
        return (Map<String, Snack>) loadFromFile();
    }
}

