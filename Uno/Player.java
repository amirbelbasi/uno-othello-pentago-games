import java.util.ArrayList;
import java.util.Scanner;

/**
 * Player
 */
public class Player {
    private String name;
    private String type;
    private int points = 0;
    private ArrayList<Card> playerCards = new ArrayList<>();
    private ArrayList<Card> possibleCards = new ArrayList<>();
    public Player(String name, String type)
    {
        this.name = name;
        this.type = type;
    }
}