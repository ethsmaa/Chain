import enigma.core.Enigma;
import enigma.event.TextMouseEvent;
import enigma.event.TextMouseListener;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.io.FileWriter;



public class Chain {
    public static enigma.console.Console cn = Enigma.getConsole("Chain", 60, 20, 20, 0);
    public TextMouseListener tmlis;
    public KeyListener klis;


    // ------ Standard variables for mouse and keyboard ------
    public int mousepr;          // mouse pressed?
    public int mousex, mousey;   // mouse text coords.
    public int keypr;   // key pressed?
    public int rkey;    // key   (for press/release)
    // ----------------------------------------------------

    // map is defined here
    static char[][] map = new char[19][31];


    static Boolean isLost = false;


    static int chains=0;

    static int row = 19;
    static int column = 31;

    //verilenler , raund sayi ve score
    static int score = 0;
    static int rounds = 0;

    // the y position used to print the text in the table
    static int printY = 5;

    //player name
    static String Username;
    Chain(int seeed , String Uname) throws Exception {   // --- Contructor

        Random random = new Random(seeed);

        Username = Uname; // username is set here


        cn.getTextWindow().setCursorPosition(47, 0); // seed is printed here
        cn.getTextWindow().output(Integer.valueOf(seeed).toString());
        // ------ Standard code for mouse and keyboard ------ Do not change
        tmlis = new TextMouseListener() {
            public void mouseClicked(TextMouseEvent arg0) {
            }

            public void mousePressed(TextMouseEvent arg0) {
                if (mousepr == 0) {
                    mousepr = 1;
                    mousex = arg0.getX();
                    mousey = arg0.getY();
                }
            }

            public void mouseReleased(TextMouseEvent arg0) {
            }
        };
        cn.getTextWindow().addTextMouseListener(tmlis);

        klis = new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                if (keypr == 0) {
                    keypr = 1;
                    rkey = e.getKeyCode();
                }
            }

