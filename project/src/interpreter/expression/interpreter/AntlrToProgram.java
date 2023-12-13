package interpreter.expression.interpreter;

import interpreter.antlr.HelloParser;
import interpreter.antlr.HelloBaseVisitor;
import interpreter.expression.toplevel.Program;

import java.util.ArrayList;
import java.util.List;

public final class AntlrToProgram extends HelloBaseVisitor<Program> {
    public List<String> semanticErrors;
    @Override
    public Program visitStart(HelloParser.StartContext ctx) {
        Program prog = new Program();

        semanticErrors = new ArrayList<>();
        AntlrToExpression lineVisitor = new AntlrToExpression(semanticErrors);
        for(int i = 0; i < ctx.getChildCount(); i++){
            if(i != ctx.getChildCount() - 1){ //childCount() - 1 == EOF
                prog.addLine(lineVisitor.visit(ctx.getChild(i)));
            }
        }
        return prog;
    }
}
