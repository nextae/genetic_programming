package nodes;

import base.Node;


public class DeclarationNode extends Node {
    public DeclarationNode(Node parent, String name, boolean canBeCrossed) {
        super(parent, name, canBeCrossed);
    }
    public DeclarationNode(Node parent, String name, boolean canBeCrossed, boolean generateChildren) {
        super(parent, name, canBeCrossed, generateChildren);
    }

    @Override
    public void generateChildren() {
        String type = switch (random.nextInt(3)) {
            case 1 -> "float";
            case 2 -> "bool";
            default -> "int";
        };
        String variable = String.format("x%d", this.root.variables.size() + 1);

        this.children.add(new TypeNode(this, type, true));
        this.children.add(new VariableNode(this, variable, true));
        this.children.add(new ExprNode(this, "expr", true));

        this.root.variables.add(variable);
    }

    @Override
    public String toString() {
        return String.format("%s %s = %s", this.children.get(0), this.children.get(1), this.children.get(2));
    }

    @Override
    public DeclarationNode clone(Node parent) {
        DeclarationNode clone = new DeclarationNode(parent, name, canBeCrossed, false);
        for(Node child: children) {
            clone.children.add(child.clone(clone));
        }
        return clone;
    }
}
