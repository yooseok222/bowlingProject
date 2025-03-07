package kr.kosa.bowl.exception;

// 주문시 발생할 예외 처리 클래스
public class OrderException extends Exception {
	public OrderException(String message) {
		super(message);
	}
}