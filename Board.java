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
    private char white = '\u25CF';
    private char black = '\u25CB';
    private char turn = '\u25CC';
    private char[][] board = new char[8][8];
    {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = ' ';
            }
        }
        board[3][3] = white;
        board[3][4] = black;
        board[4][3] = black;
        board[4][4] = white;
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
}