package genetic;

import nodes.*;
import base.Node;

import javax.sound.sampled.Line;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Solver {
    private final ArrayList<Program> programs = new ArrayList<>();
    private int maxDepth;
    private int epochs;

    public Solver(int numberOfPrograms, int maxDepth, int epochs){
        this.maxDepth = maxDepth;
        this.epochs = epochs;
        for(int i=0; i<numberOfPrograms; i++){
            programs.add(new Program(maxDepth));
        }
    }

    public static Program mutation(Program program) {

        System.out.println("\n---------------------------------------BEFORE-MUTATION----------------------------------------\n");
        System.out.println(program);
        System.out.println("\n---------------------------------------BEFORE-MUTATION-END----------------------------------------\n");

        Random rng = new Random();
        int randomInt = rng.nextInt(program.maxDepth);
        Node node = program;
        while (randomInt > 0) {
            randomInt--;
            if (node.children.isEmpty()) {
                break;
            }
            node = node.children.get(rng.nextInt(node.children.size()));
        }
        if (node instanceof Token)
            node = node.parent;
        node.children.clear();
//        node.getTreeRootNode().setMaxReachedDepth(0);
        node.generateChildren();

        System.out.println("\n---------------------------------------MUTATION----------------------------------------\n");
        System.out.println(node.root);
        System.out.println("\n---------------------------------------MUTATION-WHAT-CHANGED---------------------------------------\n");
        System.out.println(node);
        System.out.println("\n---------------------------------------MUTATION-END----------------------------------------\n");

        return node.root;
    }

    public static List<Node> getAllNodes(Node node) {
        if (node.children.isEmpty()) {
            return new ArrayList<>();
        } else {
            List<Node> nodes = new ArrayList<>();
            for (Node child : node.children) {
                nodes.addAll(getAllNodes(child));
            }
            nodes.addAll(node.children);
            return nodes;
        }
    }

    public static void setChildren(Node node, List<Node> new_children) {
        node.children = new ArrayList<>(new_children);
        for (Node child : node.children) {
            child.parent = node;
        }
        for (Node child : getAllNodes(node)){
            child.root = node.root;
            if(node instanceof BlockNode) child.indent++;
        }
    }

    public static Program cross(Program program1, Program program2) {
        List<Node> nodeList1 = getAllNodes(program1)
                .stream().filter(n -> n instanceof LineNode).toList();
        List<Node> nodeList2 = getAllNodes(program2)
                .stream().filter(n -> n instanceof LineNode).toList();

        if (nodeList1.isEmpty() || nodeList2.isEmpty()) {
            return null;
        }

        System.out.println("\n---------------------------------------CROSS P1----------------------------------------\n");
        System.out.println(program1);
        System.out.println("\n---------------------------------------CROSS P1 END----------------------------------------\n");
        System.out.println("\n---------------------------------------CROSS P2----------------------------------------\n");
        System.out.println(program2);
        System.out.println("\n---------------------------------------CROSS P2 END----------------------------------------\n");

        Random random = new Random();
        int r_1 = random.nextInt(nodeList1.size());
        int r_2 = random.nextInt(nodeList2.size());

        Node p1 = nodeList1.get(r_1);
        Node p2 = nodeList2.get(r_2);

        List<Node> p1_children = new ArrayList<>(p1.children);

        setChildren(p1, p2.children);
        setChildren(p2, p1_children);

        System.out.println("\n---------------------------------------CROSS-RESULT-CHANGE-P1--------------------------------------\n");
        System.out.println(p1);
        System.out.println("\n---------------------------------------CROSS-RESULT-CHANGE-P2--------------------------------------\n");
        System.out.println(p2);
        System.out.println("\n---------------------------------------CROSS-RESULT-CHANGE-END----------------------------------------\n");
        System.out.println("\n---------------------------------------CROSS-RESULT----------------------------------------\n");
        System.out.println(program2);
        System.out.println("\n---------------------------------------CROSS-RESULT-END----------------------------------------\n");

        return p2.root;
    }

    public void solve() {
        for (int i=0; i<epochs; i++){
//            if (fBestPop > -1e-5) { todo
//                System.out.print("PROBLEM SOLVED\n");
//                System.exit(0);
//            }
//            for (Program indiv : programs) {
//                evaluate(indiv);
//
//            }
            for (Program program : programs){
//                evaluate(program);
            }
        }
    }

//    public void evaluate(Program indiv) {
//        // tournament -> crossover2 best -> negative tournament -> mutation
//        switch (new Random().nextInt(2)) {
//            case 0:
//                Program new_mutated = mutation(indiv);
//                new_mutated.setReachedFitness(fitness(new_mutated));
//                this.reachedFitness = Math.max(new_mutated.getReachedFitness(), this.reachedFitness);
//                this.programs.add(new_mutated);
//                break;
//            case 1:
//                Program child = cross(indiv, tournament());
//                child.setReachedFitness(fitness(child));
//                this.reachedFitness = Math.max(child.getReachedFitness(), this.reachedFitness);
//                this.programs.add(child);
//                break;
//        }
////        System.out.println("Best fitness: " + this.reachedFitness);
//        negativeTournament();
//    }
}
