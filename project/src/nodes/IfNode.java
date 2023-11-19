package nodes;

import base.Node;

import java.util.Random;

public class IfNode extends Node {
    public IfNode(Node parent, String content, boolean canBeCrossed) {
        super(parent, content, canBeCrossed);
        this.minDepth = 3;
    }

    @Override
    public void generateChildren() {
        this.children.add(new Token(this, "if_token", "if "));
        this.children.add(new ExprNode(this, "if_condition", true));
        this.children.add(new ElseIfNode(this, "else_if_block", true));
    }
}
