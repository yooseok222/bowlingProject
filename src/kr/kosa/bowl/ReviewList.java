package kr.kosa.bowl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

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

	// 고유번호에 해당하는 리뷰 받아오기
	public Review findReview(int num) {
		for (Review r : reviewList) {
			if (r.getReviewNum() == num) {
				return r;
			}
		}
		return null;
	}

	// 리뷰 리스트에 리뷰 추가
	public void addReview(Review review) {
		reviewList.add(review);
	}

	// 파일 저장
	public void saveToFile() {
		fileIO.saveFile(reviewList);
	}

	// 저장된 리뷰들의 고유 번호 중 최대값 받아오기
	public int getLastReviewNum() {
		if (reviewList.isEmpty()) {
			return 0;
		}

		sortReviewList("createdAt");
		return reviewList.get(0).getReviewNum();
	}

	// 리뷰 출력
	public void showReviewList() {
		sortReviewList("createdAt");
		printReviewList();
	}

	public void showSortByStarReviewList() {
		sortReviewList("star");
		printReviewList();
	}

	public void sortReviewList(String sortby) {
		Comparator<Review> sortCondition = null;

		if (sortby.equals("createdAt")) {
			sortCondition = Comparator.comparing(Review::getReviewNum);
		} else if (sortby.equals("star")) {
			sortCondition = Comparator.comparingDouble(Review::getStarCnt);
		}

		reviewList.sort(sortCondition.reversed());
	}

	public void printReviewList() {
		StringBuilder sb = new StringBuilder();
		Scanner sc = new Scanner(System.in);

		int idx = 1;
		for (Review review : reviewList) {
			if (idx % 6 == 0) {
				System.out.println("계속 보시겠습니까? 아니라면 n을 입력해주세요.");
				String in = sc.nextLine().toLowerCase();
				if (in.equals("n")) {
					break;
				}
			}

			if (review.isReview()) {
				sb.append("===============================================\n");
				sb.append("📝 리뷰 #").append(review.getReviewNum()).append(" | ⭐ 별점: ")
						.append(generateCircleRating(review.getStarCnt())).append("| ").append(review.getCreatedAt())
						.append("\n");
				sb.append("-----------------------------------------------\n");
				sb.append(review.getContent()).append("\n");

				for (Review reply : reviewList) {
					if (!reply.isReview() && reply.getLaneNum() == review.getReviewNum()) {
						sb.append("  ↳ 💬 관리자 답변: ").append(reply.getContent()).append("\n");
					}
				}

				sb.append("===============================================\n");

				idx++;
				System.out.println(sb);
				sb.setLength(0);
			}
		}
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