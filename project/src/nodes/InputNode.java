package nodes;

import base.Node;

public class InputNode extends Node {
    public InputNode(Node parent, String name, boolean canBeCrossed) {
        super(parent, name, canBeCrossed);
    }
    @Override
    public void generateChildren() {

    }

    @Override
    public String toString() {
        return "input()";
    }
}
