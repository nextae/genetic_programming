package nodes;

import base.Node;


public class BinaryOperatorNode extends Node {

    private String operator;

    public BinaryOperatorNode(Node parent, String name, boolean canBeCrossed) {
        super(parent, name, canBeCrossed);
    }
    public BinaryOperatorNode(Node parent, String name, boolean canBeCrossed, boolean generateChildren) {
        super(parent, name, canBeCrossed, generateChildren);
    }

    @Override
    public void generateChildren() {
        switch (random.nextInt(12)){
            case 0:
                this.operator = " + ";
                break;
            case 1:
                this.operator = " - ";
                break;
            case 2:
                this.operator = " * ";
                break;
            case 3:
                this.operator = " / ";
                break;
            case 4:
                this.operator = " > ";
                break;
            case 5:
                this.operator = " < ";
                break;
            case 6:
                this.operator = " == ";
                break;
            case 7:
                this.operator = " >= ";
                break;
            case 8:
                this.operator = " <= ";
                break;
            case 9:
                this.operator = " != ";
                break;
            case 10:
                this.operator = " and ";
                break;
            case 11:
                this.operator = " or ";
                break;
        }
    }

    @Override
    public String toString() {
        return this.operator;
    }

    @Override
    public BinaryOperatorNode clone(Node parent) {
        BinaryOperatorNode clone = new BinaryOperatorNode(parent, name, canBeCrossed, false);
        clone.operator = operator;
        for(Node child: children) {
            clone.children.add(child.clone(clone));
        }
        return clone;
    }
}
