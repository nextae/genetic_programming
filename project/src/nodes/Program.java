package nodes;

import base.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Program extends Node {
    public int maxDepth;
    public List<String> variables;

    public Program(int maxDepth) {
        super();
        this.root = this;
        this.maxDepth = maxDepth;
        this.variables = new ArrayList<>();
        generateChildren();
    }

    @Override
    public void generateChildren() {
        Random random = new Random();

        while(random.nextInt(4) != 0) {
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
