/**
 * Main
 * 
 * @author Amirali Belbasi
 */
public class Main {

    public static void main(String[] args) {
        Board test = new Board();
        test.showBoard();
        System.out.println("-----");
        test.determinePossibleMoves();
        test.showBoard();
    }
}