package nodes;

import base.Node;

public class AssignmentNode extends Node {
    public AssignmentNode(Node parent, String name, boolean canBeCrossed) {
        super(parent, name, canBeCrossed);
    }
    public AssignmentNode(Node parent, String name, boolean canBeCrossed, boolean generateChildren) {
        super(parent, name, canBeCrossed, generateChildren);
    }
    @Override
    public void generateChildren() {
        if(this.root.variables.isEmpty()) {
            this.root.variables.add("x0"); // Safety precautions
        }
        String variable = this.root.variables.get(random.nextInt(this.root.variables.size()));
        this.children.add(new VariableNode(this, variable, true));
        this.children.add(new ExprNode(this, "expr", true));

    }

    @Override
    public String toString() {
        return String.format("%s = %s", this.children.get(0), this.children.get(1));
    }

    @Override
    public AssignmentNode clone(Node parent) {
        AssignmentNode clone = new AssignmentNode(parent, name, canBeCrossed, false);
        for(Node child: children) {
            clone.children.add(child.clone(clone));
        }
        return clone;
    }
}
