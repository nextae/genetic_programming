package nodes;

import base.Node;

public class ValueNode extends Node {
    String value;
    public ValueNode(Node parent, String name, boolean canBeCrossed) {
        super(parent, name, canBeCrossed);
    }

    public ValueNode(Node parent, String name, boolean canBeCrossed, boolean generateChildren) {
        super(parent, name, canBeCrossed, generateChildren);
    }

    @Override
    public void generateChildren() { // TODO: Added hard-coded low and high bounds - to be changed!!!
        switch (random.nextInt(3)){
            case 0:
                value = String.valueOf(random.nextInt(-10000, 10000));
                break;
            case 1:
                value = String.valueOf(random.nextFloat(-10000, 10000));
                break;
            case 2:
                value = String.valueOf(random.nextBoolean());
                break;
        }
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public ValueNode clone(Node parent) {
        ValueNode clone = new ValueNode(parent, name, canBeCrossed, false);

        for (Node child : children)
            clone.children.add(child.clone(clone));

        clone.value = value;

        return clone;
    }
}
