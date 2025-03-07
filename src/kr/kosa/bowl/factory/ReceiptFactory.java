package kr.kosa.bowl.factory;

import kr.kosa.bowl.Lane;
import kr.kosa.bowl.Receipt;

public class ReceiptFactory {

	public static Receipt createReceipt(Lane lane) {
		return new Receipt(lane);
	}
}
