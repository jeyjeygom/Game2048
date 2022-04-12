import isel.leic.pg.Console;
import java.awt.event.KeyEvent;

public class Game2048 {
    static final int LINES = 4, COLS = 4;
    static final int MIN_VALUE = 2, MAX_VALUE = 2048;
    private static TopScores top = new TopScores();

    private static int moves = 0;
    private static boolean exit, winner;
    private static int key;

    private static int l = LINES / 2, c = COLS / 2;
    private static int value = 2, score, changes;

    private static int[][] Matrix = new int [LINES][COLS];


    public static void main(String[] args) {
        Panel.open();
        init();
        for (; ; ) {
                new_random_tile();
                value = random_value();
                Panel.showTile(l, c, value);
                updatematrixvalue(l, c, value);
                key = Console.waitKeyPressed(0);
                if (!processKey(key)) break;
                while (Console.isKeyPressed(key)) ;
        }
        Panel.close();
        top.saveToFile();
    }

    private static void init() {
        Panel.showMessage("Use cursor keys to play");
        Panel.updateMoves(moves);
        Panel.updateScore(0);
        displayTopScore();
    }

    private static boolean processKey(int key) {
            switch (key) {
                case KeyEvent.VK_UP:
                    move(-1, 0);
                    break;
                case KeyEvent.VK_DOWN:
                    move(+1, 0);
                    break;
                case KeyEvent.VK_LEFT:
                    move(0, -1);
                    break;
                case KeyEvent.VK_RIGHT:
                    move(0, +1);
                    break;
                case KeyEvent.VK_ESCAPE:
                case 'Q':
                    quitGame();
                    break;
                case 'N':
                    newGame();
                    break;
            }
            if (winner) Winner();
            if (changes==0) quitGame();
    return !exit;
    }

    private static void Winner(){   // winner
        message("Winner");
        saveTopScore();
        top.saveToFile();
        newGame();
        winner=false;
     }

    private static void message(String message) { // send message to panel during a time period
        for (int a=0; a<10; ++a) {
            Panel.hideMessage();
            Panel.showTempMessage(message,150);
            Panel.showTempMessage("", 150);
        }
    }

    private static void displayTopScore() { // Display Top Scores scoreboard
        for (int i = 0; i < top.MAX_SCORES; i++) {
            Score s = top.getRow(i);
            if (s == null && i == 0) {
                break;
            } else {
                if (s == null) break;
                Panel.updateBestRow(i+1, s.getName(), s.getPoints());
            }
        }
    }

    private static void saveTopScore() // Saves player top score
    {
        if (score == 0) return;
        if (top.canAdd(score)) {
            String name = Panel.questionString("Your Name", 10);
            top.addRow(name, score);
        }
    }

    private static void newGame(){ // starts a new game
        int resp = Panel.questionChar("New Game (Y/N)");
        if (resp == 'N')
            exit = true;
        if (resp == 'Y') {
            score=0;
            resetmatrix();
            printmatrix();
            resetscore();
            resetmoves();
            displayTopScore();
        }
        key = 0;
    }

    private static void quitGame() {    //quit game
        int resp = Panel.questionChar("Exit game (Y/N)");
        if (resp == 'Y') {
            exit = true;
            if (score == 0) return;
            if (top.canAdd(score)) {
                String name = Panel.questionString("Your Name", 10);
                top.addRow(name,score);
            }
        }
        key = 0;
    }

    private static void move(int dLin, int dCol) { // case UP, DOWN, LEFT and RIGHT key Selected
        updatematrixvalue(l,c,value);
        if (dLin == -1) {                                     // tecla UP
            arrange_up_tiles();
            sum_up_tiles();
            arrange_up_tiles();
        }
        if (dLin == +1) {                                     // tecla Down
            arrange_down_tiles();
            sum__down_tiles();
            arrange_down_tiles();
        }
        if (dCol == -1) {                                    // tecla Left
            arrange_left_tiles();
            sum_left_tiles();
            arrange_left_tiles();
        }
        if (dCol == +1) {                                     // tecla Right
            arrange_right_tiles();
            sum_right_tiles();
            arrange_right_tiles();
        }
        Panel.updateMoves(++moves);
        printmatrix();
        movesfull();
    }

    private static void new_random_tile () {  //new random tile
        do {
            l = (int) (Math.random() * LINES);
            c = (int) (Math.random() * COLS);
        } while (!checkmatrix(l, c));
    }

    private static int random_value () { //new random value for the tile
        value= (Math.random() * 100) < 90 ? 2 : 4;
        return value;
    }

    private static void arrange_right_tiles() { // arrange tiles right
        int max = LINES-1;
        for (int a=0; a<=max ;++a) {
            int Begin=-1, End=-1;
            for (int b = max; b >= 0;--b) {
                while (!checkmatrix(a, b) && Begin == -1)
                    if (b==0)
                        break;
                    else --b;
                if (checkmatrix(a, b) && Begin==-1)
                    Begin = b;
                if (!checkmatrix(a, b) && End==-1)
                    End = b;
                if (Begin !=-1 && End != -1) {
                    Matrix[a][Begin]=Matrix[a][End];
                    updatematrixvalue(a,End,0);
                    b=max; Begin=End=-1;
                    }
            }
        }
    }

