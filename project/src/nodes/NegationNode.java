package nodes;

import base.Node;

public class NegationNode extends Node {
    public NegationNode(Node parent, String name, boolean canBeCrossed) {
        super(parent, name, canBeCrossed);
        this.minDepth = 2;
    }
    @Override
    public void generateChildren() {
        this.children.add(new ExprNode(this, "negated_expr", true));
    }

    @Override
    public String toString() {
        return "!" + this.children.get(0).toString();
    }
}
