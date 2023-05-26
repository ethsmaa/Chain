import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class DoubleLinkedList {
	private static Node head;
	private static Node tail;
    private static int size;

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
            while (current != null && player.score <= current.player.getScore()) {
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

    public static void printToConsole() {
        Node current = head;
        while (current != null) {
            System.out.println("Player: " + current.player.getName() + ", Score: " + current.player.getScore());
            current = current.next;
        }
    }

    public static void saveToFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
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
