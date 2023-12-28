package nodes;

import base.Node;

import java.util.ArrayList;
import java.util.List;

public class Program extends Node {
    public int maxDepth;
    public int maxWidth;
    public List<String> variables;
    public double fitness = 0;

    public Program(int maxDepth, int maxWidth) {
        super();
        this.root = this;
        this.maxDepth = maxDepth;
        this.maxWidth = maxWidth;
        this.variables = new ArrayList<>();
        generateChildren();
    }

    @Override
    public void generateChildren() {
        this.children.add(new LineNode(this, "line", true));
        this.children.add(new LineNode(this, "line", true));
        while(random.nextInt(5) != 0) {
            this.children.add(new LineNode(this, "line", true));
        }
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();
        for (Node child : children)
            text.append(child.toString());

        return text.toString();
    }
}
