public class RoundNode { // sütunlar gibi düşünebiliriz
    private Object data;
    private RoundNode down;
    private ChainNode right;

    public RoundNode(Object data) {
        this.data = data;
        this.down = null;
        this.right = null;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public RoundNode getDown() {
        return down;
    }

    public void setDown(RoundNode down) {
        this.down = down;
    }

    public ChainNode getRight() {
        return right;
    }

    public void setRight(ChainNode right) {
        this.right = right;
    }






}
