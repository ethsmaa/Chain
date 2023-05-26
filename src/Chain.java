import enigma.console.TextAttributes;
import enigma.core.Enigma;
import enigma.event.TextMouseEvent;
import enigma.event.TextMouseListener;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.io.FileWriter;
import java.awt.Color;



public class Chain {
    public static TextAttributes cyan = new TextAttributes(Color.cyan);
    public static TextAttributes black = new TextAttributes(Color.BLACK);
    public static TextAttributes white = new TextAttributes(Color.white);
    public static TextAttributes blue = new TextAttributes(Color.BLUE);
    public static TextAttributes gray = new TextAttributes(Color.GRAY);
    public static TextAttributes green = new TextAttributes(Color.GREEN);
    public static TextAttributes magenta = new TextAttributes(Color.MAGENTA);
    public static TextAttributes orange = new TextAttributes(Color.orange);
    public static TextAttributes red = new TextAttributes(Color.red);
    public static TextAttributes yellow = new TextAttributes(Color.yellow);
    public static TextAttributes pink = new TextAttributes(Color.PINK);

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

    //for losing logic
    static Boolean isLost = false;
    static int chains = 0;

    static int row = 19;
    static int column = 31;

    //given, number of rounds and score
    static int score = 0;
    static int rounds = 0;

    // the y position used to print the text in the table
    static int printY = 5;


    //player name
    static String Username;

    // table
    static MultiLinkedList table = new MultiLinkedList();
    static int countTable = 1;

