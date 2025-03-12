package kr.kosa.bowl.file;
import kr.kosa.bowl.Receipt;
import java.io.*;
import java.util.List;
import java.util.Map;


public class SerializedViewer {
    public static void main(String[] args) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("receipt.txt"))) {
            Object obj = ois.readObject();
            System.out.println("객체 클래스: " + obj.getClass().getName());
            
            // Receipt 객체인지 확인하고 캐스팅
            if (obj instanceof Receipt) {
                Receipt receipt = (Receipt) obj;
                
                // Receipt 객체의 내용 출력
                System.out.println("\n===== 영수증 내용 =====");
                receipt.showReceipt(); // Receipt 클래스의 showReceipt 메서드 호출
               
            } else {
                System.out.println("파일에 저장된 객체가 Receipt 타입이 아닙니다.");
            }
            
        } catch (Exception e) {
            System.out.println("오류 발생: " + e.getClass().getName());
            System.out.println("오류 메시지: " + e.getMessage());
            e.printStackTrace();
        }
    }
}