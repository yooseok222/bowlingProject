package kr.kosa.bowl;

import java.io.Serializable;
import java.util.Map;

import kr.kosa.bowl.storage.SnackFileIO;
import kr.kosa.bowl.util.AbstractFileIO;
import lombok.Data;

@Data
public class Snack implements Serializable {

	private static final long serialVersionUID = 548165165343L;
	private String snackName;
	private int snackPrice;
	private int snackCnt;

	public Snack(String snackName, int snackPrice, int snackCnt) {
		this.snackName = snackName;
		this.snackPrice = snackPrice;
		this.snackCnt = snackCnt;
	}

}
