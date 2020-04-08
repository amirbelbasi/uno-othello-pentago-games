import java.util.Scanner;
import java.io.IOException;
// import org.fusesource.jansi.AnsiConsole;
// import static org.fusesource.jansi.Ansi.*;
// import static org.fusesource.jansi.Ansi.Color.*;

/**
 * Main
 */
public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        // AnsiConsole.systemInstall();
        // AnsiConsole.systemUninstall();
        // System.out.println( ansi().eraseScreen().render("@|red Hello|@ @|green World|@") );
        Scanner sc = new Scanner(System.in);
        String player1, player2;
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        System.out.print("enter player1 name: ");
        player1 = sc.nextLine();
        System.out.print("enter player2 name: ");
        player2 = sc.nextLine();
        Board test = new Board(player1, player2);
        /**
         * READ ME PLEASE
         * input guide:
         * coordinates of cell should be like "<index of row> <index of column>" e.g. "5 0" would be left down corner
         * & "0 5 would be right up corner"
         * details of block should be like "<index of block> <direction>" e.g. "1 +", "2 -" or "4 +"
         * + ==> clock wise
         * - ==> counter clock wise
         */
        while (true) {
            if (test.playTurn() == -1) {
                break;
            }
        }
    }
}