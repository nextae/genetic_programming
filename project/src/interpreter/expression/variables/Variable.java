package interpreter.expression.variables;

import interpreter.expression.toplevel.Expr;
import org.antlr.v4.runtime.Token;

public final class Variable extends Expr {
    public String type;
    public String id;
    public Expr value;
    public Token token;

    public Variable(String type, String id, Expr value, Token token) {
        this.id = id;
        this.type = type;
        this.value = value;
        this.token = token;
    }

    public Variable(String type, String id, Token token){
        this.id = id;
        this.type = type;
        this.token = token;
        this.value = null;
    }

    @Override
    public String toString() {
        return evaluate();
    }

    @Override
    public String evaluate() {
        return value.evaluate();
    }
}
