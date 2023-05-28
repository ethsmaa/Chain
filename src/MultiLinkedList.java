public class MultiLinkedList {
    private RoundNode head;

    public void addRound(Object data) {
        RoundNode newNode = new RoundNode(data);
        if (head == null)
            head = newNode;
        else {
            RoundNode temp = head;
            while (temp.getDown() != null)
                temp = temp.getDown();
            temp.setDown(newNode);
        }
    }

    public void addChain(Object round, Object chain) {
        if (head == null) {
            System.out.println("add a round before adding an item!");
        } else {
            RoundNode temp = head;
            while (temp != null) {
                if (temp.getData().equals(round)) {
                    ChainNode temp2 = temp.getRight();
                    if (temp2 == null) {
                        temp2 = new ChainNode(chain);
                        temp.setRight(temp2);
                    } else {
                        while (temp2.getNext() != null)
                            temp2 = temp2.getNext();
                        ChainNode newNode = new ChainNode(chain);
                        temp2.setNext(newNode);
                    }
                }
                temp = temp.getDown();
            }

        }
    }

    public int sizeRound() {
        int count = 0;
        if (head == null)
            System.out.println("linked list is empty");
        else {
            RoundNode temp = head;
            while (temp != null) {
                count++;
                temp = temp.getDown();
            }
        }
        return count;
    }

    public int sizeChain() {
        int count = 0;
        if (head == null)
            System.out.println("linked list is empty");
        else {
            RoundNode temp = head;
            while (temp != null) {
                ChainNode temp2 = temp.getRight();
                while (temp2 != null) {
                    count++;
                    temp2 = temp2.getNext();
                }
                temp = temp.getDown();
            }
        }
        return count;
    }

    // display chain of spesific round
    public void displayChain(Object round) {
        if (head == null)
            System.out.println("linked list is empty");
        else {
            RoundNode temp = head;
            while (temp != null) {
                if (temp.getData().equals(round)) {
                    ChainNode temp2 = temp.getRight();
                    while (temp2 != null) {
                        if(temp2 == temp.getRight()) // ilk eleman için + koyma
                            System.out.print(temp2.getData());
                        else
                            System.out.print("+" + temp2.getData());
                        temp2 = temp2.getNext();
                    }
                }
                temp = temp.getDown();
            }
        }
    }
}





