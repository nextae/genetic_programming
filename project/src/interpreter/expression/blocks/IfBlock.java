package interpreter.expression.blocks;

import interpreter.expression.toplevel.Expr;
import interpreter.expression.toplevel.Line;
import org.antlr.v4.runtime.Token;

public final class IfBlock extends Line {

    public Expr condition;
    public ElseIfBlock elseBlock;
    public Token token;

    public IfBlock(Expr condition, ElseIfBlock elseBlock, Token token) {
        this.condition = condition;
        this.elseBlock = elseBlock;
        this.token = token;
    }

    public IfBlock(Expr condition, Token token) {
        this.condition = condition;
        this.token = token;
    }
}
