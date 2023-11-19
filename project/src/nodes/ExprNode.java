package nodes;

import base.Node;

public class ExprNode extends Node {  // TODO: implement
    public ExprNode(Node parent, String name, boolean canBeCrossed) {
        super(parent, name, canBeCrossed);
        this.minDepth = 1;
    }
    @Override
    public void generateChildren() {

    }

    @Override
    public String toString() {
        return name;
    }
}
