package interpreter.expression.arithmetic;

import interpreter.expression.toplevel.Expr;
import org.antlr.v4.runtime.Token;

public final class Modulo extends Expr {

    public Expr dividend;
    public Expr divisor;
    public Token token;

    public Modulo(Expr dividend, Expr divisor, Token token) {
        this.dividend = dividend;
        this.divisor = divisor;
        this.token = token;
    }

    @Override
    public String evaluate() {
        return null;
    }
}
