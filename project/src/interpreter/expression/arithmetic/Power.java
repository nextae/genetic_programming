package interpreter.expression.arithmetic;

import interpreter.expression.toplevel.Expr;
import org.antlr.v4.runtime.Token;

public final class Power extends Expr {
    public Expr left;
    public Expr right;
    public Token token;

    public Power(Expr left, Expr right, Token token) {
        this.left = left;
        this.right = right;
        this.token = token;
    }

    @Override
    public String evaluate() { //Not used anywhere, probably useless
        System.out.println("We've got a problem");
        int result = Integer.parseInt(left.evaluate());
        result = (int) Math.pow(result, Integer.parseInt(right.evaluate()));
        return Integer.toString(result);
    }
}
