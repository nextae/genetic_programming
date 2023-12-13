package interpreter.expression.blocks;

import interpreter.expression.toplevel.Line;

public final class ElseIfBlock extends Line {
    public IfBlock parentBlock;
    public Block ifBlock;
    public Block elseBlock;
    public IfBlock child;

    public ElseIfBlock(IfBlock parentBlock, Block ifBlock, Block elseBlock, IfBlock child) {
        this.parentBlock = parentBlock;
        this.ifBlock = ifBlock;
        this.elseBlock = elseBlock;
        this.child = child;
    }

    public ElseIfBlock(Block ifBlock) {
        this.ifBlock = ifBlock;
    }

    public ElseIfBlock(Block ifBlock, Block elseBlock) {
        this.ifBlock = ifBlock;
        this.elseBlock = elseBlock;
    }

    public ElseIfBlock(Block ifBlock, IfBlock child) {
        this.ifBlock = ifBlock;
        this.child = child;
    }
}
