package base;

import nodes.Program;

import java.util.ArrayList;
import java.util.List;

public abstract class Node {
    protected Node parent;
    protected Program root;
    protected String name;
    protected List<Node> children;
    protected boolean canBeCrossed;
    protected int depth;
    protected int minDepth;

    public Node() {
        this.parent = this;
        this.name = "root";
        this.canBeCrossed = false;
        this.children = new ArrayList<>();
        this.depth = 0;
    }

    public Node(Node parent, String name, boolean canBeCrossed) {
        this.parent = parent;
        this.name = name;
        this.canBeCrossed = canBeCrossed;
        this.children = new ArrayList<>();
        this.depth = parent.depth + 1;
        this.root = parent.root;
    }

    public abstract void generateChildren();
    public abstract String toString();
}
