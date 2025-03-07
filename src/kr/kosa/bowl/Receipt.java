package kr.kosa.bowl;

import java.util.Map;

public class Receipt {

	private int totalFee;
	private Lane lane;
	private Map<String, Snack> snackMap;

	public Receipt(Lane lane) {
		this.lane = lane;
		this.snackMap = SnackFile.readSnackFile();
	}
}
