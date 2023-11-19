package nodes;

import base.Node;
import java.util.Random;

public class BlockNode extends Node {
    public BlockNode(Node parent, String name, boolean canBeCrossed) {
        super(parent, name, canBeCrossed);
        this.minDepth = 1;
        this.generateChildren();
    }

    @Override
    public void generateChildren() {
        Random random = new Random();
        while (random.nextInt(2) != 0) {
            this.children.add(new LineNode(this, "block_instruction", true));
        }
    }

    @Override
    public String toString() {
        return null;  // TODO: implement
    }
}
