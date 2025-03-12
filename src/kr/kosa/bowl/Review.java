package kr.kosa.bowl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Data;

@Data
public class Review {

	private int reviewNum;
	private int laneNum;
	private String content;
	private double starCnt;
	private String createdAt;
	private boolean isReview; // 리뷰인지 댓글인지 구분 true : 리뷰, false : 댓글

	// 사용자용 리뷰를 남길 때 사용하는 생성자 -> content와 starCnt를 입력받는다
	public Review(int laneNum, String content, double starCnt) {
		this.laneNum = laneNum;
		this.content = content;
		this.starCnt = starCnt;
		this.isReview = true;
		createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
	}

	// 관리자가 리뷰에 댓글을 달 때 사용하는 생성자 -> reviewNum과 content를 입력받는다
	public Review(int reviewNum, String content) {
		this.laneNum = reviewNum;
		this.content = content;
		this.isReview = false;
		createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
	}
}