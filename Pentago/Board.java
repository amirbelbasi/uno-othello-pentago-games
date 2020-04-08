import java.util.Random;
import java.util.Scanner;
import java.io.IOException;

/**
 * provides more readability (hopefully!)
 */
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
    private char emptyCh = '\u25A1';
    private static char[][] board = new char[6][6];
    {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                board[i][j] = emptyCh;
            }
        }
    }
    private int turn = Turn.BLACK.ordinal();
    private boolean hasPutDisc = false; // determines if player has puted his disc
    private boolean isBlackWinner = false;
    private boolean isWhiteWinner = false;
    private String player1, player2;
    private String[] playerNames = new String[2];

    /**
     * constructor
     * 
     * @param blackPlayer name of black player
     * @param whitePlayer name of white player
     */
    public Board(String player1, String player2) {
        this.player1 = player1;
        this.player2 = player2;
        Random rnd = new Random();
        int randIndex = rnd.nextInt(2);
        playerNames[randIndex] = player1;
        playerNames[randIndex == 0 ? 1 : 0] = player2;
    }

    /**
     * shows board
     * 
     * @throws IOException
     */
    private void showBoard() throws InterruptedException, IOException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        for (int i = 0; i < 6; i++) {
            if (i == 3) {
                System.out.println("-------------");
            }
            for (int j = 0; j < 6; j++) {
                if (j == 3) {
                    System.out.print("| ");
                }
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * rotates a block clock wise or counter clock wise
     * 
     * @param indexOfBlock 1, 2, 3 or 4
     * @param direction    + or -
     */
    private void rotate(int indexOfBlock, String direction) {
        char[][] holderBoard = new char[6][6];
        int jShift = 0;
        int iShift = 0;
        if (indexOfBlock > 2) {
            iShift = 3;
        }
        if (indexOfBlock % 2 == 0) {
            jShift = 3;
        }
        if (direction.equals("+")) // clock wise
        {
            for (int a = 0; a < 3; a++) {
                for (int b = 0; b < 3; b++) {
                    holderBoard[a + iShift][b + jShift] = board[2 - b + iShift][a + jShift];
                }
            }
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 6; j++) {
                    if (holderBoard[i][j] == '\0') {
                        holderBoard[i][j] = board[i][j];
                    }
                }
            }
        } else // counter clock wise
        {
            for (int a = 0; a < 3; a++) {
                for (int b = 0; b < 3; b++) {
                    holderBoard[2 - b + iShift][a + jShift] = board[a + iShift][b + jShift];
                }
            }
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 6; j++) {
                    if (holderBoard[i][j] == '\0') {
                        holderBoard[i][j] = board[i][j];
                    }
                }
            }
        }
        board = holderBoard;
    }

    /**
     * player playing his turn
     * 
     * @throws InterruptedException
     * @throws IOException
     */
    public int playTurn() throws InterruptedException, IOException {
        Scanner sc = new Scanner(System.in);
        if (isBoardFull()) {
            determineWinnner();
            return -1;
        }
        if (!hasPutDisc) {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            showBoard();
            System.out.println(playerNames[turn] + "'s turn ");
            System.out.print("please enter coordinates of cell you wish to put your disc on: ");
            String cellInp = sc.nextLine();
            if (!isCoordinatesOfCellValid(cellInp)) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                System.out.println("invalid input");
                Thread.sleep(1500);
                return 0;
            }
            if (!isCellEmpty(cellInp)) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                System.out.println("invalid cell");
                Thread.sleep(1500);
                return 0;
            }
            board[Integer.parseInt(cellInp.charAt(0) + "")][Integer
                    .parseInt(cellInp.charAt(2) + "")] = (turn == Turn.BLACK.ordinal() ? blackCh : whiteCh);
            hasPutDisc = true;
            if (isThereAWinner()) {
                determineWinnner();
                return -1;
            }
        }
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        showBoard();
        System.out.println(playerNames[turn] + "'s turn ");
        System.out.print("please enter details of block you wish to rotate: ");
        String blockInp = sc.nextLine();
        if (!isDetailsOfBlockValid(blockInp)) {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            System.out.println("invalid input");
            Thread.sleep(1500);
            return 0;
        }
        rotate(Integer.parseInt(blockInp.charAt(0) + ""), blockInp.charAt(2) + "");
        turn = switchTurn(turn);
        hasPutDisc = false;
        if (isThereAWinner()) {
            determineWinnner();
            return -1;
        }
        return 0;
    }

    /**
     * returns opposite turn
     * 
     * @param currentTurn
     * @return
     */
    private int switchTurn(int currentTurn) {
        return currentTurn == Turn.BLACK.ordinal() ? Turn.WHITE.ordinal() : Turn.BLACK.ordinal();
    }

    /**
     * checks if input is valid
     * 
     * @param inp
     * @return true if input is valid
     */
    private boolean isCoordinatesOfCellValid(String inp) {
        if (inp.length() != 3) {
            return false;
        }
        if ((int) inp.charAt(0) - 48 >= 0 && (int) inp.charAt(0) - 48 <= 5 && (int) inp.charAt(2) - 48 >= 0
                && (int) inp.charAt(2) - 48 <= 5) {
            return true;
        }
        return false;
    }

    /**
     * checks if block is empty
     * 
     * @param inp
     * @return ture if cell is empty
     */
    private boolean isCellEmpty(String inp) {
        if (board[Integer.parseInt(inp.charAt(0) + "")][Integer.parseInt(inp.charAt(2) + "")] != emptyCh) {
            return false;
        }
        return true;
    }

    /**
     * checks if given block detail is valid
     * 
     * @param inp
     * @return
     */
    private boolean isDetailsOfBlockValid(String inp) {
        if (inp.length() != 3) {
            return false;
        }
        if ((int) inp.charAt(0) - 48 >= 1 && (int) inp.charAt(0) - 48 <= 4
                && (inp.charAt(2) == '+' || inp.charAt(2) == '-')) {
            return true;
        }
        return false;
    }

    /**
     * checks if board is full
     * 
     * @return true if board is full
     */
    private boolean isBoardFull() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (board[i][j] == emptyCh) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * checks if there is a winner
     * 
     * @return true if there is a winner
     */
    private boolean isThereAWinner() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (board[i][j] == emptyCh) {
                    continue;
                }
                int counter = 1;
                for (int k = 1; k < 6; k++) {
                    if (isCoordinatesOfCellValid((i + k) + " " + j) && board[i + k][j] == board[i][j]) {
                        counter++;
                    }
                    if (isCoordinatesOfCellValid((i - k) + " " + j) && board[i - k][j] == board[i][j]) {
                        counter++;
                    }
                }
                if (counter == 5) {
                    if (board[i][j] == blackCh) {
                        isBlackWinner = true;
                    } else {
                        isWhiteWinner = true;
                    }
                }
                counter = 1;
                for (int k = 1; k < 6; k++) {
                    if (isCoordinatesOfCellValid(i + " " + (j + k)) && board[i][j + k] == board[i][j]) {
                        counter++;
                    }
                    if (isCoordinatesOfCellValid(i + " " + (j - k)) && board[i][j - k] == board[i][j]) {
                        counter++;
                    }
                }
                if (counter == 5) {
                    if (board[i][j] == blackCh) {
                        isBlackWinner = true;
                    } else {
                        isWhiteWinner = true;
                    }
                }
                counter = 1;
                for (int k = 1; k < 6; k++) {
                    if (isCoordinatesOfCellValid((i + k) + " " + (j + k)) && board[i + k][j + k] == board[i][j]) {
                        counter++;
                    }
                    if (isCoordinatesOfCellValid((i - k) + " " + (j - k)) && board[i - k][j - k] == board[i][j]) {
                        counter++;
                    }
                }
                if (counter == 5) {
                    if (board[i][j] == blackCh) {
                        isBlackWinner = true;
                    } else {
                        isWhiteWinner = true;
                    }
                }
                counter = 1;
                for (int k = 1; k < 6; k++) {
                    if (isCoordinatesOfCellValid((i + k) + " " + (j - k)) && board[i + k][j - k] == board[i][j]) {
                        counter++;
                    }
                    if (isCoordinatesOfCellValid((i - k) + " " + (j + k)) && board[i - k][j + k] == board[i][j]) {
                        counter++;
                    }
                }
                if (counter == 5) {
                    if (board[i][j] == blackCh) {
                        isBlackWinner = true;
                    } else {
                        isWhiteWinner = true;
                    }
                }
            }
        }
        return isBlackWinner || isWhiteWinner ? true : false;
    }

    /**
     * determines the winner
     * 
     * @throws IOException
     * @throws InterruptedException
     */
    private void determineWinnner() throws InterruptedException, IOException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        showBoard();
        if ((isBlackWinner && isWhiteWinner) || (!isBlackWinner && !isWhiteWinner)) {
            System.out.print("Tie!");
            return;
        }
        if (isBlackWinner) {
            System.out.println("The winner is: " + playerNames[0]);
            return;
        }
        if (isWhiteWinner) {
            System.out.println("The winner is: " + playerNames[1]);
            return;
        }
    }
}