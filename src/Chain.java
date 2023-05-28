import enigma.console.TextAttributes;
import enigma.core.Enigma;
import enigma.event.TextMouseEvent;
import enigma.event.TextMouseListener;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.Color;
import java.util.Scanner;


public class Chain {
    public static TextAttributes cyan = new TextAttributes(Color.cyan);
    public static TextAttributes black = new TextAttributes(Color.BLACK);
    public static TextAttributes white = new TextAttributes(Color.white);
    public static TextAttributes blue = new TextAttributes(Color.BLUE);
    public static TextAttributes gray = new TextAttributes(Color.GRAY);
    public static TextAttributes green = new TextAttributes(Color.GREEN);
    public static TextAttributes magenta = new TextAttributes(Color.magenta);
    public static TextAttributes orange = new TextAttributes(Color.orange);
    public static TextAttributes red = new TextAttributes(Color.red);
    public static TextAttributes yellow = new TextAttributes(Color.yellow);
    public static TextAttributes pink = new TextAttributes(Color.PINK);

    public static enigma.console.Console cn = Enigma.getConsole("Chain", 60, 24, 20, 0);
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

    static DoubleLinkedList highscoretable = new DoubleLinkedList();
    
    //player name
    static String Username;

    // table
    static MultiLinkedList table = new MultiLinkedList();
    static int countTable = 1;

