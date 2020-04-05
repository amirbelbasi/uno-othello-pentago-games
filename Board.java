import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.swing.border.Border;
import java.io.IOException;

enum Turn {
    BLACK, WHITE;
}

/**
 * class to represent board of othello
 * 
 * @author Amirali Belbasi
 */
public class Board {
    /*
     * IMPORTENT NOTE: \u25CF is the unicode of black circle but how ever its shown
     * im my terminal as a white circle so Im using it as white circle and same goes
     * for black, \u25CB is the unicode of white circle
     */
    private char whiteCh = '\u25CF';
    private char blackCh = '\u25CB';
    private char turnCh = '\u25CE';
    private int nWhite = 0;
    private int nBlack = 0;
    private int turn = Turn.BLACK.ordinal();
    private char[] turns = new char[2];
    {
        turns[0] = blackCh;
        turns[1] = whiteCh;
    }
    String[] playerNames = new String[2];
    private ArrayList<String> possibleMoves = new ArrayList<>();
    private char[][] board = new char[8][8];
    {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = ' ';
            }
        }
        board[3][3] = whiteCh;
        board[3][4] = blackCh;
        board[4][3] = blackCh;
        board[4][4] = whiteCh;
    }
    // determines priority of squares for pc
    private ArrayList<String> firstPrioritySquares = new ArrayList<>();
    {
        firstPrioritySquares.add("00");
        firstPrioritySquares.add("07");
        firstPrioritySquares.add("70");
        firstPrioritySquares.add("77");
    }
    private ArrayList<String> secondPrioritySquares = new ArrayList<>();
    {
        secondPrioritySquares.add("22");
        secondPrioritySquares.add("23");
        secondPrioritySquares.add("24");
        secondPrioritySquares.add("25");
        secondPrioritySquares.add("32");
        secondPrioritySquares.add("33");
        secondPrioritySquares.add("34");
        secondPrioritySquares.add("35");
        secondPrioritySquares.add("42");
        secondPrioritySquares.add("43");
        secondPrioritySquares.add("44");
        secondPrioritySquares.add("45");
        secondPrioritySquares.add("52");
        secondPrioritySquares.add("53");
        secondPrioritySquares.add("54");
        secondPrioritySquares.add("55");
    }
    private ArrayList<String> thirdPrioritySquares = new ArrayList<>();
    {
        thirdPrioritySquares.add("02");
        thirdPrioritySquares.add("03");
        thirdPrioritySquares.add("04");
        thirdPrioritySquares.add("05");
        thirdPrioritySquares.add("12");
        thirdPrioritySquares.add("13");
        thirdPrioritySquares.add("14");
        thirdPrioritySquares.add("15");
        thirdPrioritySquares.add("20");
        thirdPrioritySquares.add("21");
        thirdPrioritySquares.add("26");
        thirdPrioritySquares.add("27");
        thirdPrioritySquares.add("30");
        thirdPrioritySquares.add("31");
        thirdPrioritySquares.add("36");
        thirdPrioritySquares.add("37");
        thirdPrioritySquares.add("40");
        thirdPrioritySquares.add("41");
        thirdPrioritySquares.add("46");
        thirdPrioritySquares.add("47");
        thirdPrioritySquares.add("50");
        thirdPrioritySquares.add("51");
        thirdPrioritySquares.add("56");
        thirdPrioritySquares.add("57");
        thirdPrioritySquares.add("62");
        thirdPrioritySquares.add("63");
        thirdPrioritySquares.add("64");
        thirdPrioritySquares.add("65");
        thirdPrioritySquares.add("72");
        thirdPrioritySquares.add("73");
        thirdPrioritySquares.add("74");
        thirdPrioritySquares.add("75");
    }
    private ArrayList<String> forthPrioritySquares = new ArrayList<>();
    {
        forthPrioritySquares.add("01");
        forthPrioritySquares.add("06");
        forthPrioritySquares.add("10");
        forthPrioritySquares.add("11");
        forthPrioritySquares.add("16");
        forthPrioritySquares.add("17");
        forthPrioritySquares.add("60");
        forthPrioritySquares.add("61");
        forthPrioritySquares.add("66");
        forthPrioritySquares.add("67");
        forthPrioritySquares.add("71");
        forthPrioritySquares.add("76");
    }

    public Board(String playerOneName, String playerTwoName) {
        playerNames[0] = playerOneName;
        playerNames[1] = playerTwoName;
    }

    /**
     * shows current state of board
     */
    public void showBoard() {
        possibleMoves = determinePossibleMoves(board);
        updateNumberOfDiscs();
        System.out.println("White: " + nWhite + "          Black: " + nBlack);
        System.out.println("--------------------------");
        System.out.print("  ");
        for (int i = 0; i < 8; i++) {
            System.out.print(" " + (char) (i + 65) + " ");
        }
        System.out.println();
        for (int i = 0; i < 8; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < 8; j++) {
                System.out.print("[" + board[i][j] + "]");
            }
            System.out.println();
        }
    }

    /**
     * converts input of player to a standard format e.g. "1 A" to "00"
     * 
     * @param str player input like "4 B"
     * @return formated stirng
     */
    private String toStandardFormat(String str) {
        return String.valueOf(new char[] { (char) (str.charAt(0) - 1), (char) (str.charAt(2) - 17) });
    }

    /**
     * checks if
     * 
     * @param str player input like "4 B"
     * @return true if input is valid
     */
    public boolean isValidInput(String str) {
        if (str.length() == 3 && 65 <= (int) str.charAt(2) && (int) str.charAt(2) <= 72 && 49 <= (int) str.charAt(0)
                && (int) str.charAt(0) <= 56 && str.charAt(1) == ' ') {
            return true;
        }
        return false;
    }

    /**
     * determines possible moves on the board
     * 
     * @return possible moves
     */
    private ArrayList<String> determinePossibleMoves(char[][] board) {
        ArrayList<String> tmp = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == blackCh || board[i][j] == whiteCh)
                    continue;
                if (board[i][j] == turnCh) {
                    tmp.add(i + "" + j);
                } else if (isPossibleMove(board, i, j)) {
                    board[i][j] = turnCh;
                    tmp.add(i + "" + j);
                }
            }
        }
        return tmp;
    }

    /**
     * checks if chosen block is a valid move to be done by player
     * 
     * @param i row index of block
     * @param j column index of block
     * @return true if its a valid move
     */
    private boolean isPossibleMove(char[][] board, int i, int j) {
        if (isValidBlock(i - 1, j) && board[i - 1][j] == turns[oppositeTurn(turn)]) {
            for (int k = 2; k < 8; k++) {
                if (isValidBlock(i - k, j) && board[i - k][j] == turns[turn]) {
                    return true;
                }
            }
        }
        if (isValidBlock(i + 1, j) && board[i + 1][j] == turns[oppositeTurn(turn)]) {
            for (int k = 2; k < 8; k++) {
                if (isValidBlock(i + k, j) && board[i + k][j] == turns[turn]) {
                    return true;
                }
            }
        }
        if (isValidBlock(i, j - 1) && board[i][j - 1] == turns[oppositeTurn(turn)]) {
            for (int k = 2; k < 8; k++) {
                if (isValidBlock(i, j - k) && board[i][j - k] == turns[turn]) {
                    return true;
                }
            }
        }
        if (isValidBlock(i, j + 1) && board[i][j + 1] == turns[oppositeTurn(turn)]) {
            for (int k = 2; k < 8; k++) {
                if (isValidBlock(i, j + k) && board[i][j + k] == turns[turn]) {
                    return true;
                }
            }
        }
        if (isValidBlock(i - 1, j - 1) && board[i - 1][j - 1] == turns[oppositeTurn(turn)]) {
            for (int k = 2; k < 8; k++) {
                if (isValidBlock(i - k, j - k) && board[i - k][j - k] == turns[turn]) {
                    return true;
                }
            }
        }
        if (isValidBlock(i + 1, j + 1) && board[i + 1][j + 1] == turns[oppositeTurn(turn)]) {
            for (int k = 2; k < 8; k++) {
                if (isValidBlock(i + k, j + k) && board[i + k][j + k] == turns[turn]) {
                    return true;
                }
            }
        }
        if (isValidBlock(i + 1, j - 1) && board[i + 1][j - 1] == turns[oppositeTurn(turn)]) {
            for (int k = 2; k < 8; k++) {
                if (isValidBlock(i + k, j - k) && board[i + k][j - k] == turns[turn]) {
                    return true;
                }
            }
        }
        if (isValidBlock(i - 1, j + 1) && board[i - 1][j + 1] == turns[oppositeTurn(turn)]) {
            for (int k = 2; k < 8; k++) {
                if (isValidBlock(i - k, j + k) && board[i - k][j + k] == turns[turn]) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * checks that chosen block exists
     * 
     * @param i row index of block
     * @param j column index of block
     * @return true if chosen block is valid
     */
    private boolean isValidBlock(int i, int j) {
        if (i < 0 || i > 7 || j < 0 || j > 7) // its not out of board
            return false;
        return true;
    }

    /**
     * returns opposite turn
     * 
     * @param turn crrent turn
     * @return opposite turn
     */
    private int oppositeTurn(int turn) {
        if (turn == Turn.BLACK.ordinal())
            return Turn.WHITE.ordinal();
        else
            return Turn.BLACK.ordinal();
    }

    /**
     * plays a turn and changes turn
     * 
     * @param playerInp
     */
    public void playTurn(String playerInp) throws InterruptedException, IOException, InterruptedException {
        if (!isValidInput(playerInp)) {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            System.out.println("invalid input");
            Thread.sleep(1500);
            return;
        }
        String stPlayerInp = toStandardFormat(playerInp);
        if (possibleMoves.contains(stPlayerInp)) {
            rotateAllInBetween(board, Integer.parseInt(stPlayerInp.charAt(0) + ""),
                    Integer.parseInt(stPlayerInp.charAt(1) + ""));
            board[Integer.parseInt(stPlayerInp.charAt(0) + "")][Integer
                    .parseInt(stPlayerInp.charAt(1) + "")] = turns[turn];
            turn = oppositeTurn(turn);
            clearTurnChs();
        } else {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            System.out.println("invalid block");
            Thread.sleep(1500);
        }
    }

    /**
     * clears map form turn character
     */
    public void clearTurnChs() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == turnCh) {
                    board[i][j] = ' ';
                }
            }
        }
    }

    /**
     * updates number of discs
     */
    public void updateNumberOfDiscs() {
        int nWhiteHolder = 0;
        int nBlackHolder = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == whiteCh) {
                    nWhiteHolder++;
                }
                if (board[i][j] == blackCh) {
                    nBlackHolder++;
                }
            }
        }
        nWhite = nWhiteHolder;
        nBlack = nBlackHolder;
    }

    /**
     * proper massage to enter input
     */
    public void enterInp() {
        System.out.print(playerNames[turn] + "'s turn: ");
    }

    /**
     * rotates all discs between two of player's discs
     * 
     * @param i row index
     * @param j columns index
     */
    public void rotateAllInBetween(char[][] board, int i, int j) {
        if (isValidBlock(i - 1, j) && board[i - 1][j] == turns[oppositeTurn(turn)]) {
            for (int k = 2; k < 8; k++) {
                if (isValidBlock(i - k, j) && board[i - k][j] == turns[turn]) {
                    for (int x = 1; x < k; x++) {
                        board[i - x][j] = turns[turn];
                    }
                    break;
                }
            }
        }
        if (isValidBlock(i + 1, j) && board[i + 1][j] == turns[oppositeTurn(turn)]) {
            for (int k = 2; k < 8; k++) {
                if (isValidBlock(i + k, j) && board[i + k][j] == turns[turn]) {
                    for (int x = 1; x < k; x++) {
                        board[i + x][j] = turns[turn];
                    }
                    break;
                }
            }
        }
        if (isValidBlock(i, j - 1) && board[i][j - 1] == turns[oppositeTurn(turn)]) {
            for (int k = 2; k < 8; k++) {
                if (isValidBlock(i, j - k) && board[i][j - k] == turns[turn]) {
                    for (int x = 1; x < k; x++) {
                        board[i][j - x] = turns[turn];
                    }
                    break;
                }
            }
        }
        if (isValidBlock(i, j + 1) && board[i][j + 1] == turns[oppositeTurn(turn)]) {
            for (int k = 2; k < 8; k++) {
                if (isValidBlock(i, j + k) && board[i][j + k] == turns[turn]) {
                    for (int x = 1; x < k; x++) {
                        board[i][j + x] = turns[turn];
                    }
                    break;
                }
            }
        }
        if (isValidBlock(i - 1, j - 1) && board[i - 1][j - 1] == turns[oppositeTurn(turn)]) {
            for (int k = 2; k < 8; k++) {
                if (isValidBlock(i - k, j - k) && board[i - k][j - k] == turns[turn]) {
                    for (int x = 1; x < k; x++) {
                        board[i - x][j - x] = turns[turn];
                    }
                    break;
                }
            }
        }
        if (isValidBlock(i + 1, j + 1) && board[i + 1][j + 1] == turns[oppositeTurn(turn)]) {
            for (int k = 2; k < 8; k++) {
                if (isValidBlock(i + k, j + k) && board[i + k][j + k] == turns[turn]) {
                    for (int x = 1; x < k; x++) {
                        board[i + x][j + x] = turns[turn];
                    }
                    break;
                }
            }
        }
        if (isValidBlock(i + 1, j - 1) && board[i + 1][j - 1] == turns[oppositeTurn(turn)]) {
            for (int k = 2; k < 8; k++) {
                if (isValidBlock(i + k, j - k) && board[i + k][j - k] == turns[turn]) {
                    for (int x = 1; x < k; x++) {
                        board[i + x][j - x] = turns[turn];
                    }
                    break;
                }
            }
        }
        if (isValidBlock(i - 1, j + 1) && board[i - 1][j + 1] == turns[oppositeTurn(turn)]) {
            for (int k = 2; k < 8; k++) {
                if (isValidBlock(i - k, j + k) && board[i - k][j + k] == turns[turn]) {
                    for (int x = 1; x < k; x++) {
                        board[i - x][j + x] = turns[turn];
                    }
                    break;
                }
            }
        }
    }

    /**
     * checks if is there any possible move
     * 
     * @return true if there is no possible move
     */
    public boolean checkPass() throws InterruptedException, IOException, InterruptedException {
        if (possibleMoves.isEmpty()) {
            System.out.println("pass");
            turn = oppositeTurn(turn);
            clearTurnChs();
            Thread.sleep(2500);
            return true;
        }
        return false;
    }

    /**
     * determines the winner
     */
    public void determineWinner() {
        int nWhite = 0;
        int nBlack = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == whiteCh) {
                    nWhite++;
                }
                if (board[i][j] == blackCh) {
                    nBlack++;
                }
            }
        }
        if (nWhite == nBlack) {
            System.out.println("Game is tie");
            return;
        }
        int index = nBlack > nWhite ? 0 : 1;
        System.out.println("The winner is: " + playerNames[index]);
    }

    /**
     * checks if board is full of discs
     * 
     * @return true if board is full
     */
    public boolean isFull() {
        int counter = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == blackCh || board[i][j] == whiteCh) {
                    counter++;
                }
            }
        }
        if (counter == 64) {
            return true;
        }
        return false;
    }

    /**
     * @return the turn
     */
    public int getTurn() {
        return turn;
    }

    /**
     * generates a move for PC
     * 
     * @return a string representing PC's move
     */
    public String generatePCMove() {
        ArrayList<String> possibleFirstPrioritySquares = new ArrayList<>();
        ArrayList<String> possibleSecondPrioritySquares = new ArrayList<>();
        ArrayList<String> possibleThirdPrioritySquares = new ArrayList<>();
        ArrayList<String> possibleForthPrioritySquares = new ArrayList<>();
        for (String i : possibleMoves) {
            if (firstPrioritySquares.contains(i)) {
                possibleFirstPrioritySquares.add(i);
            }
            if (secondPrioritySquares.contains(i)) {
                possibleSecondPrioritySquares.add(i);
            }
            if (thirdPrioritySquares.contains(i)) {
                possibleThirdPrioritySquares.add(i);
            }
            if (forthPrioritySquares.contains(i)) {
                possibleForthPrioritySquares.add(i);
            }
        }
        if (!possibleFirstPrioritySquares.isEmpty()) {
            return bestMoves(possibleFirstPrioritySquares);
        }
        if (!possibleSecondPrioritySquares.isEmpty()) {
            return bestMoves(possibleSecondPrioritySquares);
        }
        if (!possibleThirdPrioritySquares.isEmpty()) {
            return bestMoves(possibleThirdPrioritySquares);
        }
        if (!possibleForthPrioritySquares.isEmpty()) {
            return bestMoves(possibleForthPrioritySquares);
        }
        return "";
    }

    /**
     * determines best possible moves in a same priority level based on the number
     * of choices it gives opponent & number of discs it provides & finally
     * generates a random move of them if there is no priority
     * 
     * @return randomly one of the best possible moves
     * @param priorityLevel level of priority method works on
     */
    private String bestMoves(ArrayList<String> priorityLevel) {
        if (priorityLevel.size() == 1) {
            return priorityLevel.get(0);
        }
        int[] numberOfChoicesItGivesOpponent = new int[priorityLevel.size()];
        int[] numberOfDiscsItGivesPC = new int[priorityLevel.size()];
        for (int k = 0; k < priorityLevel.size(); k++) {
            char[][] tmpBoard = new char[8][8]; // a temporary board to test effect of each move
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    tmpBoard[i][j] = board[i][j];
                    if (tmpBoard[i][j] == turnCh) {
                        tmpBoard[i][j] = ' ';
                    }
                }
            }
            numberOfChoicesItGivesOpponent[k] = getNChoicesItGivesOpponent(tmpBoard, priorityLevel.get(k));
            tmpBoard = new char[8][8]; // a temporary board to test effect of each move
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    tmpBoard[i][j] = board[i][j];
                    if (tmpBoard[i][j] == turnCh) {
                        tmpBoard[i][j] = ' ';
                    }
                }
            }
            numberOfDiscsItGivesPC[k] = getNDiscsItGivesPC(tmpBoard, priorityLevel.get(k));
        }
        turn = Turn.WHITE.ordinal();
        ArrayList<Integer> properRelation = new ArrayList<>();
        for (int i = 0; i < priorityLevel.size(); i++) {
            properRelation.add(numberOfDiscsItGivesPC[i] - (numberOfChoicesItGivesOpponent[i] * 2));
        }
        int maxValue = properRelation.get(0);
        for (Integer i : properRelation) {
            if (i > maxValue) {
                maxValue = i;
            }
        }
        ArrayList<Integer> bestMovesIndex = new ArrayList<>();
        for (int i = 0; i < properRelation.size(); i++) {
            if (properRelation.get(i) == maxValue) {
                bestMovesIndex.add(i);
            }
        }
        Random rnd = new Random();
        int randomIndex = rnd.nextInt(bestMovesIndex.size());
        return priorityLevel.get(randomIndex);
    }

    /**
     * determines number of choices it gives opponent
     * 
     * @param board       temporary board
     * @param stPlayerInp current iput
     * @return number of choices it gives opponent
     */
    private int getNChoicesItGivesOpponent(char[][] board, String stPlayerInp) {
        turn = Turn.WHITE.ordinal();
        rotateAllInBetween(board, Integer.parseInt(stPlayerInp.charAt(0) + ""),
                Integer.parseInt(stPlayerInp.charAt(1) + ""));
        board[Integer.parseInt(stPlayerInp.charAt(0) + "")][Integer.parseInt(stPlayerInp.charAt(1) + "")] = turns[1];
        turn = Turn.BLACK.ordinal();
        determinePossibleMoves(board);
        int opponentPossibleMovesCounter = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == turnCh) {
                    opponentPossibleMovesCounter++;
                }
            }
        }
        return opponentPossibleMovesCounter;
    }

    /**
     * determines number of discs it provides
     * 
     * @param board       temporary board
     * @param stPlayerInp current iput
     * @return number of discs it provides
     */
    private int getNDiscsItGivesPC(char[][] board, String stPlayerInp) {
        int nWhite = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == whiteCh) {
                    nWhite++;
                }
            }
        }
        turn = Turn.WHITE.ordinal();
        rotateAllInBetween(board, Integer.parseInt(stPlayerInp.charAt(0) + ""),
                Integer.parseInt(stPlayerInp.charAt(1) + ""));
        board[Integer.parseInt(stPlayerInp.charAt(0) + "")][Integer.parseInt(stPlayerInp.charAt(1) + "")] = turns[1];
        int newNWhite = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == whiteCh) {
                    newNWhite++;
                }
            }
        }
        return newNWhite - nWhite - 1;
    }

    public static String toUserFormat(String str) {
        return String.valueOf(new char[] { (char) (str.charAt(0) + 1), ' ', (char) (str.charAt(1) + 17) });
    }
}