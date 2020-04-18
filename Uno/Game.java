import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.io.IOException;

enum CardTypes {
    RED, YELLOW, GREEN, BLUE, WILD
}

enum TurnType {
    PC, USER
}

/**
 * Game
 */
public class Game {
    private final int CARDINALINETOBESHOWN = 10;
    private int turn = 0;
    private int nplayers;
    public boolean endGame = false;
    public String direction = "clock wise";
    public String currentColor;
    private Card currentCard;
    private int consecutiveDraws = 1;
    private int consecutiveWildDraws = 1;
    private boolean isCurrentCardFresh = true;
    public ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Card> storage = new ArrayList<>();
    {
        String currentColor = "red";
        for (int i = 1; i <= 19; i++) {
            storage.add(new Card(i % 10 + "", currentColor));
        }
        storage.add(new Card("skip", currentColor));
        storage.add(new Card("skip", currentColor));
        storage.add(new Card("reverse", currentColor));
        storage.add(new Card("reverse", currentColor));
        storage.add(new Card("draw", currentColor));
        storage.add(new Card("draw", currentColor));
        currentColor = "yellow";
        for (int i = 1; i <= 19; i++) {
            storage.add(new Card(i % 10 + "", currentColor));
        }
        storage.add(new Card("skip", currentColor));
        storage.add(new Card("skip", currentColor));
        storage.add(new Card("reverse", currentColor));
        storage.add(new Card("reverse", currentColor));
        storage.add(new Card("draw", currentColor));
        storage.add(new Card("draw", currentColor));
        currentColor = "green";
        for (int i = 1; i <= 19; i++) {
            storage.add(new Card(i % 10 + "", currentColor));
        }
        storage.add(new Card("skip", currentColor));
        storage.add(new Card("skip", currentColor));
        storage.add(new Card("reverse", currentColor));
        storage.add(new Card("reverse", currentColor));
        storage.add(new Card("draw", currentColor));
        storage.add(new Card("draw", currentColor));
        currentColor = "blue";
        for (int i = 1; i <= 19; i++) {
            storage.add(new Card(i % 10 + "", currentColor));
        }
        storage.add(new Card("skip", currentColor));
        storage.add(new Card("skip", currentColor));
        storage.add(new Card("reverse", currentColor));
        storage.add(new Card("reverse", currentColor));
        storage.add(new Card("draw", currentColor));
        storage.add(new Card("draw", currentColor));
        currentColor = "black";
        storage.add(new Card("wild-color", currentColor));
        storage.add(new Card("wild-color", currentColor));
        storage.add(new Card("wild-color", currentColor));
        storage.add(new Card("wild-color", currentColor));
        storage.add(new Card("wild-draw", currentColor));
        storage.add(new Card("wild-draw", currentColor));
        storage.add(new Card("wild-draw", currentColor));
        storage.add(new Card("wild-draw", currentColor));
    }

    public Game(int nplayers) {
        Random myRandom = new Random();
        while (true) {
            int randomIndex = myRandom.nextInt(108);
            if (storage.get(randomIndex).color != "black") {
                currentCard = storage.get(randomIndex);
                storage.remove(randomIndex);
                updateCurrentColor();
                break;
            }
        }
        this.nplayers = nplayers;
    }

    private void showGame() throws InterruptedException, IOException
    {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        System.out.println("current direction: " + direction);
        System.out.println("current color: " + currentColor);
        System.out.println("---------------------");
        switch (nplayers) {
            case 3:
            {
                System.out.println(players.get(1).keyStr + "      " + players.get(2).keyStr);
                System.out.println();
                System.out.println();
                System.out.println("    " + players.get(0).keyStr);
                break;
            }
            case 4:
            {
                System.out.println("     " + players.get(2).keyStr);
                System.out.println();
                System.out.println(players.get(1).keyStr + "        " + players.get(3).keyStr);
                System.out.println();
                System.out.println("     " + players.get(0).keyStr);
                break;
            }
            case 5:
            {
                System.out.println("   " + players.get(2).keyStr + "    " + players.get(3).keyStr);
                System.out.println();
                System.out.println(players.get(1).keyStr + "          " + players.get(4).keyStr);
                System.out.println();
                System.out.println("      " + players.get(0).keyStr);
                break;
            }
        }
        System.out.println("---------------------");
        System.out.println("number of cards: ");
        for (int i = 0; i < players.size(); i++)
        {
            System.out.println(players.get(i).name + " (" + players.get(i).keyStr + ")" + ": " + players.get(i).playerCards.size());
        }
        System.out.println("---------------------");
        System.out.println("card on the ground: ");
        ArrayList<Card> tmp = new ArrayList<>();
        tmp.add(currentCard);
        showCardCollection(tmp);
        System.out.println("---------------------");
        showCardCollection(players.get(turn).playerCards);
        System.out.println("---------------------");
        System.out.println(players.get(turn).name + "'s (" + players.get(turn).keyStr + ") turn: ");
    }

