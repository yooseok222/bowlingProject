package kr.kosa.bowl;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Snack implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 548165165343L;
	private String snackName;
	private int snackPrice;
	private int snackCnt;
}
