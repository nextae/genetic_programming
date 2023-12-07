package base;

import nodes.BlockNode;
import nodes.Program;

import java.util.ArrayList;
import java.util.List;

public abstract class Node {
    public Node parent;
    public Program root;
    protected String name;
    public List<Node> children;
    protected boolean canBeCrossed;
    protected int depth;
    protected int minDepth;
    public int indent;

    public Node() { // Program constructor
        this.parent = this;
        this.root = (Program)this; // Not sure if necessary
        this.name = "root";
        this.canBeCrossed = false;
        this.children = new ArrayList<>();
        this.depth = 0;
        this.indent = 0;
    }

    public Node(Node parent, String name, boolean canBeCrossed) {
        this.parent = parent;
        this.name = name;
        this.canBeCrossed = canBeCrossed;
        this.children = new ArrayList<>();
        this.depth = parent.depth + 1;
        this.root = parent.root;

        if(this.parent instanceof BlockNode) this.indent = this.parent.indent+1;
        else this.indent = this.parent.indent;

        generateChildren();
    }

    public abstract void generateChildren();
    public abstract String toString();
}
