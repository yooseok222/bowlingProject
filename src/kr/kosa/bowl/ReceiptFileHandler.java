package kr.kosa.bowl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;

/**
 * 영수증 파일 처리를 위한 구체 클래스
 */
public class ReceiptFileHandler extends FileSaver{

	 private static final String RECEIPT_FILE_PATH = "receipt.txt";
    private static final String RECEIPT_LOG_FILE_PATH = "receipt_log.txt";
    
    public ReceiptFileHandler() {
        super(RECEIPT_FILE_PATH);
    }
	
	public ReceiptFileHandler(String filePath) {
		super(filePath);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initializeFile() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void writeData(Object data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Object readData() {
		// TODO Auto-generated method stub
		return null;
	}
    
}