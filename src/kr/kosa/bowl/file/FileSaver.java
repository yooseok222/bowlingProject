package kr.kosa.bowl.file;


import java.io.File;

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
        return data != null;
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