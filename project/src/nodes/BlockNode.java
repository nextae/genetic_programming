package nodes;

import base.Node;

public class BlockNode extends Node {
    public BlockNode(Node parent, String name, boolean canBeCrossed) {
        super(parent, name, canBeCrossed);
    }
    public BlockNode(Node parent, String name, boolean canBeCrossed, boolean generateChildren) {
        super(parent, name, canBeCrossed, generateChildren);
    }

    @Override
    public void generateChildren() {
        int counter = 0;
        switch(root.method) {
            case GROW:
                counter = 1;
                this.children.add(new LineNode(this, "block_instruction", true));
                while (random.nextInt(2) != 0 && counter < root.maxWidth) {
                    this.children.add(new LineNode(this, "block_instruction", true));
                    counter++;
                } break;
            case FULL:
                while (counter < root.maxWidth) {
                    this.children.add(new LineNode(this, "block_instruction", true));
                    counter++;
                } break;
        }
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();
        text.append("{\n");

        for (Node child : children)
            text.append("    ".repeat(child.indent)).append(child);

        if(this.name.equals("if_block")) text.append("    ".repeat(this.indent)).append("} ");
        else text.append("    ".repeat(this.indent)).append("}\n");
        return text.toString();
    }

    @Override
    public BlockNode clone(Node parent) {
        BlockNode clone = new BlockNode(parent, name, canBeCrossed, false);
        for(Node child: children) {
            clone.children.add(child.clone(clone));
        }
        return clone;
    }
}
