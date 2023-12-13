package interpreter.expression.library;

import interpreter.expression.toplevel.Expr;
import org.antlr.v4.runtime.Token;

public class Input extends Expr {
    public Token token;
    public Input(Token token) {
        this.token = token;
    }

    @Override
    public String evaluate() {
        return null;
    }
}
