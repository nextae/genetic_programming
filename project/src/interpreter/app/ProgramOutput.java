package interpreter.app;

import java.util.List;

public class ProgramOutput {
    public boolean hasError;
    public List<String> outputs;


    public ProgramOutput(boolean hasError, List<String> outputs) {
        this.hasError = hasError;
        this.outputs = outputs;
    }
}
