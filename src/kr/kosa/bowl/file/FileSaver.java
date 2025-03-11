package kr.kosa.bowl.file;


import java.io.File;
import java.util.List;

import kr.kosa.bowl.Receipt;

/*
 * 파일을 저장하는 기본 추상 클래스
 */
public abstract class FileSaver {
	
	protected String filePath;
	
	
	public FileSaver(String filePath) {
		this.filePath = filePath;
	}


	/** 파일 저장하는 과정을 담은 템플릿 메서드 */
	public void processFile() {
		
		//1. 파일 존재 여부 확인 및 초기화
		if(!isFileExists()) {
			System.out.println("[INFO] " + getFileName() + "가 없어 초기 데이터 생성.");
			initializeFile();
		}
	}
	 
	public void saveToFile(Object data) {
		 System.out.println("▶ 파일 쓰기 시작: 데이터 타입 = " + (data != null ? data.getClass().getName() : "null"));
		    try {
		//1. 저장 전 데이터 검증
		if(!validateData(data)) {
			System.out.println("[ERROR] 유효하지 않은 데이터입니다.");
            return;
		}
		
		 // 2. 데이터 전처리
        Object processedData = preprocessData(data);
        
        // 3. 파일에 쓰기
        writeData(processedData);
        
        // 4. 저장 후 작업
        afterSave();
        System.out.println("✅ 파일 쓰기 성공");
		    } catch (Exception e) {
		        System.out.println("[ERROR] 파일 쓰기 실패: " + e.getMessage());
		        e.printStackTrace();
		    }
		
	}
	
	//파일에서 데이터 읽기
	public Object loadFromFile() {
		//1. 파일 존재 여부 확인 
		if(!isFileExists()) {
			System.out.println("[INFO] " + getFileName() + "가 없어 초기 데이터 생성 후 읽음.");
            initializeFile();
		}
		
		// 2. 파일에서 읽기
        Object data = readData();
        
        // 3. 데이터 후처리
        return postprocessData(data);
	}
	
	 // 파일 존재 여부 확인 - 공통 구현
    protected boolean isFileExists() {
        File file = new File(filePath);
        return file.exists();
    }
    
    // 파일명 가져오기 - 공통 구현
    protected String getFileName() {
        return new File(filePath).getName();
    }
    
    // 저장 전 데이터 검증 - 기본 구현, 필요시 하위 클래스에서 재정의
    protected boolean validateData(Object data) {
        System.out.println("▶ 검증 시작: 데이터 타입 = " + (data != null ? data.getClass().getName() : "null"));
        
        if (data == null) {
            System.out.println("[ERROR] 데이터가 null입니다.");
            return false;
        }
        
        // Receipt 객체 또는 Receipt 리스트 모두 허용
        if (data instanceof Receipt) {
            Receipt receipt = (Receipt) data;
            System.out.println("▶ Receipt 객체 확인: Lane = " + (receipt.getLane() != null ? "존재" : "null"));
            // Receipt 객체의 중요 필드들 확인
            if (receipt.getLane() == null) {
                System.out.println("[ERROR] Receipt의 Lane이 null입니다.");
                return false;
            }
            return true;
        } else if (data instanceof List) {
            List<?> listData = (List<?>) data;
            System.out.println("▶ List 확인: 크기 = " + listData.size());
            
            if (listData.isEmpty()) {
                System.out.println("[ERROR] 리스트가 비어 있습니다.");
                return false;
            }
            
            // 리스트의 첫 번째 항목 타입 확인
            Object firstItem = listData.get(0);
            System.out.println("▶ 첫 번째 항목 타입: " + (firstItem != null ? firstItem.getClass().getName() : "null"));
            
            // 리스트의 모든 항목이 Receipt인지 확인
            for (int i = 0; i < listData.size(); i++) {
                Object item = listData.get(i);
                if (!(item instanceof Receipt)) {
                    System.out.println("[ERROR] 리스트의 " + i + "번째 항목이 Receipt 타입이 아닙니다: " + 
                                      (item != null ? item.getClass().getName() : "null"));
                    return false;
                }
            }
            return true;
        } else {
            System.out.println("[ERROR] 데이터가 Receipt 또는 List 타입이 아닙니다. 현재 타입: " + data.getClass().getName());
            return false;
        }
    }
    
    // 데이터 전처리 - 기본 구현, 필요시 하위 클래스에서 재정의
    protected Object preprocessData(Object data) {
        return data;
    }
    
    // 데이터 후처리 - 기본 구현, 필요시 하위 클래스에서 재정의
    protected Object postprocessData(Object data) {
        return data;
    }
    
    // 저장 후 작업 - 기본 구현, 필요시 하위 클래스에서 재정의
    protected void afterSave() {
        System.out.println("[INFO] 파일 저장 완료.");
    }
    
    // 추상 메서드 - 하위 클래스에서 반드시 구현해야 함
    protected abstract void initializeFile();
    protected abstract void writeData(Object data);
    protected abstract Object readData();
	
}