package interpreter.expression.interpreter;

import interpreter.antlr.HelloBaseVisitor;
import interpreter.antlr.HelloParser;
import interpreter.expression.arithmetic.*;
import interpreter.expression.blocks.Block;
import interpreter.expression.blocks.ElseIfBlock;
import interpreter.expression.blocks.IfBlock;
import interpreter.expression.blocks.WhileBlock;
import interpreter.expression.library.Input;
import interpreter.expression.library.Print;
import interpreter.expression.logic.Combination;
import interpreter.expression.logic.Comparison;
import interpreter.expression.logic.Negation;
import interpreter.expression.toplevel.Expr;
import interpreter.expression.toplevel.Line;
import interpreter.expression.variables.*;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.List;

public final class AntlrToExpression extends HelloBaseVisitor<Line> {
    private final List<String> varNames;
    private final List<String> semanticErrors;

    public AntlrToExpression(List<String> semanticErrors){
        this.semanticErrors = semanticErrors;
        varNames = new ArrayList<>();
    }

    @Override
    public VarName visitVariable(HelloParser.VariableContext ctx) {
        Token idToken = ctx.VARNAME().getSymbol();
        String id = ctx.VARNAME().getText();
        return new VarName(id, idToken);
    }

    @Override
    public Expr visitINT(HelloParser.INTContext ctx) {
        return new Value(ctx.getText());
    }

    @Override
    public Expr visitFLOAT(HelloParser.FLOATContext ctx) {
        return new Value(ctx.getText());
    }

    @Override
    public Expr visitBOOL(HelloParser.BOOLContext ctx) {
        return new Value(ctx.getText());
    }

    @Override
    public Expr visitValue(HelloParser.ValueContext ctx) {
        if (ctx.val() instanceof HelloParser.INTContext) return visitINT((HelloParser.INTContext) ctx.val());
        else if (ctx.val() instanceof HelloParser.FLOATContext) return visitFLOAT((HelloParser.FLOATContext) ctx.val());
        else return visitBOOL((HelloParser.BOOLContext) ctx.val());
    }

    @Override
    public VarDeclaration visitAssignDeclare(HelloParser.AssignDeclareContext ctx) {
        Token idToken = ctx.VARNAME().getSymbol();
        String id = ctx.VARNAME().getText();
        String type = ctx.TYPE().getText();
        varNames.add(id);
        return new VarDeclaration(type, id, (Expr)visit(ctx.expr()), idToken);
    }

    @Override
    public Assignment visitAssign(HelloParser.AssignContext ctx) {
        Token idToken = ctx.VARNAME().getSymbol();
        String id = ctx.VARNAME().getText();
        return new Assignment(id, (Expr) visit(ctx.getChild(2)), idToken);
    }

    @Override
    public Addition visitAdditive(HelloParser.AdditiveContext ctx) {
        Token token = ctx.getStart();
        return new Addition((Expr) visit((ctx.getChild(0))), ctx.getChild(1).getText(), (Expr) visit(ctx.getChild(2)), token);
    }

    @Override
    public Multiplication visitMultiplicative(HelloParser.MultiplicativeContext ctx) {
        Token token = ctx.getStart();
        return new Multiplication((Expr) visit((ctx.getChild(0))), ctx.getChild(1).getText(), (Expr) visit(ctx.getChild(2)), token);
    }

    @Override
    public Power visitPower(HelloParser.PowerContext ctx) {
        Token token = ctx.getStart();
        return new Power((Expr) visit((ctx.getChild(0))), (Expr) visit(ctx.getChild(2)), token);
    }

    @Override
    public Modulo visitModulo(HelloParser.ModuloContext ctx) {
        Token token = ctx.getStart();
        return new Modulo((Expr) visit(ctx.expr(0)), (Expr) visit(ctx.expr(1)), token);
    }

    @Override
    public Negation visitNegation(HelloParser.NegationContext ctx) {
        Token token = ctx.getStart();
        return new Negation((Expr) visit(ctx.expr()), token);
    }

    @Override
    public Combination visitCombination(HelloParser.CombinationContext ctx) {
        Token token = ctx.getStart();
        return new Combination((Expr) visit(ctx.expr(0)), ctx.getChild(1).getText(), (Expr) visit(ctx.expr(1)), token);
    }

    @Override
    public Comparison visitCompare(HelloParser.CompareContext ctx) {
        Token token = ctx.getStart();
        return new Comparison((Expr) visit(ctx.expr(0)), ctx.getChild(1).getText(), (Expr) visit(ctx.expr(1)), token);
    }

    @Override
    public Line visitStatement(HelloParser.StatementContext ctx) {
        return visit(ctx.getChild(0));
    }

    @Override
    public Line visitLine(HelloParser.LineContext ctx) {
        return visit(ctx.getChild(0));
    }

    @Override
    public Block visitBlock(HelloParser.BlockContext ctx) {
        Block block = new Block(null);
        AntlrToExpression expr = new AntlrToExpression(semanticErrors);
        for(var line : ctx.line()){
            block.lines.add(expr.visit(line));
        }
        return block;
    }

    @Override
    public WhileBlock visitWhileBlock(HelloParser.WhileBlockContext ctx) {
        Token token = ctx.getStart();
        return new WhileBlock((Expr) visit(ctx.expr()), visitBlock(ctx.block()), token);
    }

    @Override
    public IfBlock visitIfBlock(HelloParser.IfBlockContext ctx) {
        Token token = ctx.getStart();
        IfBlock block = new IfBlock((Expr) visit(ctx.getChild(1)), token);
        ElseIfBlock elseBlock = (ElseIfBlock) visit(ctx.getChild(2));
        block.elseBlock = elseBlock;
        elseBlock.parentBlock = block;
        return block;
    }

    @Override
    public ElseIfBlock visitBlockIf(HelloParser.BlockIfContext ctx) {
        return new ElseIfBlock(visitBlock(ctx.block()));
    }

    @Override
    public ElseIfBlock visitElse(HelloParser.ElseContext ctx) {
        Block ifBlock = (Block) visit(ctx.block(0));
        Block elseBlock = (Block) visit(ctx.block(1));
        return new ElseIfBlock(ifBlock, elseBlock);
    }

    @Override
    public ElseIfBlock visitElseIf(HelloParser.ElseIfContext ctx) {
        Block ifBlock = (Block) visit(ctx.block());
        IfBlock elseIf = (IfBlock) visit(ctx.ifBlock());
        return new ElseIfBlock(ifBlock, elseIf);
    }

    @Override
    public Line visitBrackets(HelloParser.BracketsContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Print visitPrint(HelloParser.PrintContext ctx) {

        if(ctx.getChild(2) instanceof HelloParser.VariableContext vc) {
            Token idToken = vc.VARNAME().getSymbol();
            String id = ctx.getChild(2).getText();
            return new Print(id, null, idToken);
        }
        else if (ctx.getChild(2) instanceof HelloParser.ExprContext e) return new Print(null, (Expr) visit(e));

        semanticErrors.add("Error: wrong print function ("+ctx.LBRACKET().getSymbol().getLine()+")"); //I guess
        return new Print(null, null);
    }

    @Override
    public Line visitInput(HelloParser.InputContext ctx) {
        if(ctx.getChild(0) instanceof HelloParser.StdinContext std){
            return new Input(std.LBRACKET().getSymbol());
        } else throw new RuntimeException("Problem with input - this shouldn't happen");
    }
}
