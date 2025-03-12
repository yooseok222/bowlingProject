package kr.kosa.bowl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Data;

@Data
public class Review {

	private int reviewNum;
	private int laneNum;
	private String content;
	private double stars;
	private String createdAt;

	public Review(int laneNum) {
		this.laneNum = laneNum;
		this.reviewNum = laneNum;
		createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
	}
}
