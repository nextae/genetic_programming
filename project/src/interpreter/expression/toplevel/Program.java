package interpreter.expression.toplevel;

import java.util.ArrayList;
import java.util.List;

public final class Program {
    public List<Line> lines;

    public Program(){
        lines = new ArrayList<>();
    }

    public void addLine(Line l){
        if(l instanceof Statement s){
            lines.addAll(s.statements);
        }
        lines.add(l);
    }
}
