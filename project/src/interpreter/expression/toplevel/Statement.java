package interpreter.expression.toplevel;

import java.util.ArrayList;
import java.util.List;

public class Statement extends Line{
    //Maybe it should have function execute() or smth like that?
    public List<Line> statements;

    public Statement(){
        statements = new ArrayList<>();
    }
}
