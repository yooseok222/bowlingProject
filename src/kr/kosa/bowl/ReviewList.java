package kr.kosa.bowl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ReviewList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 789451654615L;
	private List<Review> reviewList;

	public ReviewList() {
		reviewList = new ArrayList<>();
	}
}
