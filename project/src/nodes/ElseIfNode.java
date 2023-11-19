package nodes;

import base.Node;

import java.util.Random;

public class ElseIfNode extends Node {
    public ElseIfNode(Node parent, String content, boolean canBeCrossed) {
        super(parent, content, canBeCrossed);
        this.minDepth = 2;
        generateChildren();
    }
    @Override
    public void generateChildren() {
        Random random = new Random();
        switch (random.nextInt(3)){
            case 0: // Only one block to if
                this.children.add(new BlockNode(this, "if_block", true));
                break;
            case 1: // Unconditional else
                this.children.add(new BlockNode(this, "if_block", true));
                this.children.add(new Token(this, "else_token", "else "));
                this.children.add(new BlockNode(this, "else_block", true));
                break;
            case 2: // ElseIf
                this.children.add(new BlockNode(this, "if_block", true));
                this.children.add(new Token(this, "else_token", "else "));
                this.children.add(new IfNode(this, "elseIf_block", true));
                break;
        }

    }

    @Override
    public String toString() { // TODO: make this better, indents
        StringBuilder text = new StringBuilder();
        for (Node child : children)
            text.append(child.toString());
        return text.toString();
    }
}
