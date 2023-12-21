package nodes;

import base.Node;

public class VariableNode extends Node {
    public VariableNode(Node parent, String name, boolean canBeCrossed) {
        super(parent, name, canBeCrossed);
    }

    @Override
    public void generateChildren() {}

    @Override
    public String toString() {
        return name;
    }
}
