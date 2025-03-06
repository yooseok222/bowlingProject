package kr.kosa.bowl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Game {
   int gameRoundNum = 2;
   List<Integer>[] board;

   public void Start(int headCnt, int shoseNum) {
      board = new ArrayList[headCnt];
      String[] beforeScore = new String[headCnt];
      int[] totalScore = new int[headCnt];
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
               int pinNum = (int) (Math.random() * pin - score) + 1;
               System.out.println(pin + "이하의 숫자만 입력하세요");
               // 입력 int
               int inputNum = Integer.parseInt(sc.nextLine());

               pin -= score;
               score += pin - Math.abs(pinNum - inputNum);

               // todo
               // 점수 계산 방식 검토
               System.out.println("라운드" + (r + 1) + " 점수" + score + " " + pin + " " + pinNum + " " + inputNum + " "
                     + Math.abs(pinNum - inputNum));

               // todo
               // 전설 신발
               if (score == 10) {
                  if (r == 0) {
                     System.out.println("스트라이크!");
                     beforeScore[player] = "st";
                     break;
                  } else {
                     System.out.println("스페어!");
                     beforeScore[player] = "sq";
                  }
               }
               r++;
            }

            board[player].add(score);

            if (beforeScore[player].equals("st")) { // 이전에 스트라이크
               totalScore[player] += score * 2;
            } else {
               totalScore[player] += score;
            }

            // 마지막 라운드인데 스트라이크 함
            if (round == gameRoundNum - 1 && score >= 10) {
               int pinNum = (int) (Math.random() * 10 - score) + 1;
               int inputNum = Integer.parseInt(sc.nextLine());

               totalScore[player] += (10 - Math.abs(pinNum - inputNum)) * 2;
            }
         }
      }

      // todo
      // 점수판 출력 > 파일 입출력
      System.out.println("점수판 ㅎㅎ");
      for (int p = 0; p < headCnt; p++) {
         for (int i = 0; i < gameRoundNum; i++) {
            System.out.print(board[p].get(i) + " ");
            if (i == gameRoundNum - 1) {
               System.out.println("총점수:" + totalScore[p]);
            }
         }
         System.out.println();
      }

   }

   public void displayScore(int score) {
      System.out.println(score);
   }
}
