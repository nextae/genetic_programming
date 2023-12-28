package nodes;

import base.Node;


public class ElseIfNode extends Node {
    public ElseIfNode(Node parent, String content, boolean canBeCrossed) {
        super(parent, content, canBeCrossed);
    }
    public ElseIfNode(Node parent, String content, boolean canBeCrossed, boolean generateChildren) {
        super(parent, content, canBeCrossed, generateChildren);
    }
    @Override
    public void generateChildren() {
        int nextInt = random.nextInt(3);
        switch (nextInt){
            case 0: // Only one block to if // min depth 1
                this.children.add(new BlockNode(this, "if_block_last", true));
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
    public String toString() {
        StringBuilder text = new StringBuilder();
        for (Node child : children)
            text.append(child.toString());
        return text.toString();
    }

    @Override
    public ElseIfNode clone(Node parent) {
        ElseIfNode clone = new ElseIfNode(parent, name, canBeCrossed, false);
        for(Node child: children) {
            clone.children.add(child.clone(clone));
        }
        return clone;
    }
}
