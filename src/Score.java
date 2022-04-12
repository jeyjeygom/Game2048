/**
 * Each object of this class is a row of best scores table.
 */
public class Score {
    private String name;
    private int result;

    public Score(String name, int result) { // construct score with 2 variables
        this.name = name;
        this.result = result;
    }

    public int getPoints() {
        return result;
    } // return number of points (result)

    public String getName() {
        return name;
    } // return game of the player

    public String toString() {
        return "Score(" + name + "," + result + ")";
    }  // return String with name and result for each line

}