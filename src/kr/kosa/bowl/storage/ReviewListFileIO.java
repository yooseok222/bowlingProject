package kr.kosa.bowl.storage;

import java.util.ArrayList;
import java.util.List;

import kr.kosa.bowl.Review;
import kr.kosa.bowl.util.AbstractFileIO;

public class ReviewListFileIO extends AbstractFileIO<List<Review>> {
	private static final String FILE_PATH = "reviewList.txt";

	public ReviewListFileIO() {
		super(FILE_PATH);
	}

	@Override
	protected List<Review> initializeDefault() {
		System.out.println("[INFO] 기본 리뷰 데이터 생성");
		return new ArrayList<>(); // 빈 리스트 반환
	}
}