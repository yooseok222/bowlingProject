package kr.kosa.bowl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import kr.kosa.bowl.factory.OrderFactory;

public class Game implements Serializable{
	static int TOTAL_ROUNDS = 2;
	List<Integer>[] board;
	int[][] roundScore;
	private List<Map<String, Integer>> orderMenuList;
	
	Game() {
		orderMenuList = new ArrayList<>();
	}

	public List<Map<String, Integer>> start(int headCnt, int shoesNum) {
		board = new ArrayList[headCnt];
		roundScore = new int[headCnt][TOTAL_ROUNDS];

		// board 넣어주기
		for (int i = 0; i < headCnt; i++) {
			board[i] = new ArrayList<Integer>();
		}

		// 스트라이크, 스페어 여부
		char[] beforeScore = new char[headCnt];

		// 게임 시작 >> 출력
		System.out.println("환영합니다! 볼링 게임을 시작합니다!");
		System.out.println("볼링게임 하는 법: 원하는 숫자를 입력하세요~");
		System.out.println("문자를 입력하면 간식을 구매합니다!");

		Scanner sc = new Scanner(System.in);

		for (int round = 0; round < TOTAL_ROUNDS; round++) {
			for (int player = 0; player < headCnt; player++) {
				System.out.println("\n🎳 Player " + (player + 1) + "의 차례입니다!");

				roundScore[player][round] = round > 0 ? roundScore[player][round - 1] : 0;
				int score = 0;
				int roll = 0;
				int pin = 10;
				boolean isStrike = false;
				// 점수 계산
				while (roll < 2) {
					displayScore();

					int nowScore = rollBall(sc, pin);
					score += nowScore;
					pin -= nowScore;

					if (isStrike)
						board[player].add(0);
					else
						board[player].add(nowScore);

					if (beforeScore[player] == '/' && roll == 0) { // 이전에 스페어
						roundScore[player][round] += nowScore;
					} else if (beforeScore[player] == 'x' && roll == 1) { // 이전에 스트라이크
						roundScore[player][round] += score;
					}

					if (score == 10) {
						if (roll == 0) {
							System.out.println("🎯 스트라이크!");
							beforeScore[player] = 'x';
							isStrike = true;
						} else {
							System.out.println("🎳 스페어!");
							beforeScore[player] = '/';
						}
					} else {
						beforeScore[player] = ' ';
					}

					roll++;
				}

				roundScore[player][round] += score;

				// 전설 신발
				applyShoeBonus(player, shoesNum, round);

				// 마지막 라운드인데 스트라이크/스페어 함
				if (round == TOTAL_ROUNDS - 1 && score >= 10) {
					displayScore();

					System.out.println("🎉 보너스 라운드! 한 번 더 굴리세요!");

					int bonusScore = rollBall(sc, 10);

					board[player].add(bonusScore);
					System.out.println("보너스 점수 +" + (bonusScore * 2) + "점 추가!");

				}

			}
		}

		System.out.println("\n🎳 게임이 끝났습니다! 다음에 또 봐요~");
		displayScore();
		
		return orderMenuList;
	}

	private int rollBall(Scanner sc, int maxPins) {
		int randomRoll = (int) (Math.random() * maxPins) + 1;

		int score = 0;

		while (true) {
			try {
				System.out.println("🎳 공을 굴리세요!");
				int userRoll = Integer.parseInt(sc.nextLine()) % (maxPins + 1);

				score = maxPins - Math.abs(randomRoll - userRoll);
				System.out.println("🎳 " + score + "개의 핀을 쓰러뜨렸습니다!");

				break;
			} catch (NumberFormatException e) {
				// 간식 주문
				Order order = OrderFactory.createOrder();
				orderMenuList.add(order.orderMenu());
			}
		}

		return score;
	}

	private void applyShoeBonus(int player, int shoesNum, int round) {
		// 전설 신발
		if (player < shoesNum) {
			System.out.println("👟 전설 신발 보너스 +1점 추가!");
			roundScore[player][round] += 1;
		}
	}

	private void displayScore() {
		System.out.println("\n📋 볼링 점수판 📋");

		// 점수판 헤더
		System.out.print("============================================\n");
		System.out.print("| Player |");
		for (int round = 0; round < TOTAL_ROUNDS; round++) {
			System.out.printf(" R%-3d |", round + 1);
		}
		System.out.print(" B  | Total |\n");
		System.out.print("--------------------------------------------\n");

		for (int player = 0; player < board.length; player++) {
			int rolls = board[player].size();

			// 보너스 점수 (없으면 0)
			int bonusScore = (rolls % 2 != 0) ? board[player].get(rolls - 1) * 2 : 0;

			// 결과 출력
			System.out.printf("|   P%-3d |", player + 1);
			for (int i = 0; i < TOTAL_ROUNDS * 2; i += 2) {
				if (i >= rolls) {
					System.out.print("      |");
					continue;
				}
				int firstRoll = board[player].get(i);
				int secondRoll = (i + 1 < rolls) ? board[player].get(i + 1) : 0;

				String rollStr;
				if (firstRoll == 10) // 스트라이크
					rollStr = " X  ";
				else if (firstRoll + secondRoll == 10) // 스페어
					rollStr = firstRoll + " /";
				else
					rollStr = firstRoll + " " + secondRoll;

				System.out.printf(" %-4s |", rollStr);
			}
			System.out.printf(" %-3d |\n", bonusScore); // 보너스 점수 (없으면 0)

			// 누적 점수 출력
			System.out.print("|        |");
			for (int round = 0; round < TOTAL_ROUNDS; round++) {
				System.out.printf(" %-4d |", roundScore[player][round]);
			}
			System.out.printf(" %-3d | %-5d |\n", bonusScore, bonusScore + roundScore[player][TOTAL_ROUNDS - 1]);
		}

		System.out.println("============================================");
	}
}
