import enigma.core.Enigma;
import enigma.event.TextMouseEvent;
import enigma.event.TextMouseListener;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;


public class Chain {
    public enigma.console.Console cn = Enigma.getConsole("Chain", 50, 26, 20, 0);
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
    static int row = 19;
    static int column = 31;


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
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (i % 2 != 1 && j % 2 != 1) {
                    map[i][j] = (char) (random.nextInt(4) + 1);
                } else
                    map[i][j] = 0;
            }
        }


        printMap();

        while (true) {

            if (mousepr == 1) {  // if mouse button pressed

                //! + nın eklendiği yer arrayin sınırlarını geçmemeli !1! henüz bu kontrol yok

                if (isSquareEmpty(mousex, mousey) && (mousex % 2 != 1 || mousey % 2 != 1)) {
                    cn.getTextWindow().output(mousex, mousey, '+');  // write a char to x,y position without changing cursor position
                    map[mousey][mousex] = '+'; // arraye ekle
                }

                mousepr = 0;     // last action

            }


            if (keypr == 1) {    // if keyboard button pressed

                if (rkey == KeyEvent.VK_ENTER) {
                    Chain();
                    //  bu fonksiyon çalıştığında oluşturulan
                    // zincir SLL'ye eklenir. zincirdeki elemanlar
                    // yerine . yazılır.
                    // zincir kontrolü henüz yazılmadı.

                }


                if (rkey == KeyEvent.VK_E) {
                }
                keypr = 0;    // last action
            }



            // mapin altına print


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


     int[] findTail() { // bu fonksiyon mapte etrafında sadece 1 tane + işareti olan karenin koordinatlarını döndürür
        int coordinatesofTail[] = new int[2];
        boolean isTailFound = false;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (i % 2 != 1 && j % 2 != 1 && plusDirectionCount(j, i) == 1) { // sadece sayılara bakılıyor
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

     int plusDirectionCount(int x, int y) { // karenin etrafındaki + sayısı
        int count = 0;
        if (x + 1 < 31 && map[y][x + 1] == '+') count++; // sağ
        if (x - 1 > 0 && map[y][x - 1] == '+') count++; // sol
        if (y + 1 < 19 && map[y + 1][x] == '+') count++; // alt
        if (y - 1 > 0 && map[y - 1][x] == '+') count++; // üst
        return count;
    }

     int plusCount() {
        int count = 0;
        for (int i = 0; i < row; i++) { // mapin içindeki + sayısı
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

         chain.add(map[i][j]);
         map[i][j] = '.';
         cn.getTextWindow().output(j, i, '.');

         int chainLength = plusCount() + 1; // zincirdeki eleman sayısı

        for (int m = 1; m < chainLength; m++) { // zincirin tüm elemanları dolaşılana kadar
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
            map[i][j] = '.';
            cn.getTextWindow().output(j, i, '.');
        }
        return chain;
    }

}


