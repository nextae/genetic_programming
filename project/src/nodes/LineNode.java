package nodes;
import base.Node;

import java.util.Random;

public class LineNode extends Node{
    public static int minDepth;

    public LineNode(Node parent, String name, boolean canBeCrossed) {
        super(parent, name, canBeCrossed);
    }

    @Override
    public void generateChildren() {
        Random random = new Random();
        switch (random.nextInt(5)){
            case 0:
                children.add(new StatementNode(this, "instruction", true));
            case 1:
                children.add(new BlockNode(this, "code_block", true));
            case 2:
                children.add(new IfNode(this, "if_block",true));
            case 3:
                children.add(new WhileNode(this, "while_block", true));
        }
    }

}
