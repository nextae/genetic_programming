package nodes;

import base.Node;

public class ValueNode extends Node {
    String value;
    public ValueNode(Node parent, String name, boolean canBeCrossed) {
        super(parent, name, canBeCrossed);
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
}
