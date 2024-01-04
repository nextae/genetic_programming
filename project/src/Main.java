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
        Solver solver = new Solver(
            Input.fromFile("src/empty.txt"),
            3000,
            3,
            5,
            100,
            Fitnesses.ex1_1_B()
        );
        solver.solve();

//        ProgramOutput output = App.run("""
//                {
//                    if (((!x3) and input())){
//                        print(x19);
//                    } else if x2{
//                        print((787));
//                        while (input()){
//                            print(x5);
//                        }
//                    } else if x11{
//                        print(input());
//                        {
//                            x13 = !x10;
//                            print(5312.133);
//                            bool x26 = input();
//                        }
//                        x4 = !x29;
//                        if !!!x22 >= true * !!637.63965 and (input()) <= (!(-2432 + 4414.132) < -7665.709) + 1409.5488 < x4{
//                            print(input());
//                        }
//                    } else if x3{
//                        bool x31 = -2725;
//                        if -6763{
//                            print(-7406);
//                        }
//                        {
//                            print(input() and (!x18));
//                            int x32 = !input() == !(input());
//                            bool x33 = !false;
//                        }
//                        while input() > x19{
//                            print((!(x7)) != x14);
//                        }
//                        {
//                            print(input());
//                            x32 = true;
//                        }
//                    } else {
//                        while x24{
//                            x24 = (-5004.3833 == x1) > !input();
//                        }
//                        while input(){
//                            x15 = !x5;
//                            x16 = 1107;
//                        }
//                    }
//                    if !!-8435.518 >= -6786 != !true{
//                        print(x5);
//                        x7 = x4;
//                        if !!!(x10) >= true * !!637.63965 and (input()) <= ((!7742 < (-9831.828) - -7391 or false != !(input()) < -311) < -7665.709) + 1409.5488 < x4{
//                            x14 = !(x18) or false - (input());
//                            print(!!(input()));
//                        }
//                    } else {
//                        if -6763{
//                            print(-7406);
//                        }
//                        while x7{
//                            x11 = x27;
//                        }
//                        bool x13 = input();
//                        {
//                            print(!-788.2783);
//                            x1 = input();
//                        }
//                        while x5{
//                            print((-408.417));
//                            print(!!x26 * 9991.02 + !input());
//                            print(!!780 + -2468.8325 / !282);
//                        }
//                    }
//                }""", Input.fromFile("src/empty.txt"));
//
//        for (List<Integer> outputList : output.outputs) {
//            for (int o : outputList) {
//                System.out.printf("%d ", o);
//            }
//            System.out.println();
//        }
    }
}