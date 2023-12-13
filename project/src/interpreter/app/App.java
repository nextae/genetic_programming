package interpreter.app;

import interpreter.antlr.HelloLexer;
import interpreter.antlr.HelloParser;
import interpreter.expression.interpreter.AntlrToProgram;
import interpreter.expression.interpreter.ExpressionProcessor;
import interpreter.expression.toplevel.Program;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;
import java.util.List;

public final class App {
    public static void main(String[] args) {
        if(args.length != 1){
            System.err.println("Error: no filename given");
        } else {
            String fileName = args[0];
            HelloParser parser = getParser(fileName);

            ParseTree antlrAST = parser.start();
            AntlrToProgram progVisitor = new AntlrToProgram();
            Program prog = progVisitor.visit(antlrAST);

            if(progVisitor.semanticErrors.isEmpty()){
                ExpressionProcessor ep = new ExpressionProcessor(prog.lines);
                List<String> evaluations = ep.getEvalResults(null);
                if(ExpressionProcessor.semanticErrors.isEmpty())
                for(String eval: evaluations) {
                    System.out.println(eval);
                } else for(String err : ExpressionProcessor.semanticErrors) System.out.println(err);
            } else for(String err : progVisitor.semanticErrors) System.out.println(err);
        }
    }

    private static HelloParser getParser(String filename){
        HelloParser parser = null;

        try {
            CharStream input = CharStreams.fromFileName(filename);
            HelloLexer lexer = new HelloLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            parser = new HelloParser(tokens);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return parser;
    }
}
