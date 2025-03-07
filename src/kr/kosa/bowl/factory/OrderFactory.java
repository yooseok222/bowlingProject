package kr.kosa.bowl.factory;

import kr.kosa.bowl.Order;

public class OrderFactory {

	public static Order createOrder() {
		return new Order();
	}
}
