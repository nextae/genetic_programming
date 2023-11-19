package nodes;

import base.Node;

import java.util.Random;

public class WhileNode extends Node {
    public WhileNode(Node parent, String content, boolean canBeCrossed) {
        super(parent, content, canBeCrossed);
        this.minDepth = 2;
    }

    @Override
    public void generateChildren() {
        Random random = new Random();
        this.children.add(new Token(this, "while_token", "while "));
        this.children.add(new ExprNode(this, "while_condition", true));
        this.children.add(new BlockNode(this, "while_block", true));
    }
}
