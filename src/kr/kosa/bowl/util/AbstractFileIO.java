package kr.kosa.bowl.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public abstract class AbstractFileIO<T> {
	private final String filePath;

	protected AbstractFileIO(String filePath) {
		this.filePath = filePath;
	}

	// **파일 저장 로직 (변경 불가능)**
	public final void saveFile(T object) {
		try (FileOutputStream fos = new FileOutputStream(filePath);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				ObjectOutputStream oos = new ObjectOutputStream(bos)) {

			oos.writeObject(object);
			System.out.println("[INFO] " + filePath + " 저장 완료.");

		} catch (IOException e) {
			System.err.println("[ERROR] 파일 저장 중 오류 발생: " + e.getMessage());
			e.printStackTrace();
		}
	}

	// **파일 로드 로직 (변경 불가능)**
	public final T loadFile() {
		File file = new File(filePath);
		if (!file.exists()) {
			System.out.println("[INFO] 파일 없음: 기본 데이터 생성");
			return initializeDefault();
		}

		try (FileInputStream fis = new FileInputStream(file);
				BufferedInputStream bis = new BufferedInputStream(fis);
				ObjectInputStream ois = new ObjectInputStream(bis)) {

			return (T) ois.readObject();

		} catch (IOException | ClassNotFoundException e) {
			System.err.println("[ERROR] 파일 읽기 중 오류 발생: " + e.getMessage());
			e.printStackTrace();
		}
		return initializeDefault();
	}

	// **하위 클래스에서 기본 데이터를 제공하도록 강제**
	protected abstract T initializeDefault();
}