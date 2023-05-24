public class ChainNode { // chaindeki elementlerin olduğu node
    private Object data;
    private ChainNode next;

    public ChainNode(Object data) {
        this.data = data;
        this.next = null;
    }

    public Object getData() {
        return this.data;
    }

    public ChainNode getNext() {
        return this.next;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setNext(ChainNode next) {
        this.next = next;
    }

}
