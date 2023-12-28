package nodes;

import base.Node;

public class InputNode extends Node {
    public InputNode(Node parent, String name, boolean canBeCrossed, boolean generateChildren) {
        super(parent, name, canBeCrossed, generateChildren);
    }

    @Override
    public InputNode clone(Node parent) {
        InputNode clone = new InputNode(parent, name, canBeCrossed, false);
        for(Node child: children) {
            clone.children.add(child.clone(clone));
        }
        return clone;
    }

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
