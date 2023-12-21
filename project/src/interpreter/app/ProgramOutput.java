package interpreter.app;

import java.util.List;

public class ProgramOutput {
    public boolean hasError;
    public List<Integer> outputs;

    public ProgramOutput(boolean hasError, List<Integer> outputs) {
        this.hasError = hasError;
        this.outputs = outputs;
    }
}