    /**
     * updates current color
     */
    private void updateCurrentColor() {
        currentColor = currentCard.color;
    }

    /**
     * a round of game
     */
    public void startARound() throws InterruptedException, IOException {
        Random myRandom = new Random();
        turn = myRandom.nextInt(nplayers);
        for (int i = 0; i < nplayers; i++) {
            getCard(7, i);
        }
        while (true) {
            showGame();
            Thread.sleep(2500);
            if (players.get(turn).type.equals("PC")) {
                playPCTurn();
            } else {
                playUserTurn();
            }
        }
    }

    /**
     * a turn played by pc
     */
    private void playPCTurn() throws InterruptedException, IOException {
        players.get(turn).possibleCards = new ArrayList<>();
        if (currentCard.name.equals("draw") && isCurrentCardFresh) {
            for (int i = 0; i < players.get(turn).playerCards.size(); i++) {
                if (players.get(turn).playerCards.get(i).name.equals("draw")) {
                    dropCard(players.get(turn).playerCards.get(i), TurnType.PC.ordinal());
                    return;
                }
            }
            getCard(consecutiveDraws * 2, turn);
            consecutiveDraws = 1;
            return;
        }
        if (currentCard.name.equals("wild-draw") && isCurrentCardFresh) {
            for (int i = 0; i < players.get(turn).playerCards.size(); i++) {
                if (players.get(turn).playerCards.get(i).name.equals("wild-draw")) {
                    dropCard(players.get(turn).playerCards.get(i), TurnType.PC.ordinal());
                    return;
                }
            }
            getCard(consecutiveWildDraws * 4, turn);
            consecutiveWildDraws = 1;
            return;
        }
        determinePossibleCards();
        if (players.get(turn).possibleCards.size() == 0) {
            for (Card i : players.get(turn).playerCards) {
                if (i.name.equals("wild-draw")) {
                    dropCard(i, TurnType.PC.ordinal());
                    return;
                }
            }
            getCard(1, turn);
            determinePossibleCards();
            if (players.get(turn).possibleCards.size() != 0) {
                dropCard(players.get(turn).possibleCards.get(0), TurnType.PC.ordinal());
            }
            advanceTurn();
        } else {
            dropCard(players.get(turn).possibleCards.get(randomIndex(players.get(turn).possibleCards)), TurnType.PC.ordinal());
        }
    }

    /**
     * a turn played by user
     */
    private void playUserTurn() throws InterruptedException, IOException {
        players.get(turn).possibleCards = new ArrayList<>();
        if (currentCard.name.equals("draw") && isCurrentCardFresh) {
            for (int i = 0; i < players.get(turn).playerCards.size(); i++) {
                if (players.get(turn).playerCards.get(i).name.equals("draw")) {
                    players.get(turn).possibleCards.add(players.get(turn).playerCards.get(i));
                }
            }
            if(players.get(turn).possibleCards.size() != 0)
            {
                getPlayerInp();
            }
            getCard(consecutiveDraws * 2, turn);
            consecutiveDraws = 1;
            return;
        }
        if (currentCard.name.equals("wild-draw") && isCurrentCardFresh) {
            for (int i = 0; i < players.get(turn).playerCards.size(); i++) {
                if (players.get(turn).playerCards.get(i).name.equals("wild-draw")) {
                    players.get(turn).possibleCards.add(players.get(turn).playerCards.get(i));
                }
            }
            if(players.get(turn).possibleCards.size() != 0)
            {
                getPlayerInp();
            }
            getCard(consecutiveDraws * 4, turn);
            consecutiveDraws = 1;
            return;
        }
        determinePossibleCards();
        if (players.get(turn).possibleCards.size() == 0) {
            for (Card i : players.get(turn).playerCards) {
                if (i.name.equals("wild-draw")) {
                    getPlayerInp();
                }
            }
            getCard(1, turn);
            determinePossibleCards();
            if (players.get(turn).possibleCards.size() != 0) {
                getPlayerInp();
            }
            advanceTurn();
        }
        else {
            getPlayerInp();
        }
    }

