package nodes;

import base.Node;


public class IfNode extends Node {
    public IfNode(Node parent, String content, boolean canBeCrossed) {
        super(parent, content, canBeCrossed);
    }

    public IfNode(Node parent, String name, boolean canBeCrossed, boolean generateChildren) {
        super(parent, name, canBeCrossed, generateChildren);
    }

    @Override
    public void generateChildren() {
        this.children.add(new Token(this, "if_token", "if "));
        this.children.add(new ExprNode(this, "if_condition", true));
        this.children.add(new ElseIfNode(this, "else_if_block", true));
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();
        for (Node child : children)
            text.append(child.toString());
        return text.toString();
    }

    @Override
    public IfNode clone(Node parent) {
        IfNode clone = new IfNode(parent, name, canBeCrossed, false);
        for(Node child: children) {
            clone.children.add(child.clone(clone));
        }
        return clone;
    }
}
