import genetic.Solver;
import interpreter.app.App;
import interpreter.app.ProgramOutput;
import nodes.Program;

public class Main {


    public static void main(String[] args) {
//        Program p0 = new Program(100);
//        System.out.println(p0);
//        ProgramOutput output = App.run(p0.toString());
//        if (output.hasError) System.out.println("#---------RUNTIME ERROR---------#");
//        for(String eval:output.outputs) System.out.println(eval);

//        Program p1 = new Program(3);
//        Program p2 = new Program(3);
//        Solver.mutation(p1);

        Solver solver = new Solver("src/input.txt", 30, 2, 20);
        solver.solve();
    }
}