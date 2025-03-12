package kr.kosa.bowl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kr.kosa.bowl.storage.ReviewListFileIO;
import kr.kosa.bowl.util.AbstractFileIO;
import lombok.Data;

@Data
public class ReviewList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 789451654615L;
	private static ReviewList instance;
	private List<Review> reviewList;
	private static AbstractFileIO<List<Review>> fileIO;

	private ReviewList() {
		fileIO = new ReviewListFileIO();
		List<Review> savedReviews = fileIO.loadFile();
		this.reviewList = (savedReviews != null) ? savedReviews : new ArrayList<>();
	}

	public static ReviewList getInstance() {
		if (instance == null) {
			instance = new ReviewList();
		}
		return instance;
	}

	// 리뷰 출력 (createAt)
	public void showReviewList() {
		StringBuilder sb = new StringBuilder();
		for (Review review : reviewList) {

			sb.append("\n").append(review.getContent()).append("\n");
			if (review.isReview()) {
				sb.append("별점 : ").append(generateCircleRating(review.getStarCnt())).append("\n\n");
			}
		}
		System.out.println(sb);
	}
	
	

	// 별 찍는 함수
	public static String generateCircleRating(double rating) {
		StringBuilder result = new StringBuilder();
		// 별점 계산 (0.5 단위)
		int totalHalfStars = (int) (rating * 2); // 0.5 = 1, 1.0 = 2, 1.5 = 3 등
		// 문자 정의
		final String FULL_CIRCLE = "●"; // 꽉 찬 원
		final String HALF_CIRCLE = "◐"; // 반 원 (왼쪽 반쪽)
		final String EMPTY_CIRCLE = "○"; // 빈 원
		// 별점 표시 생성
		for (int i = 0; i < 5; i++) {
			// 현재 위치가 꽉 찬 원인 경우
			if (i * 2 + 1 < totalHalfStars) {
				result.append(FULL_CIRCLE).append(" ");
			}
			// 현재 위치가 반 원인 경우
			else if (i * 2 + 1 == totalHalfStars) {
				result.append(HALF_CIRCLE).append(" ");
			}
			// 현재 위치가 빈 원인 경우
			else {
				result.append(EMPTY_CIRCLE).append(" ");
			}
		}
		return result.toString();
	}
}