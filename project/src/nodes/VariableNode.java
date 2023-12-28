package nodes;

import base.Node;

public class VariableNode extends Node {
    public VariableNode(Node parent, String name, boolean canBeCrossed) {
        super(parent, name, canBeCrossed);
    }

    public VariableNode(Node parent, String name, boolean canBeCrossed, boolean generateChildren) {
        super(parent, name, canBeCrossed, generateChildren);
    }

    @Override
    public void generateChildren() {}

    @Override
    public String toString() {
        return name;
    }

    @Override
    public VariableNode clone(Node parent) {
        VariableNode clone = new VariableNode(parent, name, canBeCrossed, false);

        for (Node child : children)
            clone.children.add(child.clone(clone));

        return clone;
    }
}
