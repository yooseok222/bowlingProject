package kr.kosa.bowl.storage;

import java.util.ArrayList;
import java.util.List;

import kr.kosa.bowl.Receipt;
import kr.kosa.bowl.util.AbstractFileIO;

public class ProfitFileIO extends AbstractFileIO<List<Receipt>> {
	private static final String FILE_PATH = "profit.txt";

	public ProfitFileIO() {
		super(FILE_PATH);
	}

	@Override
	protected List<Receipt> initializeDefault() {
		System.out.println("[INFO] 기본 수익 데이터 생성");
		return new ArrayList<>(); // 빈 리스트 반환
	}
}