package kr.kosa.bowl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Game {
	static int gameRoundNum = 2;
	List<Integer>[] board;
	int[][] roundScore;

	public void Start(int headCnt, int shoseNum) {
		board = new ArrayList[headCnt];
		roundScore = new int[headCnt][gameRoundNum];
		
		// 총 점수
		int[] totalScore = new int[headCnt];
		// 스트라이크, 스페어 여부
		String[] beforeScore = new String[headCnt];
		
		Arrays.fill(totalScore, 0);
		Arrays.fill(beforeScore, "");

		for (int i = 0; i < headCnt; i++) {
			board[i] = new ArrayList<Integer>();
		}

		// 게임 시작 >> 출력
		System.out.println("환영합니다! 볼링 게임을 시작합니다!");
		System.out.println("볼링게임 하는 법: 0~10만 입력하세요~");

		Scanner sc = new Scanner(System.in);

		for (int round = 0; round < gameRoundNum; round++) {
			for (int player = 0; player < headCnt; player++) {
				System.out.println("player " + player);

				int score = 0;
				int r = 0;
				int pin = 10;
				// 점수 계산
				while (r < 2) {
					System.out.println("굴리세요");
					if (beforeScore[player].equals("sq")) { // 이전에 스페어
						totalScore[player] += score;
					}

					// 랜덤 int
					int pinNum = (int) (Math.random() * pin) + 1;
					System.out.println(pin + "이하의 숫자만 입력하세요");
					// 입력 int
					int inputNum = Integer.parseInt(sc.nextLine());

					int nowScore = pin - Math.abs(pinNum - inputNum);
					score += nowScore;
					pin -= nowScore;

					System.out.println(nowScore);

					if (score == 10) {
						if (r == 0) {
							System.out.println("스트라이크!");
							beforeScore[player] = "st";
							break;
						} else {
							System.out.println("스페어!");
							beforeScore[player] = "sq";
						}
					} else {
						if (r == 1)
							System.out.println("총" + score + "개의 볼링 핀을 쓰러뜨렸습니다!");
					}
					
					board[player].add(nowScore);
					r++;
				}

				

				if (beforeScore[player].equals("st")) { // 이전에 스트라이크
					totalScore[player] += score * 2;
				} else {
					totalScore[player] += score;
				}
				
				// 전설 신발
				if (player < shoseNum) {
					System.out.println("전설신발");
					totalScore[player] += 1;
				}

				// 마지막 라운드인데 스트라이크/스페어 함
				if (round == gameRoundNum - 1 && score >= 10) {
					System.out.println("보너스 라운드~");
					int pinNum = (int) (Math.random() * 10) + 1;
					int inputNum = Integer.parseInt(sc.nextLine());

					int bonusScore = 10 - Math.abs(pinNum - inputNum);
					System.out.println(bonusScore + " " + pinNum + " " + inputNum);
					totalScore[player] += bonusScore * 2;
					board[player].add(bonusScore);
					System.out.println("총" + bonusScore + "개의 볼링 핀을 쓰러뜨렸습니다!\n보너스 점수 " + bonusScore * 2 + "추가!");
					// todo
					// 스트라이크, 스페어 처리 (그냥 출력만)
				}
				
				roundScore[player][round] = totalScore[player];
				// 점수판 파일에 쓰기

			}
		}

		// todo
		// 점수판 출력 > 파일 입출력
		System.out.println("점수판 ㅎㅎ");
		for (int p = 0; p < headCnt; p++) {
			System.out.print("player"+p);
			for (int i = 0; i < board[p].size(); i++) {
				System.out.print(" "+board[p].get(i));
			}
			System.out.println();
			for (int i = 0; i < roundScore[p].length; i++) {
				System.out.print(roundScore[p][i] + " ");
			}
			System.out.println();
		}

	}

	void displayScore(int score) { // 점수판 출력
		
	}
	
	void writeScore() { // 점수 저장(파일)
		File file = new File("book.txt");

		try {
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(board);

			oos.close();
			fos.close();
		} catch (Exception e) {
			System.out.println("에러발생!!!");
			e.printStackTrace();
		}
		System.out.println("저장되었습니다.");
	}
}
