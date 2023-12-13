package interpreter.expression.logic;

import interpreter.expression.toplevel.Expr;
import org.antlr.v4.runtime.Token;

public final class Negation extends Expr {
    public Expr expr;
    public Token token;

    public Negation(Expr expr, Token token) {
        this.expr = expr;
        this.token = token;
    }

    @Override
    public String evaluate() {
        return null;
    }
}
