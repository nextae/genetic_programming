package nodes;

import base.Node;

public class NegationNode extends Node {
    public NegationNode(Node parent, String name, boolean canBeCrossed) {
        super(parent, name, canBeCrossed);
    }

    public NegationNode(Node parent, String name, boolean canBeCrossed, boolean generateChildren) {
        super(parent, name, canBeCrossed, generateChildren);
    }

    @Override
    public void generateChildren() {
        this.children.add(new ExprNode(this, "negated_expr", true));
    }

    @Override
    public String toString() {
        return "!" + this.children.get(0).toString();
    }

    @Override
    public NegationNode clone(Node parent) {
        NegationNode clone = new NegationNode(parent, name, canBeCrossed, false);

        for (Node child : children)
            clone.children.add(child.clone(clone));

        return clone;
    }
}