    /**
     * gets info of card player want to drop & drop it
     * @throws InterruptedException
     * @throws IOException
     */
    private void getPlayerInp() throws InterruptedException, IOException {
        Scanner myScanner = new Scanner(System.in);
        while (true) {
            showGame();
            System.out.print("enter card you want to drop: ");
            String inp = myScanner.nextLine();
            String[] tokenized = new String[2];
            tokenized = inp.split(" ");
            for (Card i: players.get(turn).possibleCards)
            {
                if(i.name.equals(tokenized[0]) && i.color.equals(tokenized[1]))
                {
                    dropCard(i, TurnType.USER.ordinal());
                    return;
                }
            }
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            System.out.println("invalid input");
            Thread.sleep(1500);
        }
    }
    
    /**
     * determines possible moves for a player
     */
    private void determinePossibleCards() {
        players.get(turn).possibleCards = new ArrayList<>();
        for (int i = 0; i < players.get(turn).playerCards.size(); i++) {
            if (players.get(turn).playerCards.get(i).name.equals("wild-color")) {
                players.get(turn).possibleCards.add(players.get(turn).playerCards.get(i));
                continue;
            }
            if (currentCard.color.equals("black")) {
                if (players.get(turn).playerCards.get(i).color.equals(currentColor)) {
                    players.get(turn).possibleCards.add(players.get(turn).playerCards.get(i));
                }
            } else if (players.get(turn).playerCards.get(i).name.equals(currentCard.name)
                    || players.get(turn).playerCards.get(i).color.equals(currentColor)) {
                players.get(turn).possibleCards.add(players.get(turn).playerCards.get(i));
            }
        }
    }

    /**
     * a player drops a card using card taken from possible moves arraylist
     */
    private void dropCard(Card cardToBeAdd, int turnType) throws InterruptedException, IOException  {
        if(cardToBeAdd.color.equals("black")) {
            askColor(turnType);
        }
        if(cardToBeAdd.name.equals("skip"))
        {
            advanceTurn();
        }
        if(cardToBeAdd.name.equals("reverse"))
        {
            switchDirection();
        }
        if (cardToBeAdd.name.equals("draw"))
        {
            consecutiveDraws++;
        }
        if (cardToBeAdd.name.equals("wild-draw"))
        {
            consecutiveWildDraws++;
        }
        storage.add(cardToBeAdd);
        currentCard = cardToBeAdd;
        players.get(turn).playerCards.remove(cardToBeAdd);
        updateCurrentColor();
        advanceTurn();
        isCurrentCardFresh = true;
    }

    /**
     * a players gets <ncards> cards
     */
    private void getCard(int ncards, int indexOfPlayer) {
        for (int i = 0; i < ncards; i++) {
            int randomIndex = randomIndex(storage);
            players.get(indexOfPlayer).playerCards.add(storage.get(randomIndex));
            storage.remove(randomIndex);
        }
        advanceTurn();
        isCurrentCardFresh = false;
    }

    /**
     * advances the turn one step in the current direction
     */
    private void advanceTurn()
    {
        if(direction.equals("clock wise"))
        {
            if(turn == nplayers - 1)
            {
                turn = 0;
            }
            else
            {
                turn++;
            }
        }
        else
        {
            if(turn == 0)
            {
                turn = nplayers - 1;
            }
            else
            {
                turn--;
            }
        }
    }
    
