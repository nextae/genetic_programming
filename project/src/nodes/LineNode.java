package nodes;
import base.Node;

import java.util.Random;

public class LineNode extends Node{

    public LineNode(Node parent, String name, boolean canBeCrossed) {
        super(parent, name, canBeCrossed);
        this.minDepth = 1;
    }

    @Override
    public void generateChildren() {
        Random random = new Random();
        switch (random.nextInt(4)){
            case 0: // min depth 1
                children.add(new StatementNode(this, "instruction", true));
                break;
            case 1: // min depth 1
                children.add(new BlockNode(this, "code_block", true));
                break;
            case 2: // min depth 3
                children.add(new IfNode(this, "if_block",true));
                break;
            case 3: // min depth 2
                children.add(new WhileNode(this, "while_block", true));
                break;
        }
    }

    @Override
    public String toString() {
        return this.children.get(0).toString();
    }

}
