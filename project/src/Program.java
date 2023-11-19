import java.util.Random;

public class Program extends Node {
    public int maxDepth;

    public Program(int maxDepth) {
        super();
        this.root = this;
        this.maxDepth = maxDepth;
    }

    @Override
    public void generateChildren() {
        Random random = new Random();

        while(random.nextInt(3) != 0) {
            // Add line
        }
    }

    @Override
    public String getText() {
        StringBuilder text = new StringBuilder();
        for (Node child : children)
            text.append(child.getText());

        return text.toString();
    }
}
