package kr.kosa.bowl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import kr.kosa.bowl.factory.OrderFactory;

public class Lane {
	private int laneNum; // 레인넘버
	private int headCnt; // 인원수
	private int shoesCnt; // 신발갯수
	private boolean isClean; // 청소여부
	private String selectedAt; // 레인선택시간
	private int gameCnt; // 게임카운트
	private Game game;		//게임객체
	private List<Map<String, Integer>> orderMenuList;

	public Lane() {

		Scanner sc = new Scanner(System.in);
		System.out.print("인원수를 입력하세요 : ");
		this.headCnt = Integer.parseInt(sc.nextLine());

		System.out.print("신발갯수를 입력하세요 : ");
		this.shoesCnt = Integer.parseInt(sc.nextLine());


		this.selectedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
		
		orderMenuList = new ArrayList<>();
		
	}

	@Override
	public String toString() {
		return "headCnt : " + headCnt + "shoesCnt : " + shoesCnt + "selectedAt : " + selectedAt;
	}

	// 인원수 선택 및 신발대여 여부
	public void useLane() {
		Order order = OrderFactory.createOrder();
		Map<String, Integer>orderedMenu = order.orderMenu();
		orderMenuList.add(orderedMenu);
		
	}
}
