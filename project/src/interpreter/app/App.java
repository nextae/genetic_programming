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
    public static ProgramOutput run(String program, String path) {
        String inputDelimiter = " ";
        
        if(program.isEmpty()){
            System.err.println("Error: no program given");
            return new ProgramOutput(true, null, null);
        } else {
//            String fileName = args[0];
            HelloParser parser = getParser(program);
            ParseTree antlrAST = parser.start();
            AntlrToProgram progVisitor = new AntlrToProgram();
            Program prog = progVisitor.visit(antlrAST);

            File file = new File(path);
            Scanner scanner = null;
            try {
                scanner = new Scanner(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            scanner.useDelimiter(inputDelimiter);
            List<Integer> inputList = new ArrayList<>();
            while(scanner.hasNext()) inputList.add(scanner.nextInt());

            ExpressionProcessor ep = new ExpressionProcessor(prog.lines, inputList);
            List<Integer> evaluations = ep.getEvalResults(null).stream().filter(s -> !s.isEmpty()).map(Integer::parseInt).collect(Collectors.toList());

            return new ProgramOutput(!ExpressionProcessor.semanticErrors.isEmpty(), inputList, evaluations);

//            if(progVisitor.semanticErrors.isEmpty()){
//                ExpressionProcessor ep = new ExpressionProcessor(prog.lines, inputFilePath, inputDelimeter);
//                List<String> evaluations = ep.getEvalResults(null);
//                if(ExpressionProcessor.semanticErrors.isEmpty())
//                    for(String eval: evaluations) {
//                        System.out.println(eval);
//                } else for(String err : ExpressionProcessor.semanticErrors) System.out.println(err);
//            } else for(String err : progVisitor.semanticErrors) System.out.println(err);
        }
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
