package nodes;

import base.Node;

public class BlockNode extends Node {
    public BlockNode(Node parent, String name, boolean canBeCrossed) {
        super(parent, name, canBeCrossed);
    }

    @Override
    public void generateChildren() {
        int childrenCount = 0;
        while (random.nextInt(2) != 0 && childrenCount < root.maxWidth) {
            this.children.add(new LineNode(this, "block_instruction", true));
            childrenCount++;
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
}
