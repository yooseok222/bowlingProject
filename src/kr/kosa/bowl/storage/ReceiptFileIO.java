package kr.kosa.bowl.storage;

import kr.kosa.bowl.Receipt;
import kr.kosa.bowl.util.AbstractFileIO;

public class ReceiptFileIO extends AbstractFileIO<Receipt> {
	private static final String FILE_PATH = "receipt.txt";

	public ReceiptFileIO() {
		super(FILE_PATH);
	}

	@Override
	protected Receipt initializeDefault() { // null로 초기화
		return null;
	}
}