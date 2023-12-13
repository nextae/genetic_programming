package interpreter.expression.blocks;

import interpreter.expression.toplevel.Line;
import interpreter.expression.variables.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Block extends Line {
    public Block parent;
    public List<Line> lines;

    public Map<String, Value> values; //Temporary solution, not sure if it will stay


    public Block(Block parent, List<Line> lines) {
        this.parent = parent;
        this.lines = lines;
        values = new HashMap<>();
    }

    public Block(Block parent) {
        this.parent = parent;
        lines = new ArrayList<>();
        values = new HashMap<>();
    }
}
