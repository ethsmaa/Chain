import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import enigma.core.Enigma;

public class DoubleLinkedList {
	private static Node head;
	private static Node tail;
    private static int size;
    
    public static enigma.console.Console cn = Enigma.getConsole("Chain", 60, 22, 20, 0);

    
    public DoubleLinkedList() {
    	head = null;
    	tail = null;
    	size = 0;
    }

    private class Node {
        private Player player;
        private Node previous;
        private Node next;

        public Node(Player player) {
            this.player = player;
        }
    }
    

    public void addPlayer(Player player) {
        Node newNode = new Node(player);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current != null && player.getScore() <= current.player.getScore()) {
                current = current.next;
            }
            if (current == head) {
                newNode.next = head;
                head.previous = newNode;
                head = newNode;
            } else if (current == null) {
                Node previous = head;
                while (previous.next != null) {
                    previous = previous.next;
                }
                previous.next = newNode;
                newNode.previous = previous;
            } else {
                newNode.next = current;
                newNode.previous = current.previous;
                current.previous.next = newNode;
                current.previous = newNode;
            }
        }
        size++;
        
    }

    public void printToConsole() {
    	int satirNo = 0;
    	try (BufferedReader reader = new BufferedReader(new FileReader("highscore.txt"))) {
            String satir;
            while ((satir = reader.readLine()) != null) {
            	cn.getTextWindow().setCursorPosition(0, satirNo);
                cn.getTextWindow().output(satir);
                satirNo += 2;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    	/*
        Node current = head;
        while (current != null) {
            System.out.println("Player: " + current.player.getName() + ", Score: " + current.player.getScore());
            current = current.next;
        }
        */
    }

    public void saveToFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName,true))) {
            Node current = head;
            while (current != null) {
                writer.write("Player: " + current.player.getName() + ", Score: " + current.player.getScore());
                writer.newLine();
                current = current.next;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	    
}