    Chain() throws Exception {   // --- Contructor




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
        menu();
        
        Scanner scn = new Scanner(System.in);
    	
        cn.getTextWindow().setCursorPosition(0, 0);
        cn.getTextWindow().output("Enter Name: ");
        String playerName = scn.nextLine();
        
        cn.getTextWindow().setCursorPosition(0, 2);
        cn.getTextWindow().output("Enter seed (If you leave it blank, a random seed is generated): ");
        String seedInput = scn.nextLine();
        
        consoleClear();
        
        int seed;
        if (seedInput.isEmpty()) {
            seed = new Random().nextInt(9000) + 1000; // generate 1000 - 9999 number
            cn.getTextWindow().setCursorPosition(0, 0);
            cn.getTextWindow().output("Generated seed: " + seed);
        } else {
            try {
                seed = Integer.parseInt(seedInput);
                cn.getTextWindow().setCursorPosition(0, 0);
            	cn.getTextWindow().output("Generated seed: " + seed);
            } catch (NumberFormatException e) {
            	cn.getTextWindow().setCursorPosition(0, 0);
            	cn.getTextWindow().output("Wrong seed value: " + seedInput);
            	seed = new Random().nextInt(9000) + 1000; // generate 1000 - 9999 number
            	cn.getTextWindow().setCursorPosition(0, 2);
            	cn.getTextWindow().output("Generated seed: " + seed);
            }
        }

     //   Thread.sleep(2000);
        
        consoleClear();
        
        scn.close();
        
        //random seed is defined here
        Random random = new Random(seed);
        
        Username = playerName;
        
        cn.getTextWindow().setCursorPosition(47, 0); // seed is printed here
        cn.getTextWindow().output(Integer.valueOf(seed).toString());
        
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
                        cn.getTextWindow().output(mousex, mousey, '+', pink);  // write a char to x,y position without changing cursor position
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
                cn.getTextWindow().output(mousex, mousey, '_', gray);

                if (rkey == KeyEvent.VK_SPACE) {
                    try {
                        if (mousex < column && mousey < row && isSquareEmpty(mousex, mousey) && (mousex % 2 != 1 || mousey % 2 != 1)) {
                            cn.getTextWindow().output(mousex, mousey, '+', pink);  // write a char to x,y position without changing cursor position
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
                    highScoreTable();
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
        if (map[y][x] == ' ' || map[y][x] == 0) return true;
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
        cn.getTextWindow().output(j, i, '.', gray);

        for (int m = 1; m < chainLength; m++) {
            if (j + 1 < column && map[i][j + 1] == '+') { // right
                map[i][j + 1] = ' '; // + is deleted
                cn.getTextWindow().output(j + 1, i, ' ');
                j += 2;
                chain.add((int) map[i][j]);
            } else if (j - 1 > 0 && map[i][j - 1] == '+') { // left
                map[i][j - 1] = ' ';
                cn.getTextWindow().output(j - 1, i, ' ');
                j -= 2;
                chain.add((int) map[i][j]);
            } else if (i + 1 < row && map[i + 1][j] == '+') { // bottom
                map[i + 1][j] = ' ';
                cn.getTextWindow().output(j, i + 1, ' ');
                i += 2;
                chain.add((int) map[i][j]);
            } else if (i - 1 > 0 && map[i - 1][j] == '+') { // top
                map[i - 1][j] = ' ';
                cn.getTextWindow().output(j, i - 1, ' ');
                i -= 2;
                chain.add((int) map[i][j]);
            }
            numbers[m] = Integer.valueOf(map[i][j]);
            map[i][j] = '.';
            cn.getTextWindow().output(j, i, '.',gray);
            say++;
        }

        if (checkChain(numbers, chain)) // zincir doğruysa ekrana basılır
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
        if (printY != 5)
            cn.getTextWindow().output(pos, printY - 1, '+');
        printY += 2;

        chain.display();

        table.addRound(rounds);
        for (int i = 0; i < chainElements.length; i++) {
            table.addChain(rounds, chainElements[i]);
        }

        cn.getTextWindow().setCursorPosition(35, 14);

        // table.display();   --> şimdilik bu satır kullanım dışı

    }


    //while losing, GameOver is displayed at the bottom,
    // and the game is stopped, while loop is over
    static void lost() {
        Scanner scanner = new Scanner(System.in);
        Player player = new Player(Username, score);
        
        try (BufferedReader reader = new BufferedReader(new FileReader("highscore.txt"))) {
            String satir;
            while ((satir = reader.readLine()) != null) {
            	String[] playerinfo = satir.split(" ");
            	int playerscore = Integer.parseInt(playerinfo[1]);
            	Player newplayer = new Player(playerinfo[0], playerscore);
            	highscoretable.addPlayer(newplayer);	
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        cn.getTextWindow().setCursorPosition(35, 16);
        cn.getTextWindow().output("Error in chain");
        cn.getTextWindow().setCursorPosition(35, 17);
        cn.getTextWindow().output("-Game Over-");

        highscoretable.addPlayer(player);
        highscoretable.clearTextFile();
        highscoretable.saveToFile("highscore.txt");

        scanner.nextLine(); // bu satır çok mantıklı değil ama şimdilik böyle kalsın
        consoleClear();
        highscoretable.printToConsole();

        isLost = true;
    }

    static void consoleClear() {
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 60; j++) {
                cn.getTextWindow().output(j, i, ' ');
            }
            cn.getTextWindow().output(0, i, '\n');
        }

    }


    static void menu() {
        Scanner scanner = new Scanner(System.in);

        cn.getTextWindow().setCursorPosition(17, 2);
        cn.getTextWindow().output("WELCOME TO CHAIN GAME!\n", pink);

        cn.getTextWindow().setCursorPosition(18, 4);
        cn.getTextWindow().output("1 - ", yellow);
        cn.getTextWindow().setCursorPosition(18, 6);
        cn.getTextWindow().output("2 - ", yellow);
        cn.getTextWindow().setCursorPosition(18, 8);
        cn.getTextWindow().output("3 - ", yellow);
        cn.getTextWindow().setCursorPosition(18, 10);
        cn.getTextWindow().output("4 - ", yellow);
        cn.getTextWindow().setCursorPosition(18, 12);
        cn.getTextWindow().output("5 - ", yellow);

        cn.getTextWindow().setCursorPosition(22, 4);
        cn.getTextWindow().output("How to play?");
        cn.getTextWindow().setCursorPosition(22, 6);
        cn.getTextWindow().output("Chain Rules");
        cn.getTextWindow().setCursorPosition(22, 8);
        cn.getTextWindow().output("Keyboard Controls");
        cn.getTextWindow().setCursorPosition(22, 10);
        cn.getTextWindow().output("Start Game");
        cn.getTextWindow().setCursorPosition(22, 12);
        cn.getTextWindow().output("HighScore Table\n\n");


        int choice = Integer.parseInt(scanner.next());
        while (choice != 4) {
            switch (choice) {
                case 1:
                    consoleClear();
                    howToPlay();
                    initText();
                    break;
                case 2:
                    consoleClear();
                    chainRules();
                    initText();
                    break;
                case 3:
                    consoleClear();
                    keyboardControls();
                    initText();
                    break;
                case 5:
                    consoleClear();
                    highScoreTable();
                    break;

            }
            choice = scanner.nextInt();
            scanner.nextLine();
        }

        // seed sorulma kısmı buraya yazılacak
        

        consoleClear();

    }

    static void initText() {
        cn.getTextWindow().setCursorPosition(10, 11);
        cn.getTextWindow().output("-------------------------------------", gray);

        cn.getTextWindow().setCursorPosition(18, 13);
        cn.getTextWindow().output("1 - ", yellow);
        cn.getTextWindow().setCursorPosition(18, 15);
        cn.getTextWindow().output("2 - ", yellow);
        cn.getTextWindow().setCursorPosition(18, 17);
        cn.getTextWindow().output("3 - ", yellow);
        cn.getTextWindow().setCursorPosition(18, 19);
        cn.getTextWindow().output("4 - ", yellow);
        cn.getTextWindow().setCursorPosition(18, 21);
        cn.getTextWindow().output("5 - ", yellow);

        cn.getTextWindow().setCursorPosition(22, 13);
        cn.getTextWindow().output("How to play?");
        cn.getTextWindow().setCursorPosition(22, 15);
        cn.getTextWindow().output("Chain Rules");
        cn.getTextWindow().setCursorPosition(22, 17);
        cn.getTextWindow().output("Keyboard Controls");
        cn.getTextWindow().setCursorPosition(22, 19);
        cn.getTextWindow().output("Start Game");
        cn.getTextWindow().setCursorPosition(22, 21);
        cn.getTextWindow().output("HighScoreTable\n");
    }

    static void howToPlay() {

        cn.getTextWindow().setCursorPosition(23, 1);
        cn.getTextWindow().output("HOW TO PLAY?\n", pink);
        cn.getTextWindow().setCursorPosition(3, 3);
        cn.getTextWindow().output("Strive to reach the highest score!");
        cn.getTextWindow().setCursorPosition(3, 4);
        cn.getTextWindow().output("Earn your spot on the highscore table!");
        cn.getTextWindow().setCursorPosition(3, 5);
        cn.getTextWindow().output("But there are chain rules that you need to pay attention");
        cn.getTextWindow().setCursorPosition(3, 6);
        cn.getTextWindow().output("to!");
        cn.getTextWindow().setCursorPosition(6, 6);
        cn.getTextWindow().output("(Press 2 to see chain rules)\n", gray);


    }

    static void chainRules() {
        cn.getTextWindow().setCursorPosition(23, 1);
        cn.getTextWindow().output("CHAIN RULES", pink);


        cn.getTextWindow().output(5, 3, '-', gray);
        cn.getTextWindow().output(5, 4, '-', gray);
        cn.getTextWindow().output(5, 6, '-', gray);


        cn.getTextWindow().setCursorPosition(7, 3);
        cn.getTextWindow().output("There must be only one chain in each round");
        cn.getTextWindow().setCursorPosition(7, 4);
        cn.getTextWindow().output("Chain with more than one part, broken chains,");
        cn.getTextWindow().setCursorPosition(7, 5);
        cn.getTextWindow().output("wrong positioned plus signs are prohibited");
        cn.getTextWindow().setCursorPosition(7, 6);
        cn.getTextWindow().output("Difference between neighbor squares ");
        cn.getTextWindow().setCursorPosition(7, 7);
        cn.getTextWindow().output("in the chain must be 1 (+1 or -1).");
        cn.getTextWindow().setCursorPosition(7, 8);
        cn.getTextWindow().output("The number of squares in the chain must be at ");
        cn.getTextWindow().setCursorPosition(7, 9);
        cn.getTextWindow().output("least 4.");


    }

    static void keyboardControls() {
        cn.getTextWindow().setCursorPosition(20, 1);
        cn.getTextWindow().output("KEYBOARD CONTROLS\n", pink);
        cn.getTextWindow().setCursorPosition(10, 4);
        cn.getTextWindow().output("W ", yellow);
        cn.getTextWindow().setCursorPosition(10, 6);
        cn.getTextWindow().output("A ", yellow);
        cn.getTextWindow().setCursorPosition(10, 8);
        cn.getTextWindow().output("S ", yellow);
        cn.getTextWindow().setCursorPosition(10, 10);
        cn.getTextWindow().output("D ", yellow);

        cn.getTextWindow().setCursorPosition(12, 4);
        cn.getTextWindow().output("Up");
        cn.getTextWindow().setCursorPosition(12, 6);
        cn.getTextWindow().output("Left");
        cn.getTextWindow().setCursorPosition(12, 8);
        cn.getTextWindow().output("Down");
        cn.getTextWindow().setCursorPosition(12, 10);
        cn.getTextWindow().output("Right\n\n");

        cn.getTextWindow().setCursorPosition(23, 4);
        cn.getTextWindow().output("ENTER ", yellow);
        cn.getTextWindow().setCursorPosition(23, 6);
        cn.getTextWindow().output("SPACE ", yellow);
        cn.getTextWindow().setCursorPosition(23, 8);
        cn.getTextWindow().output("E ", yellow);

        cn.getTextWindow().setCursorPosition(29, 4);
        cn.getTextWindow().output("End of the round");
        cn.getTextWindow().setCursorPosition(29, 6);
        cn.getTextWindow().output("Insert/remove +");
        cn.getTextWindow().setCursorPosition(25, 8);
        cn.getTextWindow().output("End of the game");
    }

    static void highScoreTable() {
        consoleClear();
        highscoretable.printToConsole();
    }




}

