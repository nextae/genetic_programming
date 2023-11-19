package nodes;

import base.Node;

import java.util.Random;

public class StatementNode extends Node {
    public StatementNode(Node parent, String name, boolean canBeCrossed) {
        super(parent, name, canBeCrossed);
        this.minDepth = 1;
        generateChildren();
    }

    @Override
    public void generateChildren() {
        Random random = new Random();
        switch (random.nextInt(3)){
            case 0:
                this.children.add(new DeclarationNode(this, "declaration", true));
                break;
            case 1:
                if (this.root.variables.isEmpty())
                    this.children.add(new DeclarationNode(this, "declaration", true));
                else
                    this.children.add(new AssignmentNode(this, "assignment", true));
                break;
            case 2:
                this.children.add(new PrintNode(this, "print",true));
                break;
        }
    }

    @Override
    public String toString() {
        return String.format("%s;\n", this.children.get(0));
    }
}
