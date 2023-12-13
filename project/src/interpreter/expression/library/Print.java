package interpreter.expression.library;

import interpreter.expression.toplevel.Expr;
import interpreter.expression.toplevel.Statement;
import org.antlr.v4.runtime.Token;

public final class Print extends Statement {
    public String id;
    public Expr expr;
    public Token token;

    public Print(String id, Expr expr) {
        this.id = id;
        this.expr = expr;
    }

    public Print(String id, Expr expr, Token token) {
        this.id = id;
        this.expr = expr;
        this.token = token;
    }
}
