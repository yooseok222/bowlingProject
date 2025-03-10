
package kr.kosa.bowl.file;

import java.io.*;

public class SerializedViewer {
    public static void main(String[] args) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("profit.txt"))) {
            Object obj = ois.readObject();
            System.out.println("객체 클래스: " + obj.getClass().getName());
            System.out.println("객체 내용: " + obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}