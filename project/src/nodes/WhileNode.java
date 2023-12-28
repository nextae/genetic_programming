package nodes;

import base.Node;


public class WhileNode extends Node {
    public WhileNode(Node parent, String content, boolean canBeCrossed) {
        super(parent, content, canBeCrossed);
    }

    public WhileNode(Node parent, String content, boolean canBeCrossed, boolean generateChildren) {
        super(parent, content, canBeCrossed, generateChildren);
    }

    @Override
    public void generateChildren() {
        this.children.add(new Token(this, "while_token", "while "));
        this.children.add(new ExprNode(this, "while_condition", true));
        this.children.add(new BlockNode(this, "while_block", true));
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();
        for (Node child : children)
            text.append(child.toString());
        return text.toString();
    }

    @Override
    public WhileNode clone(Node parent) {
        WhileNode clone = new WhileNode(parent, name, canBeCrossed, false);

        for (Node child : children)
            clone.children.add(child.clone(clone));

        return clone;
    }
}
