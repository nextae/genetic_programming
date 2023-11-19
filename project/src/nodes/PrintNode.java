package nodes;

import base.Node;
import nodes.ExprNode;

public class PrintNode extends Node {
    public PrintNode(Node parent, String name, boolean canBeCrossed) {
        super(parent, name, canBeCrossed);
        this.minDepth = 1;
        generateChildren();
    }

    @Override
    public void generateChildren() {
        this.children.add(new ExprNode(this, "expr", true));
    }

    @Override
    public String toString() {
        return String.format("print(%s)", this.children.get(0));
    }
}