    /**
     * asks color from user or randomly chooses a color for current color if
     * it is called by pc (TurnType.PC)
     * @param turnType
     * @throws InterruptedException
     * @throws IOException
     */
    private void askColor(int turnType) throws InterruptedException, IOException {
        if (turnType == TurnType.PC.ordinal()) {
            Random myRandom = new Random();
            String[] colors = new String[4];
            colors[0] = "red";
            colors[1] = "yellow";
            colors[2] = "green";
            colors[3] = "blue";
            currentColor = colors[myRandom.nextInt(4)];
        } else {
            while(true)
            {
                showGame();
                Scanner myScanner = new Scanner(System.in);
                System.out.print("enter current color you wish: ");
                String colorToBeChanged = myScanner.nextLine();
                if (!colorToBeChanged.equals("red") && !colorToBeChanged.equals("yellow") && !colorToBeChanged.equals("green")
                        && !colorToBeChanged.equals("blue")) {
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                    System.out.println("invalid input");
                    Thread.sleep(1500);
                    continue;
                }
                currentColor = colorToBeChanged;
                break;
            }
        }
    }

    /**
     * switchs the direction of game
     */
    private void switchDirection() {
        direction = direction.equals("clock wise") ? "clock wise" : "counter clock wise";
    }

    /**
     * generates a random index for a given arraylist
     * 
     * @param myArrayList
     * @return
     */
    private int randomIndex(ArrayList<Card> myArrayList) {
        Random myRandom = new Random();
        return myRandom.nextInt(myArrayList.size());
    }

    /**
     * shows card collection
     */
    private void showCardCollection(ArrayList<Card> cardCollection) {
        char squareWithVerticalFillCh = '\u25A5'; // ▥
        String cardColorCh; // a single char that represents the color of card
        int nUsedCards = 0;
        for (int line = 1; line <= (int) (cardCollection.size() / CARDINALINETOBESHOWN) + 1; line++) {
            ArrayList<Card> cardsInALineOfCards = new ArrayList<>();
            for (int i = 0; i < Math.min(CARDINALINETOBESHOWN, cardCollection.size() - nUsedCards); i++) {
                cardsInALineOfCards.add(cardCollection.get(i + nUsedCards));
            }
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < cardsInALineOfCards.size(); j++) {
                    cardColorCh = cardsInALineOfCards.get(j).color.equals("black") ? "W"
                            : (cardsInALineOfCards.get(j).color.charAt(0) + "").toUpperCase();
                    if (i % 7 == 0) // 0 7
                    {
                        System.out.print(squareWithVerticalFillCh + " " + cardColorCh + " " + cardColorCh + " "
                                + cardColorCh + " " + cardColorCh + " " + squareWithVerticalFillCh);
                    } else if (i % 4 == 1 || i % 4 == 2) // 1 2 5 6
                    {
                        System.out.print(squareWithVerticalFillCh + "         " + squareWithVerticalFillCh);
                    } else // 3 4
                    {
                        if (i == 3) {
                            if (cardsInALineOfCards.get(j).name.equals("wild-color")
                                    || cardsInALineOfCards.get(j).name.equals("wild-draw")) {
                                System.out.print(squareWithVerticalFillCh + "  wild   " + squareWithVerticalFillCh);
                            } else if (cardsInALineOfCards.get(j).name.equals("skip")) {
                                System.out.print(squareWithVerticalFillCh + "  skip   " + squareWithVerticalFillCh);
                            } else if (cardsInALineOfCards.get(j).name.equals("reverse")) {
                                System.out.print(squareWithVerticalFillCh + " reverse " + squareWithVerticalFillCh);
                            } else if (cardsInALineOfCards.get(j).name.equals("draw")) {
                                System.out.print(squareWithVerticalFillCh + "  draw   " + squareWithVerticalFillCh);
                            } else // number type card
                            {
                                System.out.print(squareWithVerticalFillCh + "    " + cardsInALineOfCards.get(j).name
                                        + "    " + squareWithVerticalFillCh);
                            }
                        } else {
                            if (cardsInALineOfCards.get(j).name.equals("wild-color")) {
                                System.out.print(squareWithVerticalFillCh + "  color  " + squareWithVerticalFillCh);
                            } else if (cardsInALineOfCards.get(j).name.equals("wild-draw")) {
                                System.out.print(squareWithVerticalFillCh + "  draw   " + squareWithVerticalFillCh);
                            } else {
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