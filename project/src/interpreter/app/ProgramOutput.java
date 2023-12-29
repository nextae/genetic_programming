package interpreter.app;

import java.util.List;

public class ProgramOutput {
    public boolean hasError;
    public List<List<Integer>> outputs;
    public List<List<Integer>> inputs;

    public ProgramOutput(boolean hasError, List<List<Integer>> inputs, List<List<Integer>> outputs) {
        this.hasError = hasError;
        this.inputs = inputs;
        this.outputs = outputs;
    }
}