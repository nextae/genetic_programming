package nodes;

import base.Node;


public class WhileNode extends Node {
    public WhileNode(Node parent, String content, boolean canBeCrossed) {
        super(parent, content, canBeCrossed);
        this.minDepth = 2;
    }

    @Override
    public void generateChildren() {
        this.children.add(new Token(this, "while_token", "while "));
        this.children.add(new ExprNode(this, "while_condition", true));
        this.children.add(new BlockNode(this, "while_block", true));
    }

    @Override
    public String toString() { // TODO: make this better, indents
        StringBuilder text = new StringBuilder();
        for (Node child : children)
            text.append(child.toString());
        return text.toString();
    }
}
