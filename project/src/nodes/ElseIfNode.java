package nodes;

import base.Node;

import java.util.Random;

public class ElseIfNode extends Node {
    public ElseIfNode(Node parent, String content, boolean canBeCrossed) {
        super(parent, content, canBeCrossed);
        this.minDepth = 2;
    }
    @Override
    public void generateChildren() {
        Random random = new Random();
        int nextInt;
        if(root.maxDepth - this.depth <= 1) nextInt = random.nextInt(2);
        else nextInt = random.nextInt(3);
        switch (nextInt){
            case 0: // Only one block to if // min depth 1
                this.children.add(new BlockNode(this, "if_block", true));
                break;
            case 1: // Unconditional else // min depth 1
                this.children.add(new BlockNode(this, "if_block", true));
                this.children.add(new Token(this, "else_token", "else "));
                this.children.add(new BlockNode(this, "else_block", true));
                break;
            case 2: // ElseIf // min depth 4
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
