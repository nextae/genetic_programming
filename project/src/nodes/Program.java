package nodes;

import base.Node;
import genetic.generationMethods;

import java.util.ArrayList;
import java.util.List;

public class Program extends Node {
    public int maxDepth;
    public int maxWidth;
    public List<String> variables;
    public double fitness = 0;
    public generationMethods method;

    public Program(int maxDepth, int maxWidth) {
        super();
        this.root = this;
        this.maxDepth = maxDepth;
        this.maxWidth = maxWidth;
        this.variables = new ArrayList<>();
        generateChildren();
    }

    public Program(int maxDepth, int maxWidth, generationMethods method) {
        super();
        this.root = this;
        this.maxDepth = maxDepth;
        this.maxWidth = maxWidth;
        this.variables = new ArrayList<>();
        this.method = method;
        generateChildren();
    }

    public Program(int maxDepth, int maxWidth, boolean generateChildren, List<String> variables) {
        super();
        this.root = this;
        this.maxDepth = maxDepth;
        this.maxWidth = maxWidth;
        this.variables = new ArrayList<>(variables);

        if (generateChildren)
            generateChildren();
    }

    @Override
    public void generateChildren() {
        int counter = 0;
        switch(method) {
            case GROW:
                counter = 1;
                this.children.add(new LineNode(this, "line", true));
                while (random.nextInt(5) != 0 && counter < maxWidth) {
                    counter++;
                    this.children.add(new LineNode(this, "line", true));
                } break;
            case FULL:
                while (counter < maxWidth) {
                    counter++;
                    this.children.add(new LineNode(this, "line", true));
                } break;
        }
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();
        for (Node child : children)
            text.append(child.toString());

        return text.toString();
    }

    @Override
    public Program clone(Node parent) {
        Program clone = new Program(maxDepth, maxWidth, false, variables);
        clone.parent = clone;
        clone.root = clone;
        clone.method = method;

        for (Node child : children)
            clone.children.add(child.clone(clone));

        return clone;
    }
}
