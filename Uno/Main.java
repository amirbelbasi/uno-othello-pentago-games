import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;

/**
 * Main USER GUIDE: type of player would be "PC" or "user"
 */
public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        String nPlayers;
        Scanner myScanner = new Scanner(System.in);
        while (true) {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            System.out.print("enter number of players: ");
            nPlayers = myScanner.nextLine();
            if (!nPlayers.equals("3") && !nPlayers.equals("4") && !nPlayers.equals("5")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                System.out.println("invalid input");
                Thread.sleep(1500);
                continue;
            }
            break;
        }
        Game myGame = new Game(Integer.parseInt(nPlayers));
        boolean hasPickedName = false;
        String name = "", type = "";
        int i = 1;
        while (true) {
            if(i > Integer.parseInt(nPlayers))
            {
                break;
            }
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            if (!hasPickedName) {
                System.out.print("enter name of player #" + i + ": ");
                name = myScanner.nextLine();
                hasPickedName = true;
            }
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            System.out.print("enter type of player #" + i + ": ");
            type = myScanner.nextLine();
            if (!type.equals("PC") && !type.equals("user")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                System.out.println("invalid input");
                Thread.sleep(1500);
                continue;
            }
            myGame.players.add(new Player(name, type));
            hasPickedName = false;
            i++;
        }
        // so now players has been added to game
        myGame.startARound();
        myScanner.close();
    }
}