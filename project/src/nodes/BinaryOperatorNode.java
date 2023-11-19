package nodes;

import base.Node;

import java.util.Random;

public class BinaryOperatorNode extends Node {

    String operator;

    public BinaryOperatorNode(Node parent, String name, boolean canBeCrossed) {
        super(parent, name, canBeCrossed);
        this.minDepth = 0;
    }

    @Override
    public void generateChildren() {
        Random random = new Random();
        switch (random.nextInt(14)){
            case 0:
                this.operator = " + ";
            case 1:
                this.operator = " - ";
            case 2:
                this.operator = " * ";
            case 3:
                this.operator = " / ";
            case 4:
                this.operator = " ^ ";
            case 5:
                this.operator = " % ";
            case 6:
                this.operator = " > ";
            case 7:
                this.operator = " < ";
            case 8:
                this.operator = " == ";
            case 9:
                this.operator = " >= ";
            case 10:
                this.operator = " <= ";
            case 11:
                this.operator = " != ";
            case 12:
                this.operator = " and ";
            case 13:
                this.operator = " or ";

        }
    }

    @Override
    public String toString() {
        return this.operator;
    }
}
