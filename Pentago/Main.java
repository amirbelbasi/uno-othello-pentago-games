import java.util.Scanner;
import java.io.IOException;

/**
 * READ ME:
 * to keep the efficiency of the code i evoided using inheritence contecpt
 * using it in this case will only make code more complex
 */

/**
 * Main
 */
public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        Scanner sc = new Scanner(System.in);
        String gameModeInp;
        while (true) {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            System.out.println("1) Single player");
            System.out.println("2) Two player");
            System.out.print("choose game mode: ");
            gameModeInp = sc.nextLine();
            if (gameModeInp.length() != 1) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                System.out.println("invalid input");
                Thread.sleep(1500);
                continue;
            }
            if (!gameModeInp.equals("1") && !gameModeInp.equals("2")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                System.out.println("invalid input");
                Thread.sleep(1500);
                continue;
            }
            break;
        }
        if (gameModeInp.equals("2")) // two player
        {
            String player1, player2;
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            System.out.print("enter player1 name: ");
            player1 = sc.nextLine();
            System.out.print("enter player2 name: ");
            player2 = sc.nextLine();
            Board myBoard = new Board(player1, player2);
            /**
             * READ ME PLEASE input guide: coordinates of cell should be like "<index of
             * row> <index of column>" e.g. "5 0" would be left down corner & "0 5 would be
             * right up corner" details of block should be like "<index of block>
             * <direction>" e.g. "1 +", "2 -" or "4 +" + ==> clock wise - ==> counter clock
             * wise
             */
            while (true) {
                if (myBoard.playTurn() == -1) {
                    break;
                }
            }
        } else // single player
        {
            String player;
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            System.out.print("enter player name: ");
            player = sc.nextLine();
            Board myBoard = new Board(player);
            /**
             * READ ME PLEASE input guide: coordinates of cell should be like "<index of
             * row> <index of column>" e.g. "5 0" would be left down corner & "0 5 would be
             * right up corner" details of block should be like "<index of block>
             * <direction>" e.g. "1 +", "2 -" or "4 +" + ==> clock wise - ==> counter clock
             * wise
             */
            while (true) {
                if (myBoard.getTurn() == Turn.BLACK.ordinal()) {
                    if (myBoard.PCPlayTurn() == -1) {
                        break;
                    }
                } else {
                    if (myBoard.playTurn() == -1) {
                        break;
                    }
                }
            }
        }
        sc.close();
    }
}