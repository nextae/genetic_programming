package nodes;

import base.Node;


public class BinaryOperatorNode extends Node {

    private String operator;

    public BinaryOperatorNode(Node parent, String name, boolean canBeCrossed) {
        super(parent, name, canBeCrossed);
    }

    @Override
    public void generateChildren() {
        switch (random.nextInt(14)){
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
                this.operator = " ^ ";
                break;
            case 5:
                this.operator = " % ";
                break;
            case 6:
                this.operator = " > ";
                break;
            case 7:
                this.operator = " < ";
                break;
            case 8:
                this.operator = " == ";
                break;
            case 9:
                this.operator = " >= ";
                break;
            case 10:
                this.operator = " <= ";
                break;
            case 11:
                this.operator = " != ";
                break;
            case 12:
                this.operator = " and ";
                break;
            case 13:
                this.operator = " or ";
                break;
        }
    }

    @Override
    public String toString() {
        return this.operator;
    }
}
