package kr.kosa.bowl.storage;

import kr.kosa.bowl.util.AbstractFileIO;
import kr.kosa.bowl.Snack;

import java.util.HashMap;
import java.util.Map;

public class SnackFileIO extends AbstractFileIO<Map<String, Snack>> {
	private static final String FILE_PATH = "snack.txt";

	public SnackFileIO() {
		super(FILE_PATH);
	}

	@Override
	protected Map<String, Snack> initializeDefault() {
		System.out.println("[INFO] 기본 스낵 데이터 생성");
		Map<String, Snack> defaultSnacks = new HashMap<>();
		defaultSnacks.put("맥주", new Snack("맥주", 5000, 100));
		defaultSnacks.put("땅콩", new Snack("땅콩", 3000, 100));
		return defaultSnacks;
	}
}