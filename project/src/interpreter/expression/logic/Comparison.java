package interpreter.expression.logic;

import interpreter.expression.toplevel.Expr;
import org.antlr.v4.runtime.Token;

public final class Comparison extends Expr {
    public Expr left;
    public String operator;
    public Expr right;
    public Token token;

    public Comparison(Expr left, String operator, Expr right, Token token) {
        this.left = left;
        this.operator = operator;
        this.right = right;
        this.token = token;
    }

    @Override
    public String evaluate() {
        return null;
    }
}