            public void keyReleased(KeyEvent e) {
            }
        };
        cn.getTextWindow().addKeyListener(klis);
        // ----------------------------------------------------

        // map
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (i % 2 != 1 && j % 2 != 1) {
                    map[i][j] = (char) (random.nextInt(4) + 1);
                } else
                    map[i][j] = 0;
            }
        }

       // User interface, rounds, score can be found here
        cn.getTextWindow().setCursorPosition(35, 0);
        cn.getTextWindow().output("Board Seed:");
        cn.getTextWindow().setCursorPosition(35, 1);
        cn.getTextWindow().output("Round:");
        cn.getTextWindow().setCursorPosition(35, 2);
        cn.getTextWindow().output("Score:");
        cn.getTextWindow().setCursorPosition(35, 3);
        cn.getTextWindow().output("------------------------");
        cn.getTextWindow().setCursorPosition(35, 4);
        cn.getTextWindow().output("Table:");
        cn.getTextWindow().setCursorPosition(35, 4);
        cn.getTextWindow().setCursorPosition(43, 2);
        cn.getTextWindow().output(Integer.valueOf(score).toString());
        cn.getTextWindow().setCursorPosition(43, 1);
        cn.getTextWindow().output(Integer.valueOf(rounds).toString());

        printMap();



        /* player yön tuşları ile oynayacaksa
        mousey=1;
        mousex=1;
         */

        // isLost is used to stop when losing
        while (!isLost) {

            if (mousepr == 1) {  // if mouse button pressed


                // It is set so that there is no error when you press the edge
                try {
                    if (isSquareEmpty(mousex, mousey) && (mousex % 2 != 1 || mousey % 2 != 1)) {
                        cn.getTextWindow().output(mousex, mousey, '+');  // write a char to x,y position without changing cursor position
                        map[mousey][mousex] = '+'; // arraye ekle
                    }
                }catch(Exception e) {

                }finally{}

                mousepr = 0;     // last action

            }


            if (keypr == 1) {    // if keyboard button pressed


                    if (rkey == KeyEvent.VK_LEFT && isSquareEmpty(mousex - 1, mousey)) {
                        if (mousex > 0) {
                            if (map[mousey][mousex] != '+') {
                                cn.getTextWindow().output(mousex, mousey, ' ');
                            }
                            mousex--;
                        }
                    }
                    if (rkey == KeyEvent.VK_RIGHT && isSquareEmpty(mousex + 1, mousey)) {
                        if (mousex < column - 1) {
                            if (map[mousey][mousex] != '+') {
                                cn.getTextWindow().output(mousex, mousey, ' ');
                            }
                            mousex++;
                        }
                    }
                    if (rkey == KeyEvent.VK_UP && isSquareEmpty(mousex, mousey - 1)) {
                        if (mousey > 0) {
                            if (map[mousey][mousex] != '+') {
                                cn.getTextWindow().output(mousex, mousey, ' ');
                            }
                            mousey--;
                        }
                    }
                    if (rkey == KeyEvent.VK_DOWN && isSquareEmpty(mousex, mousey + 1)) {
                        if (mousey < row - 1) {
                            if (map[mousey][mousex] != '+') {
                                cn.getTextWindow().output(mousex, mousey, ' ');
                            }
                            mousey++;
                        }
                    }
                cn.getTextWindow().output(mousex, mousey, '_');

                if( rkey == KeyEvent.VK_SPACE) {
                    try {
                        if (isSquareEmpty(mousex, mousey) && (mousex % 2 != 1 || mousey % 2 != 1)) {
                            cn.getTextWindow().output(mousex, mousey, '+');  // write a char to x,y position without changing cursor position
                            map[mousey][mousex] = '+';
                        }
                    } catch (Exception e) {
                    }
                }


                if (rkey == KeyEvent.VK_ENTER) {
                    Chain();
                    //when this function runs, the generated
                    // chain is added to the SLL. instead of elements
                    // in the chain '.' is written.

                }

                // end the game when the e is pressed
                if (rkey == KeyEvent.VK_E){
                    lost();
                }
                keypr = 0;    // last action
            }


            Thread.sleep(20);
        }
    }


    void printMap() {
        //print map
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == 0)
                    cn.getTextWindow().output(j, i, ' ');
                else
                    cn.getTextWindow().output(j, i, Integer.toString(map[i][j]).charAt(0));
            }
        }
    }


    boolean isSquareEmpty(int x, int y) {
        if (map[y][x] == 0) return true;
        else return false;

    }


    int[] findTail() { // this function returns the coordinates of the square with only 1 '+' sign around it in the map
        int coordinatesofTail[] = new int[2];
        boolean isTailFound = false;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (i % 2 != 1 && j % 2 != 1 && plusDirectionCount(j, i) == 1) { // only look at squares with numbers
                    coordinatesofTail[0] = i;
                    coordinatesofTail[1] = j;
                    isTailFound = true;
                    break;
                }

            }
            if (isTailFound) break;
        }
        return coordinatesofTail;
    }

    int plusDirectionCount(int x, int y) { // + number around the square
        int count = 0;
        if (x + 1 < 31 && map[y][x + 1] == '+') count++; // sağ
        if (x - 1 > 0 && map[y][x - 1] == '+') count++; // sol
        if (y + 1 < 19 && map[y + 1][x] == '+') count++; // alt
        if (y - 1 > 0 && map[y - 1][x] == '+') count++; // üst
        return count;
    }

    int plusCount() {
        int count = 0;
        for (int i = 0; i < row; i++) { // + number in the map
            for (int j = 0; j < column; j++) {
                if (map[i][j] == '+') count++;
            }
        }
        return count;
    }

    SLL Chain() {
        SLL chain = new SLL();
        int i = findTail()[0];
        int j = findTail()[1];
        int say =0;
        int chainLength = plusCount() + 1; // elements count in the chain
        int[] numbers = new int[chainLength];
        chain.add(map[i][j]);
        numbers[0] = Integer.valueOf(map[i][j]);
        map[i][j] = '.';
        cn.getTextWindow().output(j, i, '.');

        for (int m = 1; m < chainLength; m++) {
            if (j + 1 < column && map[i][j + 1] == '+') { // sağ
                map[i][j + 1] = ' '; // + silinir
                cn.getTextWindow().output(j + 1, i, ' ');
                j += 2;
                chain.add(map[i][j]);
            }
            else if (j - 1 > 0 && map[i][j - 1] == '+') { // sol
                map[i][j - 1] = ' ';
                cn.getTextWindow().output(j - 1, i, ' ');
                j -= 2;
                chain.add(map[i][j]);
            }
            else if (i + 1 < row && map[i + 1][j] == '+') { // alt
                map[i + 1][j] = ' ';
                cn.getTextWindow().output(j, i + 1, ' ');
                i += 2;
                chain.add(map[i][j]);
            }
            else if (i - 1 > 0 && map[i - 1][j] == '+') { // üst
                map[i - 1][j] = ' ';
                cn.getTextWindow().output(j, i - 1, ' ');
                i -= 2;
                chain.add(map[i][j]);
            }
            numbers[m] = Integer.valueOf(map[i][j]);
            map[i][j] = '.';
            cn.getTextWindow().output(j, i, '.');
            say++;
        }
        //print to table
        PrintToTable(numbers);
        //If there is no error in the chain, updating the score, otherwise the game ends
        if(!isLost) {
            //We add n*n to the score, n is the number of digits
            UpdateScore(score+(say+1)*(say+1));
            UpdateRounds();
        }
        return chain;
    }

    //this function updates and prints the score
    static void UpdateScore(int ns) {
        score = ns;
        cn.getTextWindow().setCursorPosition(43, 2);
        cn.getTextWindow().output(Integer.valueOf(score).toString());

    }
    //updating rounds
    static void UpdateRounds() {
        rounds++;
        cn.getTextWindow().setCursorPosition(43, 1);
        cn.getTextWindow().output(Integer.valueOf(rounds).toString());
    }


    static void PrintToTable(int[] nn) {
        int pos = 35;
        if(nn.length < 4)lost();

        for (int i = 0; i < nn.length && !isLost; i++) {
            //46 occurs when nn[i] = "." ,
            // this means that there is an error in the chain,
            // at which point the game ends
            if(nn[i] == 46) {
                lost();
            }
            //otherwise the game continues at the given level and ends.
            cn.getTextWindow().setCursorPosition(pos, printY);
            pos++;
            cn.getTextWindow().output(Integer.valueOf(nn[i]).toString());
            //cap etme mentiqidir
            if(i!=nn.length-1) {
                if(nn[i] != nn[i+1]+1 && nn[i] != nn[i+1]-1)lost();
                cn.getTextWindow().setCursorPosition(pos, printY);
                cn.getTextWindow().output("+");
                pos++;
            }
        }

        //positions of the + sign
        if(chains!=0) {
            printY--;
            cn.getTextWindow().setCursorPosition(35, printY);
            cn.getTextWindow().output("+");
            printY++;
        }
        printY+=2;
        chains++;
    }
    //while losing, GameOver is displayed at the bottom,
    // and the game is saved, while the round ends
    static void lost() {
        cn.getTextWindow().setCursorPosition(35, 16);
        cn.getTextWindow().output("Error in chain");
        cn.getTextWindow().setCursorPosition(35, 17);
        cn.getTextWindow().output("-Game Over-");
        try {
            FileWriter fileWriter = new FileWriter("highscore.txt");
            String information = Username.concat("       ").concat(Integer.valueOf(score).toString());
            fileWriter.write(information);
            fileWriter.close();
        } catch (Exception e) {
            System.out.print("An error occurred while writing to the file: " + e.getMessage());
        }
        isLost = true;
    }
}


