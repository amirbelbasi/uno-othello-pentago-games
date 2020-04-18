import java.util.ArrayList;
import java.util.Scanner;

/**
 * Player
 */
public class Player {
    private static int codeOfPlayer = 1;
    public String name;
    public String type;
    public String keyStr; // a string that is used as player
    public int points = 0;
    public ArrayList<Card> playerCards = new ArrayList<>();
    public ArrayList<Card> possibleCards = new ArrayList<>();
    public Player(String name, String type)
    {
        this.name = name;
        this.type = type;
        keyStr = name.substring(0, 1).toUpperCase() + codeOfPlayer;
        codeOfPlayer++;
    }
}