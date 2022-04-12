import java.io.*;
import java.util.Scanner;

/**
 * Class that manages the table of the best scores.
 * Each instance object of this class is a distinct table.
 *
 * The getRow, canAdd, and addRow methods are marked to be implemented.
 *
 * The saveToFile and restoreFromFile methods are already implemented to write to file
 * and restore from the file the best scores table.
 * Just uncomment the lines marked and implement the Score class.
 */
public class TopScores {
    // File name to save and restore table.
    private static final String FILE_NAME = "top2048.txt";

    // Maximum rows of the table.
    public static final int MAX_SCORES = 5;

    // The table. Each row of the table is an object of Score class.
    private static Score[] table = new Score[MAX_SCORES];
    // Number of rows currently used in table.
    private static int rows = 0;

    /**
     * The constructor read the table from file.
     */
    public TopScores() {
        restoreFromFile();
    }

    /**
     * Returns one row of the table.
     *
     * @param idx Index of the row ( 0 .. getNumOfRows() )
     * @return The row. One object of the class Score
     */
    public Score getRow(int idx) {
        return table[idx];
    }

    /**
     * Returns the number of rows currently used in table.
     *
     * @return a value between 0 and MAX_SCORES
     */
    public static int getNumOfRows() {
        return rows;
    }

    /**
     * Check the possibility to add the score to the record table.
     *
     * @param score The score to try add
     * @return true if the table is not full or the score is greater than the last stored
     */
    public boolean canAdd(int score) {
        Score sc;
        if (rows != 0 || rows == 5) {
            sc = table[rows - 1];
        } else sc = table[rows];
      return ((rows < table.length) || (score > sc.getPoints()));
    }

    /**
     * Inserts one more row in the table.
     * The table should be sorted in descending order of the score.
     *
     * @param name  of the player
     * @param score
     * @return true if is inserted.
     */
    public boolean addRow(String name, int score) {
        if (!(canAdd(score)))
            return false;
        if (rows < table.length) ++rows;
        int last = rows - 1;
        while (last > 0 && table[last - 1].getPoints() < score) {
            table[last] = table[last - 1];
            --last;
        }
        table[last] = new Score(name, score);
        return true;
    }

    /**
     * Load the table of best scores from a text file.
     */
    private void restoreFromFile() {
        try {
            Scanner in = new Scanner(new FileReader(FILE_NAME));
            for (; in.hasNextLine(); ++rows) {
                int points = in.nextInt();
                String name = in.nextLine().trim();
                table[rows] = new Score(name, points);  // DONE TODO: UNCOMMENT to use Score constructor
            }
            in.close();
        } catch (Exception e) {
            System.out.println("Error reading file " + FILE_NAME);
        }
    }

    /**
     * Save the table of best scores in a text file.
     */
    public void saveToFile() {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(FILE_NAME));
            for (int i = 0; i < rows; i++) {
                out.println(table[i].getPoints() + " " + table[i].getName());  // TODO: UNCOMMENT to use fields of Score
            }
            out.close();
        } catch (Exception e) {
            System.out.println("Error writing file " + FILE_NAME);
        }
    }
}