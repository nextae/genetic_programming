package nodes;

import base.Node;

public class Token extends Node {
    String token;

    public Token(Node parent, String name, String token) {
        super(parent, name, false);
        this.token = token;
    }

    @Override
    public void generateChildren() {}

    @Override
    public String toString() {
        return token;
    }
}
