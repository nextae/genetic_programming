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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public final class App {
    public static ProgramOutput run(String program, List<List<Integer>> inputs) {
        if(program.isEmpty())
            return new ProgramOutput(true, null, null);

        HelloParser parser = getParser(program);
        ParseTree antlrAST = parser.start();
        AntlrToProgram progVisitor = new AntlrToProgram();
        Program prog = progVisitor.visit(antlrAST);

        List<List<Integer>> outputs = new ArrayList<>();

        ExpressionProcessor ep = new ExpressionProcessor(prog.lines, new ArrayList<>());
        for (List<Integer> inputList : inputs) {
            ep.reset(prog.lines, inputList);
            List<Integer> evaluations = ep.getEvalResults(null).stream().filter(s -> !s.isEmpty()).map(Integer::parseInt).toList();
            outputs.add(evaluations);
        }

        return new ProgramOutput(!ExpressionProcessor.semanticErrors.isEmpty(), inputs, outputs);
    }

    private static HelloParser getParser(String program){
        HelloParser parser;

        //            CharStream input = CharStreams.fromFileName(filename);
        CharStream input = CharStreams.fromString(program);
        HelloLexer lexer = new HelloLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        parser = new HelloParser(tokens);

        return parser;
    }
}