    Chain(int seeed, String Uname) throws Exception {   // --- Contructor
        //random seed is defined here
        Random random = new Random(seeed);

        //username is defined here
        Username = Uname;


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

        // map is filled here
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (i % 2 != 1 && j % 2 != 1) {
                    map[i][j] = (char) (random.nextInt(4) + 1);
                } else
                    map[i][j] = 0;
            }
        }

        // User interface, rounds, score is printed here
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


        mousey = 1;
        mousex = 1;


        // isLost is used to stop when losing
        while (!isLost) {

            if (mousepr == 1) {  // if mouse button pressed


                // It is set so that there is no error when you press the edge
                try {
                    if (isSquareEmpty(mousex, mousey) && (mousex % 2 != 1 || mousey % 2 != 1)) {
                        cn.getTextWindow().output(mousex, mousey, '+');  // write a char to x,y position without changing cursor position
                        map[mousey][mousex] = '+'; // arraye ekle
                    } else if (map[mousey][mousex] == '+') {
                        cn.getTextWindow().output(mousex, mousey, ' ');
                        map[mousey][mousex] = 0; // arraye ekle

                    }
                } catch (Exception e) {

                } finally {
                }

                mousepr = 0;     // last action

            }


            if (keypr == 1) {    // if keyboard button pressed


                if (rkey == KeyEvent.VK_LEFT && mousex > 0 && isSquareEmpty(mousex - 1, mousey)) {

                    if (map[mousey][mousex] != '+') {
                        cn.getTextWindow().output(mousex, mousey, ' ');
                    }
                    mousex--;

                }
                if (rkey == KeyEvent.VK_RIGHT && mousex < column - 1 && isSquareEmpty(mousex + 1, mousey)) {
                    if (map[mousey][mousex] != '+') {
                        cn.getTextWindow().output(mousex, mousey, ' ');
                    }
                    mousex++;
                }
                if (rkey == KeyEvent.VK_UP && mousey > 0 && isSquareEmpty(mousex, mousey - 1)) {

                    if (map[mousey][mousex] != '+') {
                        cn.getTextWindow().output(mousex, mousey, ' ');
                    }
                    mousey--;

                }
                if (rkey == KeyEvent.VK_DOWN && mousey < row - 1 && isSquareEmpty(mousex, mousey + 1)) {

                    if (map[mousey][mousex] != '+') {
                        cn.getTextWindow().output(mousex, mousey, ' ');
                    }
                    mousey++;

                }
                cn.getTextWindow().output(mousex, mousey, '_');

                if (rkey == KeyEvent.VK_SPACE) {
                    try {
                        if ( mousex < column && mousey < row && isSquareEmpty(mousex, mousey) && (mousex % 2 != 1 || mousey % 2 != 1)) {
                            cn.getTextWindow().output(mousex, mousey, '+');  // write a char to x,y position without changing cursor position
                            map[mousey][mousex] = '+';
                        } else if (map[mousey][mousex] == '+') {
                            cn.getTextWindow().output(mousex, mousey, ' ');
                            map[mousey][mousex] = 0; // arraye ekle

                        }
                    } catch (Exception e) {
                    }
                }


                if (rkey == KeyEvent.VK_ENTER) {
                    chain();
                    //when this function runs, the generated
                    // chain is added to the SLL. instead of elements
                    // in the chain '.' is written.

                }

                // end the game when the e is pressed
                if (rkey == KeyEvent.VK_E) {
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
                    cn.getTextWindow().output(j, i, Integer.toString(map[i][j]).charAt(0), red);
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
        if (x + 1 < 31 && map[y][x + 1] == '+') count++; // right
        if (x - 1 > 0 && map[y][x - 1] == '+') count++; // left
        if (y + 1 < 19 && map[y + 1][x] == '+') count++; // bottom
        if (y - 1 > 0 && map[y - 1][x] == '+') count++; // top
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

    SLL chain() {
        SLL chain = new SLL();
        int i = findTail()[0];
        int j = findTail()[1];
        int say = 0;
        int chainLength = plusCount() + 1; // elements count in the chain
        int[] numbers = new int[chainLength];
        chain.add((int) map[i][j]);
        numbers[0] = Integer.valueOf(map[i][j]);
        map[i][j] = '.';
        cn.getTextWindow().output(j, i, '.');

        for (int m = 1; m < chainLength; m++) {
            if (j + 1 < column && map[i][j + 1] == '+') { // right
                map[i][j + 1] = '0'; // + is deleted
                cn.getTextWindow().output(j + 1, i, ' ');
                j += 2;
                chain.add((int) map[i][j]);
            } else if (j - 1 > 0 && map[i][j - 1] == '+') { // left
                map[i][j - 1] = '0';
                cn.getTextWindow().output(j - 1, i, ' ');
                j -= 2;
                chain.add((int) map[i][j]);
            } else if (i + 1 < row && map[i + 1][j] == '+') { // bottom
                map[i + 1][j] = '0';
                cn.getTextWindow().output(j, i + 1, ' ');
                i += 2;
                chain.add((int) map[i][j]);
            } else if (i - 1 > 0 && map[i - 1][j] == '+') { // top
                map[i - 1][j] = '0';
                cn.getTextWindow().output(j, i - 1, ' ');
                i -= 2;
                chain.add((int) map[i][j]);
            }
            numbers[m] = Integer.valueOf(map[i][j]);
            map[i][j] = '.';
            cn.getTextWindow().output(j, i, '.');
            say++;
        }

        if(checkChain(numbers, chain)) // zincir doğruysa ekrana basılır
            PrintToTable(numbers, chain);
        //If there is no error in the chain, updating the score, otherwise the game ends
        if (!isLost) {
            //We add n*n to the score, n is the number of digits
            UpdateScore(score + (say + 1) * (say + 1));
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

    static boolean checkChain(int[] chainElements, SLL chain) {
        if (chainElements.length < 4) {
            lost();
            return false;
        }

        for (int i = 0; i < chainElements.length; i++) {
            if (chainElements[i] == 46) {
                lost();
                return false;
            }
        }

        if (!chain.checkDifference()) {
            lost();
            return false;
        }

        return true;
    }



    static void PrintToTable(int[] chainElements, SLL chain) {
        int pos = 35;
        cn.getTextWindow().setCursorPosition(pos, printY);
        if(countTable!=1)
            cn.getTextWindow().output(pos,printY-1,'+');
        printY+=2;

        chain.display();

        table.addRound(rounds);
        for(int i=0;i<chainElements.length;i++){
            table.addChain(rounds,chainElements[i]);
        }

    }

    //while losing, GameOver is displayed at the bottom,
    // and the game is stopped, while loop is over
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

