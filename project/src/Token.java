public class Token extends Node {
    String token;

    public Token(Node parent, String name, String token) {
        super(parent, name, false);
        this.token = token;
        this.minDepth = 0;
    }

    @Override
    public void generateChildren() {}

    @Override
    public String getText() {
        return token;
    }
}
