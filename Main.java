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
    public static void main(String... arg) throws IOException, InterruptedException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        Scanner sc = new Scanner(System.in);
        Board test = new Board("Mr.Black", "Mr.White");
        int passCounter = 0;
        while (true) {
            if (passCounter == 2)
                break;
            test.showBoard();
            test.enterInp();
            String inp = sc.nextLine();
            passCounter += test.playTurn(inp);
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        }
        sc.close();
    }
}