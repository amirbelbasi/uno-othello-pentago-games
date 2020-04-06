import java.util.Scanner;

/**
 * Main
 */
public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        Scanner sc = new Scanner(System.in);
        Board test = new Board();
        /**
         * input guide:
         * coordinates of cell should be like "<index of row> <index of column>" like "2 0"
         * details of block should be like ""
         */
        while (true)
        {
            test.showBoard();
            test.playTurn();
        }
    }
}