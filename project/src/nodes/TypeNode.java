package nodes;

import base.Node;

public class TypeNode extends Node {
    public TypeNode(Node parent, String name, boolean canBeCrossed) {
        super(parent, name, canBeCrossed);
    }

    public TypeNode(Node parent, String name, boolean canBeCrossed, boolean generateChildren) {
        super(parent, name, canBeCrossed, generateChildren);
    }

    @Override
    public void generateChildren() {}

    @Override
    public String toString() {
        return name;
    }

    @Override
    public TypeNode clone(Node parent) {
        TypeNode clone = new TypeNode(parent, name, canBeCrossed, false);

        for (Node child : children)
            clone.children.add(child.clone(clone));

        return clone;
    }
}
