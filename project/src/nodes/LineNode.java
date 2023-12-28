package nodes;
import base.Node;

public class LineNode extends Node{

    public LineNode(Node parent, String name, boolean canBeCrossed) {
        super(parent, name, canBeCrossed);
    }

    public LineNode(Node parent, String name, boolean canBeCrossed, boolean generateChildren) {
        super(parent, name, canBeCrossed, generateChildren);
    }

    @Override
    public void generateChildren() {
        int lowerBound = 0;
        if(root.maxDepth - this.indent <= 0) lowerBound = 3;
        int nextInt = random.nextInt(lowerBound, 4);
        switch (nextInt){
            case 0:
                children.add(new IfNode(this, "if_block",true));
                break;
            case 1:
                children.add(new BlockNode(this, "code_block", true));
                break;
            case 2:
                children.add(new WhileNode(this, "while_block", true));
                break;
            case 3: // Only result that doesn't generate more depth
                children.add(new StatementNode(this, "instruction", true));
                break;
        }
    }

    @Override
    public String toString() {
        return this.children.get(0).toString();
    }

    @Override
    public LineNode clone(Node parent) {
        LineNode clone = new LineNode(parent, name, canBeCrossed, false);
        for(Node child: children) {
            clone.children.add(child.clone(clone));
        }
        return clone;
    }
}
