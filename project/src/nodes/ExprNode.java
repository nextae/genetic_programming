package nodes;

import base.Node;


public class ExprNode extends Node {
    public ExprNode(Node parent, String name, boolean canBeCrossed) {
        super(parent, name, canBeCrossed);
    }
    @Override
    public void generateChildren() {
        int lowerBound = 0;
        if(root.variables.isEmpty()) lowerBound = 1;
        int nextInt = random.nextInt(lowerBound, 6);

        switch (nextInt){
            case 0: // varname // min depth 0
                String variable = this.root.variables.get(random.nextInt(this.root.variables.size()));
                this.children.add(new VariableNode(this, variable, true));
                break;
            case 1: // min depth 0
                this.children.add(new ValueNode(this, "expr_value", true));
                break;
            case 2: // min depth 0
                this.children.add(new InputNode(this, "expr_input", true));
                break;
            case 3: // () // min depth 1
                this.children.add(new Token(this, "expr_l_bracket", "("));
                this.children.add(new ExprNode(this, "expr", true));
                this.children.add(new Token(this, "expr_r_bracket", ")"));
                break;
            case 4: // expr + expr // min depth 1
                this.children.add(new ExprNode(this, "left_expr", true));
                this.children.add(new BinaryOperatorNode(this, "binary_operator", false));
                this.children.add(new ExprNode(this, "left_expr", true));
                break;
            case 5: // !expr // min depth 2
                this.children.add(new NegationNode(this, "expr_negation", true));
                break;
        }
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();
        if(this.children.isEmpty()) text.append(this.name);
        else for (Node child : children)
            text.append(child.toString());
        return text.toString();
    }
}