    private static void arrange_left_tiles() { // arrange tiles left
        int max = LINES-1;
        for (int a = 0; a <= max; ++a) {
            int Begin=-1, End=-1;
            for (int b = 0; b <= max ; ++b) {
                while (!checkmatrix(a, b) && Begin == -1)
                    if (b==max)
                        break;
                    else ++b;
                if (checkmatrix(a, b) && Begin == -1)
                    Begin = b;
                if (!checkmatrix(a, b) && End == -1)
                    End = b;
                if (Begin != -1 && End != -1) {
                    Matrix[a][Begin] = Matrix[a][End];
                    updatematrixvalue(a,End,0);
                    b = 0;
                    Begin = End = -1;
                }
            }
        }
    }

    private static void arrange_up_tiles() { // arrange tiles up
        int max = COLS-1;
        for (int a=0; a<=max ;++a) {
            int Begin=-1, End=-1;
            for (int b = 0; b <=max;++b) {
                while (!checkmatrix(b, a) && Begin == -1)
                    if (b==max)
                        break;
                    else ++b;
                if (checkmatrix(b, a) && Begin==-1)
                    Begin = b;
                if (!checkmatrix(b, a) && End==-1)
                    End = b;
                if (Begin !=-1 && End != -1) {
                    Matrix[Begin][a]=Matrix[End][a];
                    updatematrixvalue(End,a,0);
                    b=0; Begin=End=-1;
                }
            }
        }
    }

    private static void arrange_down_tiles() { // arrange tiles down
        int max = COLS-1;
        for (int a=0; a<=max ;++a) {
            int Begin=-1, End=-1;
            for (int b = max; b>= 0;--b) {
                while (!checkmatrix(b, a) && Begin == -1)
                    if (b==0)
                        break;
                    else --b;
                if (checkmatrix(b, a) && Begin==-1)
                    Begin = b;
                if (!checkmatrix(b, a) && End==-1)
                    End = b;
                if (Begin !=-1 && End != -1) {
                    Matrix[Begin][a]=Matrix[End][a];
                    updatematrixvalue(End,a,0);
                    b=max; Begin=End=-1;
                }
            }
        }
    }

    private static void sum_up_tiles() { // sum tiles up
        int max = COLS-1;
        for (int a = max; a > 0; --a) {
            for (int b = max; b >= 0; --b) {
                if (!checkmatrix(a, b)) {
                    if (Matrix[a - 1][b] == Matrix[a][b]) {
                        Matrix[a - 1][b] *= 2;
                        Panel.updateScore(Score(Matrix[a - 1][b]));
                        winner(a-1,b);
                        updatematrixvalue(a,b,0);
                    }
                }
            }
        }
    }

    private static void sum__down_tiles(){ // sum tiles down
        int max = COLS;
        for (int a = max-2; a >=0 ; --a) {
            for (int b = 0; b < max; ++b) {
                if (!checkmatrix(a, b)) {
                    if (Matrix[a + 1][b] == Matrix[a][b]) {
                        Matrix[a + 1][b] *= 2;
                        Panel.updateScore(Score(Matrix[a + 1][b]));
                        winner(a+1,b);
                        updatematrixvalue(a,b,0);
                    }
                }
            }

        }
    }

    private static void sum_left_tiles() { // sum tiles left
        int max = Matrix.length;
        for (int a = 0; a < max; ++a) {
            for (int b = max-1; b > 0; --b) {
                if (!checkmatrix(a, b)) {
                    if (Matrix[a][b-1] == Matrix[a][b]) {
                        Matrix[a][b-1] *= 2;
                        Panel.updateScore(Score(Matrix[a][b-1]));
                        winner(a,b-1);
                        updatematrixvalue(a,b,0);
                    }
                }
            }
        }
    }

    private static void sum_right_tiles() { // sum tiles right
        int max = Matrix.length;
        for (int a = 0; a < max; ++a) {
            for (int b = 0; b < max-1; ++b) {
                if (!checkmatrix(a, b)) {
                    if (Matrix[a][b+1] == Matrix[a][b]) {
                        Matrix[a][b+1] *= 2;
                        Panel.updateScore(Score(Matrix[a][b+1]));
                        winner(a,b+1);
                        updatematrixvalue(a,b,0);
                    }
                }
            }
        }
    }

    private static void updatematrixvalue (int dLin, int dCol, int value){ // update tile value
        Matrix[dLin][dCol]=value;
    }

    private static boolean checkmatrix (int dLin, int dCol) { //check if tile is valid
        return (Matrix[dLin][dCol]==0);
    }

    private static void movesfull (){ // check if there are moves
        changes = 0;
        for (int a = 0; a < LINES; ++a) {
            for (int b = 0; b < COLS; ++b) {
                if (Matrix[a][b] == 0)
                        changes=1;
            }
        }
    }
    private static void printmatrix () { //Update all tiles
        for (int a = 0; a < LINES; ++a) {
            for (int b = 0; b < COLS; ++b) {
                if (checkmatrix(a, b))
                    Panel.hideTile(a, b);
                else
                    Panel.showTile(a, b, Matrix[a][b]);

            }
        }
    }

    private static void winner(int l , int c) { // wins if reach maxvalue
        winner = (Matrix[l][c] == MAX_VALUE);
    }

    private static void resetmatrix (){ // matrixreset
        for (int a=0; a< LINES; ++a)
            for (int b=0; b< COLS; ++b)
                updatematrixvalue(a, b,0);
    }

    private static int Score(int value) { // updates panel scoreboard
        return score+=value;
    }

    private static void resetmoves () {Panel.updateMoves(0);} //reset panel moves

    private static void resetscore() {Panel.updateScore(0);} //reset panel scoreboard
}