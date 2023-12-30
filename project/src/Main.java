import genetic.Fitnesses;
import genetic.Input;
import genetic.Solver;
import interpreter.app.App;
import interpreter.app.ProgramOutput;
import nodes.Program;

import java.io.FileNotFoundException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        Solver solver = new Solver(Input.truthTable(2), 2000, 3, 5, 200);
        solver.solve();

//        ProgramOutput output = App.run("{\n" +
//                "    if (input() * 9971.646){\n" +
//                "        bool x18 = (x13) > !!input() <= !(726) * input() <= (x15) < (x9);\n" +
//                "        float x22 = (((input())));\n" +
//                "        int x9 = input() and true or (x5);\n" +
//                "    } else {\n" +
//                "        x17 = x10;\n" +
//                "        float x5 = input();\n" +
//                "    }\n" +
//                "    if x1 < !true or !x1 != !-6628{\n" +
//                "        while input(){\n" +
//                "            print(input());\n" +
//                "        }\n" +
//                "        x3 = false;\n" +
//                "        while false{\n" +
//                "            x1 = x1;\n" +
//                "            float x14 = input() / input();\n" +
//                "        }\n" +
//                "        x1 = !(x3);\n" +
//                "        print(-792);\n" +
//                "    }\n" +
//                "    if -8389{\n" +
//                "        {\n" +
//                "            print(input());\n" +
//                "            print(true);\n" +
//                "            x1 = !(x3);\n" +
//                "            float x14 = 3360.7588;\n" +
//                "        }\n" +
//                "        int x6 = false;\n" +
//                "    } else if input(){\n" +
//                "        x4 = true * 5640;\n" +
//                "        while ((input()) < (input())){\n" +
//                "            x13 = -3102.3442 / !true <= input();\n" +
//                "        }\n" +
//                "    } else {\n" +
//                "        bool x17 = x6;\n" +
//                "    }\n" +
//                "}\n" +
//                "{\n" +
//                "    if (x1){\n" +
//                "        if x6 - !false{\n" +
//                "            x3 = input();\n" +
//                "        } else {\n" +
//                "            float x14 = 3360.7588;\n" +
//                "            float x9 = (!input());\n" +
//                "        }\n" +
//                "        {\n" +
//                "            x7 = input();\n" +
//                "            x8 = input();\n" +
//                "        }\n" +
//                "    } else if input() == x9 < !!!-3243{\n" +
//                "        x8 = !input() + input() and !(-4070);\n" +
//                "        {\n" +
//                "            x7 = x4;\n" +
//                "            bool x13 = x11;\n" +
//                "        }\n" +
//                "        if (x5 * (-6527) or 6287.6357 - input()){\n" +
//                "            x2 = input();\n" +
//                "        }\n" +
//                "    } else if input(){\n" +
//                "        while (!input() / x3 != true and input()) == !-9510.136 * ((input())){\n" +
//                "            float x1 = input();\n" +
//                "        }\n" +
//                "        {\n" +
//                "            bool x12 = input();\n" +
//                "            float x14 = input() / input();\n" +
//                "            x11 = 4015;\n" +
//                "            print(!!!(!x7) <= !!input() >= (352));\n" +
//                "        }\n" +
//                "        print(-792);\n" +
//                "        while (-8662.224){\n" +
//                "            x7 = x4;\n" +
//                "            int x16 = (input());\n" +
//                "            print(x6);\n" +
//                "            x4 = input() <= (input()) / (1722.3564);\n" +
//                "        }\n" +
//                "        if (x5 * (-6527) or 6287.6357 - input()){\n" +
//                "            x2 = input();\n" +
//                "        }\n" +
//                "    } else if !!!4473.338 >= x16 == ((!input())) / (!x15) or !!input() < (input()) != input() - 3727.5576 != 9065 > x9{\n" +
//                "        print(input());\n" +
//                "        int x6 = false;\n" +
//                "        if -4623{\n" +
//                "            x6 = -2279.3604;\n" +
//                "            x2 = 5053;\n" +
//                "            print(((input())));\n" +
//                "        }\n" +
//                "    } else {\n" +
//                "        int x21 = !!(x11);\n" +
//                "        print(((-9458)));\n" +
//                "    }\n" +
//                "    print(input());\n" +
//                "    while x7{\n" +
//                "        if -4623{\n" +
//                "            x6 = -2279.3604;\n" +
//                "            bool x3 = !input();\n" +
//                "            print(x4);\n" +
//                "        }\n" +
//                "    }\n" +
//                "    {\n" +
//                "        int x7 = x5;\n" +
//                "        float x11 = !true;\n" +
//                "        x6 = input();\n" +
//                "    }\n" +
//                "    {\n" +
//                "        {\n" +
//                "            x6 = x1;\n" +
//                "            float x11 = !true;\n" +
//                "            x6 = -2279.3604;\n" +
//                "        }\n" +
//                "        {\n" +
//                "            x2 = 5053;\n" +
//                "            int x1 = !9857.213;\n" +
//                "        }\n" +
//                "        bool x12 = input();\n" +
//                "        x9 = (8261.764);\n" +
//                "    }\n" +
//                "}", solver.inputs);
//
//        for (List<Integer> outputList : output.outputs) {
//            for (int o : outputList) {
//                System.out.printf("%d ", o);
//            }
//            System.out.println();
//        }
//        System.out.println(Fitnesses.smallest(output.inputs, output.outputs));
    }
}