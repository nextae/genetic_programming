import genetic.Fitnesses;
import genetic.Solver;
import interpreter.app.App;
import interpreter.app.ProgramOutput;
import nodes.Program;

import java.io.FileNotFoundException;
import java.util.List;

public class Main {


    public static void main(String[] args) throws FileNotFoundException {
//        Program p0 = new Program(100);
//        System.out.println(p0);
//        ProgramOutput output = App.run(p0.toString());
//        if (output.hasError) System.out.println("#---------RUNTIME ERROR---------#");
//        for(String eval:output.outputs) System.out.println(eval);

//        Program p1 = new Program(3);
//        Program p2 = new Program(3);
//        Solver.mutation(p1);

        Solver solver = new Solver("src/input.txt", 10000, 3, 3, 100);
        solver.solve();

//        ProgramOutput output = App.run("while (-5020 >= !3424 or (!input()) / 321.9619) < input() or !((2473 or !1836)) * !(-7101.3096){\n" +
//                "    while (input()){\n" +
//                "        while (-5020 >= (input()) / 321.9619) < input() or !(x1) * !x1{\n" +
//                "            while (input()){\n" +
//                "                while (input()){\n" +
//                "                    float x1 = (true);\n" +
//                "                }\n" +
//                "                while (x1){\n" +
//                "                    x2 = input();\n" +
//                "                }\n" +
//                "                {\n" +
//                "                    print(input());\n" +
//                "                }\n" +
//                "            }\n" +
//                "        }\n" +
//                "        while (x1){\n" +
//                "            x1 = input();\n" +
//                "        }\n" +
//                "        {\n" +
//                "            print(input());\n" +
//                "        }\n" +
//                "    }\n" +
//                "}", solver.inputs);
//
//        for (List<Integer> outputList : output.outputs) {
//            for (int o : outputList) {
//                System.out.printf("%d ", o);
//            }
//            System.out.println();
//        }
//        System.out.println(Fitnesses.ex1_4_B(output.inputs, output.outputs));
    }
}