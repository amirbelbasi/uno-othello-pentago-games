import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
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
    char[] turns = new char[2];
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

    public Board(String playerOneName, String playerTwoName) {
        playerNames[0] = playerOneName;
        playerNames[1] = playerTwoName;
    }

    /**
     * shows current state of board
     */
    public void showBoard() {
        determinePossibleMoves();
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
    public String toStandardFormat(String str) {
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
     */
    public void determinePossibleMoves() {
        ArrayList<String> tmp = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == blackCh || board[i][j] == whiteCh)
                    continue;
                if (board[i][j] == turnCh) {
                    tmp.add(i + "" + j);
                } else if (isPossibleMove(i, j)) {
                    board[i][j] = turnCh;
                    tmp.add(i + "" + j);
                }
            }
        }
        possibleMoves = tmp;
    }

    /**
     * checks if chosen block is a valid move to be done by player
     * 
     * @param i row index of block
     * @param j column index of block
     * @return true if its a valid move
     */
    public boolean isPossibleMove(int i, int j) {
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
    public boolean isValidBlock(int i, int j) {
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
    public int oppositeTurn(int turn) {
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
    public int playTurn(String playerInp) throws InterruptedException, IOException, InterruptedException {
        Sleep sleep = new Sleep();
        if (!isValidInput(playerInp)) {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            System.out.println("invalid input");
            Thread.sleep(1500);
            return 0;
        }
        String stPlayerInp = toStandardFormat(playerInp);
        if (possibleMoves.isEmpty()) {
            System.out.println("pass");
            Thread.sleep(1500);
            return 1;
        }
        if (possibleMoves.contains(stPlayerInp)) {
            rotateAllInBetween(Integer.parseInt(stPlayerInp.charAt(0) + ""),
                    Integer.parseInt(stPlayerInp.charAt(1) + ""));
            board[Integer.parseInt(stPlayerInp.charAt(0) + "")][Integer
                    .parseInt(stPlayerInp.charAt(1) + "")] = turns[turn];
            turn = oppositeTurn(turn);
            clearTurnChs();
            return 0;
        } else {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            System.out.println("invalid block");
            Thread.sleep(1500);
            return 0;
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
    public void rotateAllInBetween(int i, int j) {
        if (isValidBlock(i - 1, j) && board[i - 1][j] == turns[oppositeTurn(turn)]) {
            for (int k = 2; k < 8; k++) {
                if (isValidBlock(i - k, j) && board[i - k][j] == turns[turn]) {
                    for (int x = 1; x < k; x++) {
                        board[i - x][j] = turns[turn];
                    }
                }
            }
        }
        if (isValidBlock(i + 1, j) && board[i + 1][j] == turns[oppositeTurn(turn)]) {
            for (int k = 2; k < 8; k++) {
                if (isValidBlock(i + k, j) && board[i + k][j] == turns[turn]) {
                    for (int x = 1; x < k; x++) {
                        board[i + x][j] = turns[turn];
                    }
                }
            }
        }
        if (isValidBlock(i, j - 1) && board[i][j - 1] == turns[oppositeTurn(turn)]) {
            for (int k = 2; k < 8; k++) {
                if (isValidBlock(i, j - k) && board[i][j - k] == turns[turn]) {
                    for (int x = 1; x < k; x++) {
                        board[i][j - x] = turns[turn];
                    }
                }
            }
        }
        if (isValidBlock(i, j + 1) && board[i][j + 1] == turns[oppositeTurn(turn)]) {
            for (int k = 2; k < 8; k++) {
                if (isValidBlock(i, j + k) && board[i][j + k] == turns[turn]) {
                    for (int x = 1; x < k; x++) {
                        board[i][j + x] = turns[turn];
                    }
                }
            }
        }
        if (isValidBlock(i - 1, j - 1) && board[i - 1][j - 1] == turns[oppositeTurn(turn)]) {
            for (int k = 2; k < 8; k++) {
                if (isValidBlock(i - k, j - k) && board[i - k][j - k] == turns[turn]) {
                    for (int x = 1; x < k; x++) {
                        board[i - x][j - x] = turns[turn];
                    }
                }
            }
        }
        if (isValidBlock(i + 1, j + 1) && board[i + 1][j + 1] == turns[oppositeTurn(turn)]) {
            for (int k = 2; k < 8; k++) {
                if (isValidBlock(i + k, j + k) && board[i + k][j + k] == turns[turn]) {
                    for (int x = 1; x < k; x++) {
                        board[i + x][j + x] = turns[turn];
                    }
                }
            }
        }
        if (isValidBlock(i + 1, j - 1) && board[i + 1][j - 1] == turns[oppositeTurn(turn)]) {
            for (int k = 2; k < 8; k++) {
                if (isValidBlock(i + k, j - k) && board[i + k][j - k] == turns[turn]) {
                    for (int x = 1; x < k; x++) {
                        board[i + x][j - x] = turns[turn];
                    }
                }
            }
        }
        if (isValidBlock(i - 1, j + 1) && board[i - 1][j + 1] == turns[oppositeTurn(turn)]) {
            for (int k = 2; k < 8; k++) {
                if (isValidBlock(i - k, j + k) && board[i - k][j + k] == turns[turn]) {
                    for (int x = 1; x < k; x++) {
                        board[i - x][j + x] = turns[turn];
                    }
                }
            }
        }
    }
}