import enigma.core.Enigma;
import enigma.event.TextMouseEvent;
import enigma.event.TextMouseListener;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;


public class Chain {
    public enigma.console.Console cn = Enigma.getConsole("Chain", 40, 20, 20, 0);
    public TextMouseListener tmlis;
    public KeyListener klis;

    // ------ Standard variables for mouse and keyboard ------
    public int mousepr;          // mouse pressed?
    public int mousex, mousey;   // mouse text coords.
    public int keypr;   // key pressed?
    public int rkey;    // key   (for press/release)
    // ----------------------------------------------------

    // map burada tanımlanır
    static char[][] map = new char[19][31];
    Random random = new Random();


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

        // map burada doldurulur
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 31; j++) {
                if (i % 2 == 0 && j % 2 == 0) {
                    int element = random.nextInt(4) + 1;
                    map[i][j] = Integer.toString(element).charAt(0);
                } else
                    map[i][j] = ' ';
            }
        }

        //print map
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                cn.getTextWindow().output(j, i, map[i][j]);
            }
        }


        while (true) {

            if (mousepr == 1) {  // if mouse button pressed
                if(isSquareEmpty(mousex, mousey)) {
                    cn.getTextWindow().output(mousex, mousey, '+');  // write a char to x,y position without changing cursor position
                    map[mousey][mousex] = '+'; // arraye ekle
                }

                    mousepr = 0;     // last action

            }


            // + yon tuslariyla hareket edecekse asagidaki kod
            /*
            if (keypr == 1) {    // if keyboard button pressed

                if (rkey == KeyEvent.VK_ENTER) {
                }

                if (rkey == KeyEvent.VK_E) {
                }
                keypr = 0;    // last action
              }
             */

            Thread.sleep(20);
        }

    }

    static boolean isSquareEmpty(int x, int y) {
    	if(map[y][x] == ' ') return true;
    	else return false;

    }

}


