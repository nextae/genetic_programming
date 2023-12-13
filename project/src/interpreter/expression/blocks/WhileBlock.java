package interpreter.expression.blocks;

import interpreter.expression.toplevel.Expr;
import interpreter.expression.toplevel.Line;
import org.antlr.v4.runtime.Token;

public final class WhileBlock extends Line {
    public Expr condition;
    public Block block;
    public Token token;

    public WhileBlock(Expr condition, Block block, Token token) {
        this.condition = condition;
        this.block = block;
        this.token = token;
    }
}
