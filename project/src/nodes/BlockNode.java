package nodes;

import base.Node;
import java.util.Random;

public class BlockNode extends Node {
    public BlockNode(Node parent, String name, boolean canBeCrossed) {
        super(parent, name, canBeCrossed);
        this.minDepth = 1;
    }

    @Override
    public void generateChildren() {
        Random random = new Random();
        if(root.maxDepth - this.depth >= 1) while (random.nextInt(2) != 0) {
            this.children.add(new LineNode(this, "block_instruction", true));
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
