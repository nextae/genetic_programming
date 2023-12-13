package interpreter.expression.variables;

import interpreter.expression.toplevel.Expr;
import interpreter.expression.toplevel.Statement;
import org.antlr.v4.runtime.Token;

public final class VarDeclaration extends Statement {
    public Variable variable;

    public VarDeclaration(String type, String id, Expr val, Token token){
        variable = new Variable(type, id, val, token);
    }

    public VarDeclaration(String type, String id, Token token){
        variable = new Variable(type, id, token);
    }

}
