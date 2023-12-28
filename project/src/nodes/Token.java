package nodes;

import base.Node;

public class Token extends Node {
    String token;

    public Token(Node parent, String name, String token) {
        super(parent, name, false);
        this.token = token;
    }

    public Token(Node parent, String name, String token, boolean generateChildren) {
        super(parent, name, false, generateChildren);
        this.token = token;
    }

    @Override
    public void generateChildren() {}

    @Override
    public String toString() {
        return token;
    }

    @Override
    public Token clone(Node parent) {
        Token clone = new Token(parent, name, token, false);

        for (Node child : children)
            clone.children.add(child.clone(clone));

        return clone;
    }
}
