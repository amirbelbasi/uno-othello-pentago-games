import java.util.Random;
import java.util.Scanner;
import jdk.nashorn.internal.ir.ReturnNode;
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
     * two player constructor
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
     * single player constructor
     * 
     * @param player
     */
    public Board(String player2) {
        this.player2 = player2;
        playerNames[0] = "Mr.PC";
        playerNames[1] = player2;
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
    private void rotate(int indexOfBlock, String direction, char[][] board) {
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
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                board[i][j] = holderBoard[i][j];
            }
        }
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
        rotate(Integer.parseInt(blockInp.charAt(0) + ""), blockInp.charAt(2) + "", board);
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
                    if (!isCoordinatesOfCellValid((i + k) + " " + j)) {
                        continue;
                    }
                    if (board[i + k][j] == board[i][j]) {
                        counter++;
                    } else {
                        break;
                    }
                }
                for (int k = 1; k < 6; k++) {
                    if (!isCoordinatesOfCellValid((i - k) + " " + j)) {
                        continue;
                    }
                    if (board[i - k][j] == board[i][j]) {
                        counter++;
                    } else {
                        break;
                    }
                }
                if (counter >= 5) {
                    if (board[i][j] == blackCh) {
                        isBlackWinner = true;
                    } else {
                        isWhiteWinner = true;
                    }
                }
                counter = 1;
                for (int k = 1; k < 6; k++) {
                    if (!isCoordinatesOfCellValid(i + " " + (j + k))) {
                        continue;
                    }
                    if (board[i][j + k] == board[i][j]) {
                        counter++;
                    } else {
                        break;
                    }
                }
                for (int k = 1; k < 6; k++) {
                    if (!isCoordinatesOfCellValid(i + " " + (j - k))) {
                        continue;
                    }
                    if (board[i][j - k] == board[i][j]) {
                        counter++;
                    } else {
                        break;
                    }
                }
                if (counter >= 5) {
                    if (board[i][j] == blackCh) {
                        isBlackWinner = true;
                    } else {
                        isWhiteWinner = true;
                    }
                }
                counter = 1;
                for (int k = 1; k < 6; k++) {
                    if (!isCoordinatesOfCellValid((i + k) + " " + (j + k))) {
                        continue;
                    }
                    if (board[i + k][j + k] == board[i][j]) {
                        counter++;
                    } else {
                        break;
                    }
                }
                for (int k = 1; k < 6; k++) {
                    if (!isCoordinatesOfCellValid((i - k) + " " + (j - k))) {
                        continue;
                    }
                    if (board[i - k][j - k] == board[i][j]) {
                        counter++;
                    } else {
                        break;
                    }
                }
                if (counter >= 5) {
                    if (board[i][j] == blackCh) {
                        isBlackWinner = true;
                    } else {
                        isWhiteWinner = true;
                    }
                }
                counter = 1;
                for (int k = 1; k < 6; k++) {
                    if (!isCoordinatesOfCellValid((i + k) + " " + (j - k))) {
                        continue;
                    }
                    if (board[i + k][j - k] == board[i][j]) {
                        counter++;
                    } else {
                        break;
                    }
                }
                for (int k = 1; k < 6; k++) {
                    if (!isCoordinatesOfCellValid((i - k) + " " + (j + k))) {
                        continue;
                    }
                    if (board[i - k][j + k] == board[i][j]) {
                        counter++;
                    } else {
                        break;
                    }
                }
                if (counter >= 5) {
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
     * checks if black is winner
     * 
     * @return
     */
    private boolean ismyCharAWinner(char[][] board, char myChar) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (board[i][j] == emptyCh) {
                    continue;
                }
                int counter = 1;
                for (int k = 1; k < 6; k++) {
                    if (!isCoordinatesOfCellValid((i + k) + " " + j)) {
                        continue;
                    }
                    if (board[i + k][j] == board[i][j]) {
                        counter++;
                    } else {
                        break;
                    }
                }
                for (int k = 1; k < 6; k++) {
                    if (!isCoordinatesOfCellValid((i - k) + " " + j)) {
                        continue;
                    }
                    if (board[i - k][j] == board[i][j]) {
                        counter++;
                    } else {
                        break;
                    }
                }
                if (counter >= 5) {
                    return true;
                }
                counter = 1;
                for (int k = 1; k < 6; k++) {
                    if (!isCoordinatesOfCellValid(i + " " + (j + k))) {
                        continue;
                    }
                    if (board[i][j + k] == board[i][j]) {
                        counter++;
                    } else {
                        break;
                    }
                }
                for (int k = 1; k < 6; k++) {
                    if (!isCoordinatesOfCellValid(i + " " + (j - k))) {
                        continue;
                    }
                    if (board[i][j - k] == board[i][j]) {
                        counter++;
                    } else {
                        break;
                    }
                }
                if (counter >= 5) {
                    return true;
                }
                counter = 1;
                for (int k = 1; k < 6; k++) {
                    if (!isCoordinatesOfCellValid((i + k) + " " + (j + k))) {
                        continue;
                    }
                    if (board[i + k][j + k] == board[i][j]) {
                        counter++;
                    } else {
                        break;
                    }
                }
                for (int k = 1; k < 6; k++) {
                    if (!isCoordinatesOfCellValid((i - k) + " " + (j - k))) {
                        continue;
                    }
                    if (board[i - k][j - k] == board[i][j]) {
                        counter++;
                    } else {
                        break;
                    }
                }
                if (counter >= 5) {
                    return true;
                }
                counter = 1;
                for (int k = 1; k < 6; k++) {
                    if (!isCoordinatesOfCellValid((i + k) + " " + (j - k))) {
                        continue;
                    }
                    if (board[i + k][j - k] == board[i][j]) {
                        counter++;
                    } else {
                        break;
                    }
                }
                for (int k = 1; k < 6; k++) {
                    if (!isCoordinatesOfCellValid((i - k) + " " + (j + k))) {
                        continue;
                    }
                    if (board[i - k][j + k] == board[i][j]) {
                        counter++;
                    } else {
                        break;
                    }
                }
                if (counter >= 5) {
                    return true;
                }
            }
        }
        return false;
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

    /**
     * a turn played by PC
     * 
     * @return
     * @throws InterruptedException
     * @throws IOException
     */
    public int PCPlayTurn() throws InterruptedException, IOException {
        Scanner sc = new Scanner(System.in);
        if (isBoardFull()) {
            determineWinnner();
            return -1;
        }
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        showBoard();
        System.out.println(playerNames[turn] + "'s turn ");
        System.out.print("please enter coordinates of cell you wish to put your disc on: ");
        String cellInp = getBestPut();
        System.out.println(cellInp);
        Thread.sleep(2500);
        board[Integer.parseInt(cellInp.charAt(0) + "")][Integer
                .parseInt(cellInp.charAt(2) + "")] = (turn == Turn.BLACK.ordinal() ? blackCh : whiteCh);
        if (isThereAWinner()) {
            determineWinnner();
            return -1;
        }
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        showBoard();
        System.out.println(playerNames[turn] + "'s turn ");
        System.out.print("please enter details of block you wish to rotate: ");
        String blockInp = getBestRotate();
        System.out.println(blockInp);
        Thread.sleep(2500);
        rotate(Integer.parseInt(blockInp.charAt(0) + ""), blockInp.charAt(2) + "", board);
        turn = switchTurn(turn);
        hasPutDisc = false;
        if (isThereAWinner()) {
            determineWinnner();
            return -1;
        }
        return 0;
    }

    /**
     * @return the turn
     */
    public int getTurn() {
        return turn;
    }

    /**
     * looks for win rotate
     * 
     * @return
     */
    private String winRotate() {
        char[][] tmpBoard = new char[6][6];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                tmpBoard[i][j] = board[i][j];
            }
        }
        for (int k = 1; k <= 4; k++) {
            rotate(k, "+", tmpBoard);
            if (ismyCharAWinner(tmpBoard, blackCh)) {
                return k + " +";
            }
            rotate(k, "-", tmpBoard);
            rotate(k, "-", tmpBoard);
            if (ismyCharAWinner(tmpBoard, blackCh)) {
                return k + " -";
            }
            rotate(k, "+", tmpBoard);
        }
        return "";
    }

    /**
     * looks for win put considering rotating
     * 
     * @param myChar
     * @return
     */
    private String winMove(char myChar) {
        char[][] tmpBoard = new char[6][6];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                tmpBoard[i][j] = board[i][j];
            }
        }
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (tmpBoard[i][j] != emptyCh) {
                    continue;
                }
                tmpBoard[i][j] = myChar;
                for (int k = 1; k <= 4; k++) {
                    rotate(k, "+", tmpBoard);
                    if (ismyCharAWinner(tmpBoard, myChar)) {
                        return i + " " + j;
                    }
                    rotate(k, "-", tmpBoard);
                    rotate(k, "-", tmpBoard);
                    if (ismyCharAWinner(tmpBoard, myChar)) {
                        return i + " " + j;
                    }
                    rotate(k, "+", tmpBoard);
                }
                tmpBoard[i][j] = emptyCh;
            }
        }
        return "";
    }

    private int threeCenterSequence = 0;

    /**
     * generates best move possible
     * 
     * @return
     */
    private String getBestPut() {
        if (winMove(blackCh) != "") {
            return winMove(blackCh);
        }
        if (winMove(whiteCh) != "") {
            return winMove(whiteCh);
        }
        if (board[1][1] == blackCh && board[1][4] == blackCh && board[1][0] == emptyCh && board[1][5] == emptyCh) {
            if (board[1][2] == blackCh && board[1][3] == emptyCh) {
                return "1 3";
            }
            if (board[1][2] == emptyCh && board[1][3] == blackCh) {
                return "1 2";
            }
        }
        if (board[1][1] == blackCh && board[4][1] == blackCh && board[0][1] == emptyCh && board[5][1] == emptyCh) {
            if (board[2][1] == blackCh && board[3][1] == emptyCh) {
                return "3 1";
            }
            if (board[2][1] == emptyCh && board[3][1] == blackCh) {
                return "2 1";
            }
        }
        if (board[1][1] == emptyCh) {
            return "1 1";
        }
        if (board[1][4] != emptyCh || board[4][1] != emptyCh) {
            if(board[1][4] == emptyCh)
            {
                return "1 4";
            }
            if (board[4][1] == emptyCh)
            {
                return "4 1";
            }
        }
        if (lookForThreeSideSequence(whiteCh) != "") {
            return lookForThreeSideSequence(whiteCh);
        }
        if (threeCenterSequence == 1 || threeCenterSequence == 4) {
            if (board[1][4] == emptyCh) {
                return "1 4";
            }
            if (board[4][1] == emptyCh) {
                return "4 1";
            }
        }
        if (threeCenterSequence == 2 || threeCenterSequence == 3) {
            if (board[1][1] == emptyCh) {
                return "1 1";
            }
            if (board[4][4] == emptyCh) {
                return "4 4";
            }
        }
        if (lookForThreeCenterSequence(blackCh) != "") {
            if (Integer.parseInt(lookForThreeCenterSequence(blackCh).charAt(0) + "") <= 2
                    && Integer.parseInt(lookForThreeCenterSequence(blackCh).charAt(2) + "") <= 2) {
                threeCenterSequence = 1;
            }
            if (Integer.parseInt(lookForThreeCenterSequence(blackCh).charAt(0) + "") <= 2
                    && Integer.parseInt(lookForThreeCenterSequence(blackCh).charAt(2) + "") >= 3) {
                threeCenterSequence = 2;
            }
            if (Integer.parseInt(lookForThreeCenterSequence(blackCh).charAt(0) + "") >= 3
                    && Integer.parseInt(lookForThreeCenterSequence(blackCh).charAt(2) + "") <= 2) {
                threeCenterSequence = 3;
            }
            if (Integer.parseInt(lookForThreeCenterSequence(blackCh).charAt(0) + "") >= 3
                    && Integer.parseInt(lookForThreeCenterSequence(blackCh).charAt(2) + "") >= 3) {
                threeCenterSequence = 4;
            }
            return lookForThreeCenterSequence(blackCh);
        }
        if (lookForThreeSideSequence(blackCh) != "") {
            return lookForThreeSideSequence(blackCh);
        }
        if (prioritiesPut() != "") {
            return prioritiesPut();
        }
        if (getCorner() != "") {
            return getCorner();
        }
        if (getDouble() != "") {
            return getDouble();
        }
        if (getDiagonal() != "") {
            return getDiagonal();
        }
        if (board[1][4] == emptyCh) {
            return "1 4";
        }
        if (board[4][1] == emptyCh) {
            return "4 1";
        }
        if (board[4][4] == emptyCh) {
            return "4 4";
        }
        if (board[1][4] == blackCh) {
            if (board[1][3] == emptyCh) {
                return "1 3";
            }
            if (board[1][5] == emptyCh) {
                return "1 5";
            }
            if (board[0][4] == emptyCh) {
                return "0 4";
            }
            if (board[2][4] == emptyCh) {
                return "2 4";
            }
            if (board[3][1] == emptyCh) {
                return "3 1";
            }
            if (board[4][0] == emptyCh) {
                return "4 0";
            }
            if (board[4][2] == emptyCh) {
                return "4 2";
            }
            if (board[5][1] == emptyCh) {
                return "5 1";
            }
        }
        if (board[4][1] == blackCh) {
            if (board[3][1] == emptyCh) {
                return "3 1";
            }
            if (board[4][0] == emptyCh) {
                return "4 0";
            }
            if (board[4][2] == emptyCh) {
                return "4 2";
            }
            if (board[5][1] == emptyCh) {
                return "5 1";
            }
            if (board[1][3] == emptyCh) {
                return "1 3";
            }
            if (board[1][5] == emptyCh) {
                return "1 5";
            }
            if (board[0][4] == emptyCh) {
                return "0 4";
            }
            if (board[2][4] == emptyCh) {
                return "2 4";
            }
        }
        if (board[2][2] == emptyCh) {
            return "2 2";
        }
        if (board[3][3] == emptyCh) {
            return "3 3";
        }
        if (board[2][0] == emptyCh) {
            return "2 0";
        }
        if (board[0][2] == emptyCh) {
            return "0 2";
        }
        if (board[3][5] == emptyCh) {
            return "3 5";
        }
        if (board[5][3] == emptyCh) {
            return "5 3";
        }
        if (board[0][0] == emptyCh) {
            return "0 0";
        }
        if (board[5][5] == emptyCh) {
            return "5 5";
        }
        if (board[0][5] == emptyCh) {
            return "0 5";
        }
        if (board[5][0] == emptyCh) {
            return "5 0";
        }
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (board[i][j] == emptyCh) {
                    return i + " " + j;
                }
            }
        }
        return "";
    }

    /**
     * generates best rotate possible
     * 
     * @return
     */
    private String getBestRotate() {
        if (winRotate() != "") {
            return winRotate();
        }
        if (board[5][0] == blackCh && board[3][0] == blackCh && board[3][2] == blackCh) {
            return "3 +";
        }
        if (board[5][0] == blackCh && board[5][2] == blackCh && board[3][2] == blackCh) {
            return "3 -";
        }
        if (board[0][5] == blackCh && board[0][3] == blackCh && board[2][3] == blackCh) {
            return "2 -";
        }
        if (board[0][5] == blackCh && board[2][5] == blackCh && board[2][3] == blackCh) {
            return "2 +";
        }
        if (board[3][2] != blackCh) {
            if (board[3][0] == blackCh) {
                return "3 +";
            }
            if (board[5][2] == blackCh) {
                return "3 -";
            }
            if (board[5][0] == blackCh) {
                return "3 +";
            }
        }
        if (board[2][3] != blackCh) {
            if (board[0][3] == blackCh) {
                return "2 -";
            }
            if (board[2][5] == blackCh) {
                return "2 +";
            }
            if (board[0][5] == blackCh) {
                return "2 +";
            }
        }
        if (goodRotate() != "") {
            return goodRotate();
        }
        return "4 +"; // default rotate
    }

    /**
     * looks for centeral 3 black sequences
     * 
     * @return
     */
    private String lookForThreeCenterSequence(char myChar) {
        int i, j;
        for (i = 0; i < 6; i++) {
            if (i % 3 != 1) // 1 4
            {
                continue;
            }
            int counter = 0;
            for (j = 0; j < 3; j++) {
                if (board[i][j] == myChar) {
                    counter++;
                }
            }
            if (counter == 2) {
                for (int k = 0; k < 3; k++) {
                    if (board[i][k] == emptyCh) {
                        return i + " " + k;
                    }
                }
            }
            counter = 0;
            for (j = 3; j < 6; j++) {
                if (board[i][j] == myChar) {
                    counter++;
                }
            }
            if (counter == 2) {
                for (int k = 3; k < 6; k++) {
                    if (board[i][k] == emptyCh) {
                        return i + " " + k;
                    }
                }
            }
        }

        for (j = 0; j < 6; j++) {
            if (j % 3 != 1) // 1 4
            {
                continue;
            }
            int counter = 0;
            for (i = 0; i < 3; i++) {
                if (board[i][j] == myChar) {
                    counter++;
                }
            }
            if (counter == 2) {
                for (int k = 0; k < 3; k++) {
                    if (board[k][j] == emptyCh) {
                        return k + " " + j;
                    }
                }
            }
            counter = 0;
            for (i = 3; i < 6; i++) {
                if (board[i][j] == myChar) {
                    counter++;
                }
            }
            if (counter == 2) {
                for (int k = 3; k < 6; k++) {
                    if (board[k][j] == emptyCh) {
                        return k + " " + j;
                    }
                }
            }
        }
        return "";
    }

    /**
     * looks for Three Side Sequences
     * 
     * @return
     */
    private String lookForThreeSideSequence(char myChar) {
        int i, j;
        for (i = 0; i < 6; i++) {
            if ((i % 3) % 2 != 0) // 0 2 3 5
            {
                continue;
            }
            int counter = 0;
            for (j = 0; j < 3; j++) {
                if (board[i][j] == myChar) {
                    counter++;
                }
            }
            if (counter == 2) {
                for (int k = 0; k < 3; k++) {
                    if (board[i][k] == emptyCh) {
                        return i + " " + k;
                    }
                }
            }
            counter = 0;
            for (j = 3; j < 6; j++) {
                if (board[i][j] == myChar) {
                    counter++;
                }
            }
            if (counter == 2) {
                for (int k = 3; k < 6; k++) {
                    if (board[i][k] == emptyCh) {
                        return i + " " + k;
                    }
                }
            }
        }

        for (j = 0; j < 6; j++) {
            if ((j % 3) % 2 != 0) // 0 2 3 5
            {
                continue;
            }
            int counter = 0;
            for (i = 0; i < 3; i++) {
                if (board[i][j] == myChar) {
                    counter++;
                }
            }
            if (counter == 2) {
                for (int k = 0; k < 3; k++) {
                    if (board[k][j] == emptyCh) {
                        return k + " " + j;
                    }
                }
            }
            counter = 0;
            for (i = 3; i < 6; i++) {
                if (board[i][j] == myChar) {
                    counter++;
                }
            }
            if (counter == 2) {
                for (int k = 3; k < 6; k++) {
                    if (board[k][j] == emptyCh) {
                        return k + " " + j;
                    }
                }
            }
        }
        return "";
    }

    /**
     * its a priority based set of moves
     * 
     * @return
     */
    private String prioritiesPut() {
        if (board[3][4] == whiteCh && board[5][4] == emptyCh) {
            return "5 4";
        }
        if (board[4][3] == whiteCh && board[4][5] == emptyCh) {
            return "4 5";
        }
        if (board[5][4] == whiteCh && board[3][4] == emptyCh) {
            return "3 4";
        }
        if (board[4][5] == whiteCh && board[4][3] == emptyCh) {
            return "4 3";
        }

        if (board[0][1] == whiteCh && board[2][1] == emptyCh) {
            return "2 1";
        }
        if (board[2][1] == whiteCh && board[0][1] == emptyCh) {
            return "0 1";
        }
        if (board[1][0] == whiteCh && board[1][2] == emptyCh) {
            return "1 2";
        }
        if (board[1][2] == whiteCh && board[1][0] == emptyCh) {
            return "1 0";
        }

        if (board[0][4] == whiteCh && board[2][4] == emptyCh) {
            return "2 4";
        }
        if (board[2][4] == whiteCh && board[0][4] == emptyCh) {
            return "0 4";
        }
        if (board[1][3] == whiteCh && board[1][5] == emptyCh) {
            return "1 5";
        }
        if (board[1][5] == whiteCh && board[1][3] == emptyCh) {
            return "1 3";
        }

        if (board[3][1] == whiteCh && board[5][1] == emptyCh) {
            return "5 1";
        }
        if (board[5][1] == whiteCh && board[3][1] == emptyCh) {
            return "3 1";
        }
        if (board[4][0] == whiteCh && board[4][2] == emptyCh) {
            return "4 2";
        }
        if (board[4][2] == whiteCh && board[4][0] == emptyCh) {
            return "4 0";
        }

        if (board[3][0] != blackCh && board[3][2] != blackCh && board[5][0] != blackCh && board[5][2] != blackCh) {
            if (board[3][2] == emptyCh) {
                return "3 2";
            }
            if (board[3][0] == emptyCh) {
                return "3 0";
            }
            if (board[5][2] == emptyCh) {
                return "5 2";
            }
            if (board[5][0] == emptyCh) {
                return "5 0";
            }
        }
        if (board[0][3] != blackCh && board[2][3] != blackCh && board[0][5] != blackCh && board[2][5] != blackCh) {
            if (board[2][3] == emptyCh) {
                return "2 3";
            }
            if (board[0][3] == emptyCh) {
                return "0 3";
            }
            if (board[2][5] == emptyCh) {
                return "2 5";
            }
            if (board[0][5] == emptyCh) {
                return "0 5";
            }
        }
        if (board[3][4] == emptyCh) {
            return "3 4";
        }
        if (board[4][5] == emptyCh) {
            return "4 5";
        }
        if (board[4][3] == emptyCh) {
            return "4 3";
        }
        if (board[5][4] == emptyCh) {
            return "5 4";
        }
        if (board[0][1] == emptyCh) {
            return "0 1";
        }
        if (board[1][2] == emptyCh) {
            return "1 2";
        }
        if (board[1][0] == emptyCh) {
            return "1 0";
        }
        if (board[2][1] == emptyCh) {
            return "2 1";
        }

        if (board[3][2] == emptyCh) {
            return "3 2";
        }
        if (board[2][3] == emptyCh) {
            return "2 3";
        }
        if (board[5][2] == emptyCh) {
            return "5 2";
        }
        if (board[3][0] == emptyCh) {
            return "3 0";
        }
        if (board[0][3] == emptyCh) {
            return "0 3";
        }
        if (board[2][5] == emptyCh) {
            return "2 5";
        }
        return "";
    }

    /**
     * looks for corners
     * 
     * @return
     */
    private String getCorner() {
        for (int k = 1; k <= 4; k++) {
            int iShift = 0, jShift = 0;
            if (k == 2) {
                jShift = 3;
            }
            if (k == 3) {
                iShift = 3;
            }
            if (k == 4) {
                iShift = 3;
                jShift = 3;
            }
            if (board[0 + iShift][0 + jShift] == emptyCh && board[0 + iShift][1 + jShift] == blackCh
                    && board[1 + iShift][0 + jShift] == blackCh && board[0 + iShift][2 + jShift] == emptyCh
                    && board[2 + iShift][0 + jShift] == emptyCh) {
                return (0 + iShift) + " " + (0 + jShift);
            }
            if (board[0 + iShift][2 + jShift] == emptyCh && board[0 + iShift][1 + jShift] == blackCh
                    && board[1 + iShift][2 + jShift] == blackCh && board[0 + iShift][0 + jShift] == emptyCh
                    && board[2 + iShift][2 + jShift] == emptyCh) {
                return (0 + iShift) + " " + (2 + jShift);
            }
            if (board[2 + iShift][0 + jShift] == emptyCh && board[1 + iShift][0 + jShift] == blackCh
                    && board[2 + iShift][1 + jShift] == blackCh && board[0 + iShift][0 + jShift] == emptyCh
                    && board[2 + iShift][2 + jShift] == emptyCh) {
                return (2 + iShift) + " " + (0 + jShift);
            }
            if (board[2 + iShift][2 + jShift] == emptyCh && board[2 + iShift][1 + jShift] == blackCh
                    && board[1 + iShift][2 + jShift] == blackCh && board[2 + iShift][0 + jShift] == emptyCh
                    && board[0 + iShift][2 + jShift] == emptyCh) {
                return (2 + iShift) + " " + (2 + jShift);
            }
        }
        return "";
    }

    /**
     * a good rotate is one that keeps the diagonal chance between square 1 and 4
     * 
     * @return
     */
    private String goodRotate() {
        int k;
        char[][] tmpBoard = new char[6][6];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                tmpBoard[i][j] = board[i][j];
            }
        }
        if (tmpBoard[1][2] == blackCh && tmpBoard[0][1] == blackCh) {
            k = 4;
            rotate(k, "+", tmpBoard);
            if ((tmpBoard[4][5] == blackCh && tmpBoard[3][4] == blackCh)) {
                return k + " +";
            }
            rotate(k, "-", tmpBoard);
            rotate(k, "-", tmpBoard);
            if ((tmpBoard[4][5] == blackCh && tmpBoard[3][4] == blackCh)) {
                return k + " -";
            }
            rotate(k, "+", tmpBoard);
        }
        if (tmpBoard[1][0] == blackCh && tmpBoard[2][1] == blackCh) {
            k = 4;
            rotate(k, "+", tmpBoard);
            if ((tmpBoard[4][3] == blackCh && tmpBoard[5][4] == blackCh)) {
                return k + " +";
            }
            rotate(k, "-", tmpBoard);
            rotate(k, "-", tmpBoard);
            if ((tmpBoard[4][3] == blackCh && tmpBoard[5][4] == blackCh)) {
                return k + " -";
            }
            rotate(k, "+", tmpBoard);
        }
        if (tmpBoard[3][4] == blackCh && tmpBoard[4][5] == blackCh) {
            k = 1;
            rotate(k, "+", tmpBoard);
            if ((tmpBoard[0][1] == blackCh && tmpBoard[1][2] == blackCh)) {
                return k + " +";
            }
            rotate(k, "-", tmpBoard);
            rotate(k, "-", tmpBoard);
            if ((tmpBoard[0][1] == blackCh && tmpBoard[1][2] == blackCh)) {
                return k + " -";
            }
            rotate(k, "+", tmpBoard);
        }
        if (tmpBoard[4][3] == blackCh && tmpBoard[5][4] == blackCh) {
            k = 1;
            rotate(k, "+", tmpBoard);
            if ((tmpBoard[1][0] == blackCh && tmpBoard[2][1] == blackCh)) {
                return k + " +";
            }
            rotate(k, "-", tmpBoard);
            rotate(k, "-", tmpBoard);
            if ((tmpBoard[1][0] == blackCh && tmpBoard[2][1] == blackCh)) {
                return k + " -";
            }
            rotate(k, "+", tmpBoard);
        }
        k = 1;
        rotate(k, "+", tmpBoard);
        if ((tmpBoard[1][2] == blackCh && tmpBoard[0][1] == blackCh)
                || (tmpBoard[1][0] == blackCh && tmpBoard[2][1] == blackCh)) {
            return k + " +";
        }
        rotate(k, "-", tmpBoard);
        rotate(k, "-", tmpBoard);
        if ((tmpBoard[1][2] == blackCh && tmpBoard[0][1] == blackCh)
                || (tmpBoard[1][0] == blackCh && tmpBoard[2][1] == blackCh)) {
            return k + " -";
        }
        rotate(k, "+", tmpBoard);
        k = 4;
        rotate(k, "+", tmpBoard);
        if ((tmpBoard[4][5] == blackCh && tmpBoard[3][4] == blackCh)
                || (tmpBoard[4][3] == blackCh && tmpBoard[5][4] == blackCh)) {
            return k + " +";
        }
        rotate(k, "-", tmpBoard);
        rotate(k, "-", tmpBoard);
        if ((tmpBoard[4][5] == blackCh && tmpBoard[3][4] == blackCh)
                || (tmpBoard[4][3] == blackCh && tmpBoard[5][4] == blackCh)) {
            return k + " -";
        }
        return "";
    }

    /**
     * looks for double trick
     * 
     * @return
     */
    private String getDouble() {
        for (int k = 1; k <= 4; k++) {
            int iShift = 0, jShift = 0;
            if (k == 2) {
                jShift = 3;
            }
            if (k == 3) {
                iShift = 3;
            }
            if (k == 4) {
                iShift = 3;
                jShift = 3;
            }
            if (board[0 + iShift][0 + jShift] == emptyCh && board[0 + iShift][1 + jShift] == emptyCh
                    && board[0 + iShift][2 + jShift] == blackCh && board[1 + iShift][0 + jShift] == blackCh
                    && board[2 + iShift][0 + jShift] == emptyCh) {
                return (0 + iShift) + " " + (0 + jShift);
            }
            if (board[0 + iShift][0 + jShift] == emptyCh && board[2 + iShift][0 + jShift] == emptyCh
                    && board[1 + iShift][0 + jShift] == blackCh && board[2 + iShift][2 + jShift] == blackCh
                    && board[2 + iShift][1 + jShift] == emptyCh) {
                return (2 + iShift) + " " + (0 + jShift);
            }
            if (board[0 + iShift][0 + jShift] == emptyCh && board[0 + iShift][1 + jShift] == blackCh
                    && board[0 + iShift][2 + jShift] == emptyCh && board[1 + iShift][0 + jShift] == emptyCh
                    && board[2 + iShift][0 + jShift] == blackCh) {
                return (0 + iShift) + " " + (0 + jShift);
            }
            if (board[0 + iShift][0 + jShift] == emptyCh && board[0 + iShift][1 + jShift] == blackCh
                    && board[0 + iShift][2 + jShift] == emptyCh && board[1 + iShift][2 + jShift] == emptyCh
                    && board[2 + iShift][2 + jShift] == blackCh) {
                return (0 + iShift) + " " + (2 + jShift);
            }
            if (board[0 + iShift][0 + jShift] == blackCh && board[0 + iShift][1 + jShift] == emptyCh
                    && board[0 + iShift][2 + jShift] == emptyCh && board[1 + iShift][2 + jShift] == blackCh
                    && board[2 + iShift][2 + jShift] == emptyCh) {
                return (0 + iShift) + " " + (2 + jShift);
            }
            if (board[0 + iShift][2 + jShift] == emptyCh && board[1 + iShift][2 + jShift] == blackCh
                    && board[2 + iShift][0 + jShift] == blackCh && board[2 + iShift][1 + jShift] == emptyCh
                    && board[2 + iShift][2 + jShift] == emptyCh) {
                return (2 + iShift) + " " + (2 + jShift);
            }
            if (board[0 + iShift][2 + jShift] == blackCh && board[1 + iShift][2 + jShift] == emptyCh
                    && board[2 + iShift][0 + jShift] == emptyCh && board[2 + iShift][1 + jShift] == blackCh
                    && board[2 + iShift][2 + jShift] == emptyCh) {
                return (2 + iShift) + " " + (2 + jShift);
            }
            if (board[0 + iShift][0 + jShift] == blackCh && board[1 + iShift][0 + jShift] == emptyCh
                    && board[2 + iShift][0 + jShift] == emptyCh && board[2 + iShift][1 + jShift] == blackCh
                    && board[2 + iShift][2 + jShift] == emptyCh) {
                return (2 + iShift) + " " + (0 + jShift);
            }
        }
        return "";
    }

    /**
     * get a diagonal trick
     * 
     * @return
     */
    private String getDiagonal() {
        for (int k = 1; k <= 4; k++) {
            int iShift = 0, jShift = 0;
            if (k == 2) {
                jShift = 3;
            }
            if (k == 3) {
                iShift = 3;
            }
            if (k == 4) {
                iShift = 3;
                jShift = 3;
            }
            if (board[0 + iShift][0 + jShift] == blackCh && board[2 + iShift][2 + jShift] == blackCh) {
                if (board[1 + iShift][0 + jShift] == emptyCh && board[2 + iShift][0 + jShift] == emptyCh
                        && board[2 + iShift][1 + jShift] == emptyCh) {
                    return (2 + iShift) + " " + (0 + jShift);
                }
                if (board[0 + iShift][1 + jShift] == emptyCh && board[0 + iShift][2 + jShift] == emptyCh
                        && board[1 + iShift][2 + jShift] == emptyCh) {
                    return (0 + iShift) + " " + (2 + jShift);
                }
            }
            if (board[0 + iShift][2 + jShift] == blackCh && board[2 + iShift][0 + jShift] == blackCh) {
                if (board[0 + iShift][0 + jShift] == emptyCh && board[0 + iShift][1 + jShift] == emptyCh
                        && board[1 + iShift][0 + jShift] == emptyCh) {
                    return (0 + iShift) + " " + (0 + jShift);
                }
                if (board[1 + iShift][2 + jShift] == emptyCh && board[2 + iShift][2 + jShift] == emptyCh
                        && board[2 + iShift][1 + jShift] == emptyCh) {
                    return (2 + iShift) + " " + (2 + jShift);
                }
            }
        }
        return "";
    }
}