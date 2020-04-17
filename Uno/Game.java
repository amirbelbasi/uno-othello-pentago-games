import java.util.ArrayList;
import java.util.Random;

enum CardTypes
{
    RED,
    YELLOW,
    GREEN,
    BLUE,
    WILD
}

/**
 * Game
 */
public class Game {
    private final int CARDINALINETOBESHOWN = 10;
    private int turn;
    private int nplayers;
    public boolean endGame = false;
    public String direction = "clock wise";
    public String currentColor;
    private Card currentCard;
    public ArrayList<Player> players = new ArrayList<>();
    public ArrayList<Card> storage = new ArrayList<>();
    {
        String currentColor = "red";
        for (int i = 1; i <= 19; i++)
        {
            storage.add(new Card(i % 10 + "", currentColor));
        }
        storage.add(new Card("skip", currentColor));
        storage.add(new Card("skip", currentColor));
        storage.add(new Card("reverse", currentColor));
        storage.add(new Card("reverse", currentColor));
        storage.add(new Card("draw", currentColor));
        storage.add(new Card("draw", currentColor));
        currentColor = "yellow";
        for (int i = 1; i <= 19; i++)
        {
            storage.add(new Card(i % 10 + "", currentColor));
        }
        storage.add(new Card("skip", currentColor));
        storage.add(new Card("skip", currentColor));
        storage.add(new Card("reverse", currentColor));
        storage.add(new Card("reverse", currentColor));
        storage.add(new Card("draw", currentColor));
        storage.add(new Card("draw", currentColor));
        currentColor = "green";
        for (int i = 1; i <= 19; i++)
        {
            storage.add(new Card(i % 10 + "", currentColor));
        }
        storage.add(new Card("skip", currentColor));
        storage.add(new Card("skip", currentColor));
        storage.add(new Card("reverse", currentColor));
        storage.add(new Card("reverse", currentColor));
        storage.add(new Card("draw", currentColor));
        storage.add(new Card("draw", currentColor));
        currentColor = "blue";
        for (int i = 1; i <= 19; i++)
        {
            storage.add(new Card(i % 10 + "", currentColor));
        }
        storage.add(new Card("skip", currentColor));
        storage.add(new Card("skip", currentColor));
        storage.add(new Card("reverse", currentColor));
        storage.add(new Card("reverse", currentColor));
        storage.add(new Card("draw", currentColor));
        storage.add(new Card("draw", currentColor));
        currentColor = "black";
        storage.add(new Card("wild color", currentColor));
        storage.add(new Card("wild color", currentColor));
        storage.add(new Card("wild color", currentColor));
        storage.add(new Card("wild color", currentColor));
        storage.add(new Card("wild draw", currentColor));
        storage.add(new Card("wild draw", currentColor));
        storage.add(new Card("wild draw", currentColor));
        storage.add(new Card("wild draw", currentColor));
    }
    
    public Game(int nplayers)
    {
        while(true)
        {
            int randomIndex = myRandom.nextInt(108);
            if(storage.get(randomIndex).color != "black")
            {
                currentCard = storage.get(randomIndex);
                storage.remove(randomIndex);
                break;
            }
        }
        this.nplayers = nplayers;
    }

    /**
     * a round of game
     */
    public void startARound()
    {
        while(true)
        {
            players.get(index)
        }
    }
    
    /**
     * switchs the direction of game
     */
    private void switchDirection()
    {
        direction = direction.equals("clock wise") ? "clock wise" : "counter clock wise";
    }

    /**
     * determines possible cards to be played
     * @param currentCard
     * @return
     */
    protected void determinePossibleCards(Card currentCard, String currentColor, ArrayList<Card> cardsArrayList)
    {
        ArrayList<Card> cardsHolder = new ArrayList<>();
        for(Card i: storage)
        {
            if(i)
        }
    }

    /**
     * generates a random index for a given arraylist
     * @param myArrayList
     * @return
     */
    private int randomIndex(ArrayList<Card> myArrayList)
    {
        Random myRandom = new Random();
        return myRandom.nextInt(myArrayList.size());
    }
    
    /**
     * shows card collection
     */
    public void showCardCollection(ArrayList<Card> cardCollection) {
        char squareWithVerticalFillCh = '\u25A5'; //▥
        String cardColorCh; // a single char that represents the color of card
        int nUsedCards = 0;
        for (int line = 1; line <= (int)(cardCollection.size() / CARDINALINETOBESHOWN) + 1; line++)
        {
            ArrayList<Card> cardsInALineOfCards = new ArrayList<>();
            for (int i = 0; i < Math.min(CARDINALINETOBESHOWN, cardCollection.size() - nUsedCards); i++)
            {
                cardsInALineOfCards.add(cardCollection.get(i + nUsedCards));
            }
            for (int i = 0; i < 8; i++)
            {
                for (int j = 0; j < cardsInALineOfCards.size(); j++)
                {
                    cardColorCh = cardsInALineOfCards.get(j).color.equals("black") ? "W" : (cardsInALineOfCards.get(j).color.charAt(0)+"").toUpperCase();
                    if (i % 7 == 0) // 0 7
                    {
                        System.out.print(squareWithVerticalFillCh + " " + cardColorCh + " " + cardColorCh + " " + cardColorCh
                        + " " + cardColorCh + " " + squareWithVerticalFillCh);
                    }
                    else if (i % 4 == 1 || i % 4 == 2) // 1 2 5 6
                    {
                        System.out.print(squareWithVerticalFillCh + "         " + squareWithVerticalFillCh);
                    }
                    else // 3 4
                    {
                        if (i == 3)
                        {
                            if(cardsInALineOfCards.get(j).name.equals("wild color") || cardsInALineOfCards.get(j).name.equals("wild draw"))
                            {
                                System.out.print(squareWithVerticalFillCh + "  wild   " + squareWithVerticalFillCh);
                            }
                            else if(cardsInALineOfCards.get(j).name.equals("skip"))
                            {
                                System.out.print(squareWithVerticalFillCh + "  skip   " + squareWithVerticalFillCh);
                            }
                            else if(cardsInALineOfCards.get(j).name.equals("reverse"))
                            {
                                System.out.print(squareWithVerticalFillCh + " reverse " + squareWithVerticalFillCh);
                            }
                            else if(cardsInALineOfCards.get(j).name.equals("draw"))
                            {
                                System.out.print(squareWithVerticalFillCh + "  draw   " + squareWithVerticalFillCh);
                            }
                            else // number type card
                            {
                                System.out.print(squareWithVerticalFillCh + "    " + cardsInALineOfCards.get(j).name + "    " + squareWithVerticalFillCh);
                            }
                        }
                        else
                        {
                            if(cardsInALineOfCards.get(j).name.equals("wild color"))
                            {
                                System.out.print(squareWithVerticalFillCh + "  color  " + squareWithVerticalFillCh);
                            }
                            else if(cardsInALineOfCards.get(j).name.equals("wild draw"))
                            {
                                System.out.print(squareWithVerticalFillCh + "  draw   " + squareWithVerticalFillCh);
                            }
                            else
                            {
                                System.out.print(squareWithVerticalFillCh + "         " + squareWithVerticalFillCh);
                            }
                        }
                    }
                    System.out.print("  ");
                }
                System.out.println();
            }
            System.out.println();
            nUsedCards += CARDINALINETOBESHOWN;
        }
    }
}