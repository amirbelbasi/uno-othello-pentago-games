import java.util.Scanner;
import java.io.IOException;

/**
 * Main
 * 
 * @author Amirali Belbasi
 */
public class Main {
    /**
     * Code: new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
     * is a way to clean console & was copied from
     * 
     * @see https://stackoverflow.com/questions/2979383/java-clear-the-console
     * @param arg
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String[] arg) throws IOException, InterruptedException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            System.out.println("1) Single player");
            System.out.println("2) Two player");
            System.out.print("Choose game mode: ");
            String dummy = sc.nextLine();
            if (dummy.equals("1")) {
                Board test = new Board("Mr.Black", "Mr.White");
                int passCounter = 0;
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                while (true) {
                    if (test.isFull()) {
                        break;
                    }
                    if (passCounter == 2) {
                        break;
                    }
                    test.showBoard();
                    test.enterInp();
                    if (test.checkPass()) {
                        passCounter++;
                        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                        continue;
                    }
                    passCounter = 0;
                    String inp = sc.nextLine();
                    test.playTurn(inp);
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                }
                test.showBoard();
                test.determineWinner();
                break;
            } else if (dummy.equals("2")) {
                Board test = new Board("Mr.Black", "Mr.PC");
                int passCounter = 0;
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                while (true) {
                    if (test.isFull()) {
                        break;
                    }
                    if (passCounter == 2) {
                        break;
                    }
                    test.showBoard();
                    test.enterInp();
                    if (test.checkPass()) {
                        passCounter++;
                        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                        continue;
                    }
                    passCounter = 0;
                    if (test.getTurn() == 0) // its player's turn
                    {
                        String inp = sc.nextLine();
                        test.playTurn(inp);
                    } else // its PC's turn
                    {
                        System.out.println(Board.toUserFormat(test.generatePCMove()));
                        test.playTurn(Board.toUserFormat(test.generatePCMove()));
                        Thread.sleep(1500);
                    }
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                }
                test.showBoard();
                test.determineWinner();
                break;
            } else {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                System.out.println("invalid input");
                Thread.sleep(1500);
            }
        }
        sc.close();
    }
}