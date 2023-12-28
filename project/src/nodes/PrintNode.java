package nodes;

import base.Node;

public class PrintNode extends Node {
    public PrintNode(Node parent, String name, boolean canBeCrossed) {
        super(parent, name, canBeCrossed);
    }

    public PrintNode(Node parent, String name, boolean canBeCrossed, boolean generateChildren) {
        super(parent, name, canBeCrossed, generateChildren);
    }

    @Override
    public void generateChildren() {
        this.children.add(new ExprNode(this, "expr", true));
    }

    @Override
    public String toString() {
        return String.format("print(%s)", this.children.get(0));
    }

    @Override
    public PrintNode clone(Node parent) {
        PrintNode clone = new PrintNode(parent, name, canBeCrossed, false);

        for (Node child : children)
            clone.children.add(child.clone(clone));

        return clone;
    }
}
