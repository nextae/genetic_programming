package interpreter.expression.variables;

import interpreter.expression.toplevel.Expr;
import org.antlr.v4.runtime.Token;

public final class VarName extends Expr {
    public String id;
    public Token token;

    public VarName(String id, Token token) {
        this.id = id;
        this.token = token;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public String evaluate() {
        return null;
    }
}
