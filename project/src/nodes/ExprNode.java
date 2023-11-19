package nodes;

import base.Node;

import java.util.Random;

public class ExprNode extends Node {
    public ExprNode(Node parent, String name, boolean canBeCrossed) {
        super(parent, name, canBeCrossed);
        this.minDepth = 1;
    }
    @Override
    public void generateChildren() {
        Random random = new Random();
        switch (random.nextInt(6)){
            case 0: // varname // min depth 0
                this.children.add(new VariableNode(this, "expr_varname", true));
                break;
            case 1: // () // min depth 1
                this.children.add(new Token(this, "expr_l_bracket", "("));
                this.children.add(new ExprNode(this, "expr", true));
                this.children.add(new Token(this, "expr_r_bracket", ")"));
                break;
            case 2: // expr + expr // min depth 1
                this.children.add(new ExprNode(this, "left_expr", true));
                this.children.add(new BinaryOperatorNode(this, "binary_operator", false));
                this.children.add(new ExprNode(this, "left_expr", true));
                break;
            case 3: // !expr // min depth 2
                this.children.add(new NegationNode(this, "expr_negation", true));
                break;
            case 4: // min depth 0
                this.children.add(new ValueNode(this, "expr_value", true));
                break;
            case 5: // min depth 0
                this.children.add(new InputNode(this, "expr_input", true));
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
