package nodes;

import base.Node;

public class AssignmentNode extends Node {
    public AssignmentNode(Node parent, String name, boolean canBeCrossed) {
        super(parent, name, canBeCrossed);
    }

    @Override
    public void generateChildren() {
        String variable = this.root.variables.get(random.nextInt(this.root.variables.size()));

        this.children.add(new VariableNode(this, variable, true));
        this.children.add(new ExprNode(this, "expr", true));
    }

    @Override
    public String toString() {
        return String.format("%s = %s", this.children.get(0), this.children.get(1));
    }
}
