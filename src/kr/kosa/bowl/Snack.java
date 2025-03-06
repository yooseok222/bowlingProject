package kr.kosa.bowl;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Snack {
	private String snackName;
	private int snackPrice;
	private int snackCnt;
}
