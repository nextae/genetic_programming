package interpreter.expression.variables;

import interpreter.expression.toplevel.Expr;
import interpreter.expression.toplevel.Statement;
import org.antlr.v4.runtime.Token;

public final class Assignment extends Statement {
    public String id;
    public Expr expr;
    public Token token;

    public Assignment(String id, Expr expr, Token token) {
        this.id = id;
        this.expr = expr;
        this.token = token;
    }
}
