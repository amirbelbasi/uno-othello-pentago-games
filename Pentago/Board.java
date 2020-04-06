import java.util.Scanner;

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
    private char emptyCh = '\u25CE';
    private static char[][] board = new char[6][6];
    {
        for (int i = 0; i < 6; i++)
        {
            for (int j = 0; j < 6; j++)
            {
                board[i][j] = emptyCh;
            }
        }
    }
    private int turn = Turn.BLACK.ordinal();

    /**
     * shows board
     * @throws InterruptedException
     * @throws IOException
     */
    public void showBoard() throws InterruptedException, IOException
    {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        for (int i = 0; i < 6; i++)
        {
            if (i == 3)
            {
                System.out.println("-------------");
            }
            for (int j = 0; j < 6; j++)
            {
                if (j == 3)
                {
                    System.out.print("|");
                }
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * rotates a block clock wise or counter clock wise
     * @param indexOfBlock 1, 2, 3 or 4
     * @param direction CW or CCW
     */
    private void rotate(int indexOfBlock, String direction)
    {
        char[][] holderBoard = new char[6][6];
        int jShift = 0;
        int iShift = 0;
        if(indexOfBlock > 2)
        {
            iShift = 3;
        }
        if (indexOfBlock % 2 == 0)
        {
            jShift = 3;
        }
        if (direction.equals("CW")) // clock wise
        {
            for (int a = 0; a < 3; a++)
            {
                for (int b = 0; b < 3; b++)
                {
                    holderBoard[a + iShift][b + jShift] = board[2 - b + iShift][a + jShift];
                }
            }
        }
        else // counter clock wise
        {
            for (int a = 0; a < 3; a++)
            {
                for (int b = 0; b < 3; b++)
                {
                    holderBoard[2 - b + iShift][a + jShift] = board[a + iShift][b + jShift];
                }
            }
        }
        board = holderBoard;
    }

    /**
     * player playing his turn
     * @throws InterruptedException
     * @throws IOException
     */
    public void playTurn() throws InterruptedException, IOException
    {
        Scanner sc = new Scanner(System.in);
        System.out.print(turn == Turn.BLACK.ordinal() ? Turn.BLACK : Turn.WHITE + "'s turn ");
        System.out.print("please enter coordinates of cell you wish to put your disc on: ");
        String inp = sc.nextLine();
        if (!isInputValid(inp))
        {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            System.out.println("invalid input");
            Thread.sleep(1500);
            return;
        }
        if (!isValidCell(inp))
        {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            System.out.println("invalid cell");
            Thread.sleep(1500);
            return;
        }
        board[Integer.parseInt(inp.charAt(0)+"")][Integer.parseInt(inp.charAt(2)+"")] = (turn == Turn.BLACK.ordinal() ? blackCh : whiteCh);
        System.out.print("please enter details of block you wish to rotate: ");
        inp = sc.nextLine();
        rotate(indexOfBlock, direction);

        turn = switchTurn(turn);
    }

    /**
     * returns opposite turn
     * @param currentTurn
     * @return
     */
    private int switchTurn(int currentTurn)
    {
        return currentTurn == Turn.BLACK.ordinal() ? Turn.WHITE.ordinal() : Turn.BLACK.ordinal();
    }

    /**
     * checks if input is valid
     * @param inp
     * @return true if input is valid
     */
    private boolean isInputValid(String inp)
    {
        if (inp.length() != 3)
        {
            return false;
        }
        if ((int)inp.charAt(0) - 48 >= 0 && (int)inp.charAt(0) - 48 <= 5 && (int)inp.charAt(2) - 48 >= 0 && (int)inp.charAt(2) - 48 <= 5)
        {
            return true;
        }
        return false;
    }

    /**
     * checks if block is empty
     * @param inp
     * @return ture if cell is empty
     */
    private boolean isValidCell(String inp)
    {
        if(board[Integer.parseInt(inp.charAt(0)+"")][Integer.parseInt(inp.charAt(2)+"")] != emptyCh)
        {
            return false;
        }
        return true;
    }
}