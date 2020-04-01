import java.util.ArrayList;

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
    private char turnCh = '\u25CC';
    private int turn = Turn.BLACK.ordinal();
    private ArrayList<String> blackPossibleMoves = new ArrayList<>();
    private ArrayList<String> whitePossibleMoves = new ArrayList<>();
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

    /**
     * shows current state of board
     */
    public void showBoard() {
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
     * converts input of player to a standard format e.g. "A 1" to "00"
     * 
     * @param str player input like "B 4"
     * @return formated stirng
     */
    public String toStandardFormat(String str) {
        return String.valueOf(new char[] { (char) (str.charAt(0) - 17), (char) (str.charAt(2) - 1) });
    }

    /**
     * checks if
     * 
     * @param str player input like "B 4"
     * @return true if input is valid
     */
    public boolean isValidInput(String str) {
        if (65 <= (int) str.charAt(0) && (int) str.charAt(0) <= 72 && 49 <= (int) str.charAt(2)
                && (int) str.charAt(2) <= 56) {
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
                if (board[i][j] != ' ')
                    continue;
                if (isPossibleMove(i, j)) {
                    board[i][j] = turnCh;
                    tmp.add(i + "" + j);
                }
            }
        }
        if (turn == Turn.BLACK.ordinal()) {
            blackPossibleMoves = tmp;
        } else {
            whitePossibleMoves = tmp;
        }
    }

    /**
     * checks if chosen block is a valid move to be done by player
     * 
     * @param i row index of block
     * @param j column index of block
     * @return true if its a valid move
     */
    public boolean isPossibleMove(int i, int j) {
        char[] tmp = new char[2];
        tmp[0] = blackCh;
        tmp[1] = whiteCh;
        if (isValidBlock(i - 1, j) && board[i - 1][j] == tmp[oppositeTurn(turn)]) {
            for (int k = 2; k < 8; k++) {
                if (isValidBlock(i - k, j) && board[i - k][j] == tmp[turn]) {
                    return true;
                }
            }
        }
        if (isValidBlock(i + 1, j) && board[i + 1][j] == tmp[oppositeTurn(turn)]) {
            for (int k = 2; k < 8; k++) {
                if (isValidBlock(i + k, j) && board[i + k][j] == tmp[turn]) {
                    return true;
                }
            }
        }
        if (isValidBlock(i, j - 1) && board[i][j - 1] == tmp[oppositeTurn(turn)]) {
            for (int k = 2; k < 8; k++) {
                if (isValidBlock(i, j - k) && board[i][j - k] == tmp[turn]) {
                    return true;
                }
            }
        }
        if (isValidBlock(i, j + 1) && board[i][j + 1] == tmp[oppositeTurn(turn)]) {
            for (int k = 2; k < 8; k++) {
                if (isValidBlock(i, j + k) && board[i][j + k] == tmp[turn]) {
                    return true;
                }
            }
        }
        if (isValidBlock(i - 1, j - 1) && board[i - 1][j - 1] == tmp[oppositeTurn(turn)]) {
            for (int k = 2; k < 8; k++) {
                if (isValidBlock(i - k, j - k) && board[i - k][j - k] == tmp[turn]) {
                    return true;
                }
            }
        }
        if (isValidBlock(i + 1, j + 1) && board[i + 1][j + 1] == tmp[oppositeTurn(turn)]) {
            for (int k = 2; k < 8; k++) {
                if (isValidBlock(i + k, j + k) && board[i + k][j + k] == tmp[turn]) {
                    return true;
                }
            }
        }
        if (isValidBlock(i + 1, j - 1) && board[i + 1][j - 1] == tmp[oppositeTurn(turn)]) {
            for (int k = 2; k < 8; k++) {
                if (isValidBlock(i + k, j - k) && board[i + k][j - k] == tmp[turn]) {
                    return true;
                }
            }
        }
        if (isValidBlock(i - 1, j + 1) && board[i - 1][j + 1] == tmp[oppositeTurn(turn)]) {
            for (int k = 2; k < 8; k++) {
                if (isValidBlock(i - k, j + k) && board[i - k][j + k] == tmp[turn]) {
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
}