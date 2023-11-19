import java.util.ArrayList;
import java.util.List;

public abstract class Node {
    Node parent;
    String name;
    List<Node> children;
    boolean canBeCrossed;
    int depth;
    int minDepth;

    public Node(Node parent, String name, boolean canBeCrossed) {
        this.parent = parent;
        this.name = name;
        this.canBeCrossed = canBeCrossed;
        this.children = new ArrayList<>();
        this.depth = parent.depth + 1;
    }

    public abstract void generateChildren();
    public String getText() {
        StringBuilder text = new StringBuilder();
        for (Node child : children)
            text.append(child.getText());

        return text.toString();
    }
}
