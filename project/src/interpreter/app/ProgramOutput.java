package interpreter.app;

import java.util.List;

public class ProgramOutput {
    public boolean hasError;
    public List<Integer> outputs;
    public List<Integer> inputs;

    public ProgramOutput(boolean hasError, List<Integer> inputs, List<Integer> outputs) {
        this.hasError = hasError;
        this.inputs = inputs;
        this.outputs = outputs;
    }
}