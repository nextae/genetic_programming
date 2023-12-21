package nodes;

import base.Node;


public class StatementNode extends Node {
    public StatementNode(Node parent, String name, boolean canBeCrossed) {
        super(parent, name, canBeCrossed);
    }

    @Override
    public void generateChildren() {
        switch (random.nextInt(3)){
            case 0: // min depth 1
                this.children.add(new DeclarationNode(this, "declaration", true));
                break;
            case 1: // min depth 1
                if (this.root.variables.isEmpty())
                    this.children.add(new DeclarationNode(this, "declaration", true));
                else
                    this.children.add(new AssignmentNode(this, "assignment", true));
                break;
            case 2: // min depth 1
                this.children.add(new PrintNode(this, "print", true));
                break;
        }
    }

    @Override
    public String toString() {
        return String.format("%s;\n", this.children.get(0));
    }
}
