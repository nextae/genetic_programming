package genetic;

import base.Node;
import interpreter.app.App;
import interpreter.app.ProgramOutput;
import nodes.BlockNode;
import nodes.LineNode;
import nodes.Program;
import nodes.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solver {
    private final ArrayList<Program> programs = new ArrayList<>();
    private String path;
    private int epochs;
    private double bestFitness = Double.NEGATIVE_INFINITY;
    private static Random random = new Random();

    private int tournamentSize = 3;

    public Solver(String path, int numberOfPrograms, int maxDepth, int maxWidth, int epochs) {
        this.path = path;
        this.epochs = epochs;
        for (int i = 0; i < numberOfPrograms; i++)
            programs.add(new Program(maxDepth, maxWidth));
    }

    public static Program mutation(Program program) {
        return mutation(program, false);
    }

    public static Program mutation(Program program, boolean verbose) {

        if (verbose) {
            System.out.println("\n---------------------------------------BEFORE-MUTATION----------------------------------------\n");
            System.out.println(program);
            System.out.println("\n---------------------------------------BEFORE-MUTATION-END----------------------------------------\n");
        }

        Node node = program;

//        int randomInt = rng.nextInt(program.maxDepth);
//        while (randomInt > 0) {
//            randomInt--;
//            if (node.children.isEmpty()) {
//                break;
//            }
//            node = node.children.get(rng.nextInt(node.children.size()));
//        }

        List<Node> allNodes = getAllNodes(node);
        node = allNodes.get(random.nextInt(allNodes.size()));  // Get a random node to mutate
        if (node instanceof Token)
            node = node.parent;

        node.children.clear();

        node.generateChildren();

        if (verbose) {
            System.out.println("\n---------------------------------------MUTATION----------------------------------------\n");
            System.out.println(node.root);
            System.out.println("\n---------------------------------------MUTATION-WHAT-CHANGED---------------------------------------\n");
            System.out.println(node);
            System.out.println("\n---------------------------------------MUTATION-END----------------------------------------\n");
        }

        return node.root;
    }

    public static List<Node> getAllNodes(Node node) {
        if (node.children.isEmpty())
            return new ArrayList<>();

        List<Node> nodes = new ArrayList<>();
        for (Node child : node.children)
            nodes.addAll(getAllNodes(child));

        nodes.addAll(node.children);
        return nodes;
    }

    public static void setChildren(Node node, List<Node> new_children) {
        node.children = new ArrayList<>(new_children);
        for (Node child : node.children)
            child.parent = node;

        for (Node child : getAllNodes(node)) {
            child.root = node.root;
            if (node instanceof BlockNode)
                child.indent++;
        }
    }

    public static Program cross(Program program1, Program program2) {
        return cross(program1, program2, false);
    }

    public static Program cross(Program program1, Program program2, boolean verbose) {
        List<Node> nodeList1 = getAllNodes(program1)
                .stream().filter(n -> n instanceof LineNode).toList();
        List<Node> nodeList2 = getAllNodes(program2)
                .stream().filter(n -> n instanceof LineNode).toList();

        if (verbose) {
            System.out.println("\n---------------------------------------CROSS P1----------------------------------------\n");
            System.out.println(program1);
            System.out.println("\n---------------------------------------CROSS P1 END----------------------------------------\n");
            System.out.println("\n---------------------------------------CROSS P2----------------------------------------\n");
            System.out.println(program2);
            System.out.println("\n---------------------------------------CROSS P2 END----------------------------------------\n");
        }

        int r_1 = random.nextInt(nodeList1.size());
        int r_2 = random.nextInt(nodeList2.size());

        Node p1 = nodeList1.get(r_1);
        Node p2 = nodeList2.get(r_2);

        List<Node> p1_children = new ArrayList<>(p1.children);

        setChildren(p1, p2.children);
        setChildren(p2, p1_children);

        if (verbose) {
            System.out.println("\n---------------------------------------CROSS-RESULT-CHANGE-P1--------------------------------------\n");
            System.out.println(p1);
            System.out.println("\n---------------------------------------CROSS-RESULT-CHANGE-P2--------------------------------------\n");
            System.out.println(p2);
            System.out.println("\n---------------------------------------CROSS-RESULT-CHANGE-END----------------------------------------\n");
            System.out.println("\n---------------------------------------CROSS-RESULT----------------------------------------\n");
            System.out.println(program2);
            System.out.println("\n---------------------------------------CROSS-RESULT-END----------------------------------------\n");
        }

        return p2.root;
    }

    public void solve() {
        for (Program program : programs) {
            program.fitness = fitness(program);
            if (program.fitness > bestFitness)
                bestFitness = program.fitness;
        }

        for (int e = 0; e < epochs; e++) {
            System.out.println("Best fitness: " + bestFitness);
            for (Program program : programs) {
                if (program.fitness == bestFitness) {
                    System.out.println(program);
                    ProgramOutput output = App.run(program.toString(), path);

                    // TODO: wtf?
                    System.out.print("OUTPUTS: ");

                    for (int o: output.outputs)
                        System.out.printf("%d ", o);
                    System.out.println();
                }
            }
            if (bestFitness > -1e-4) {
                System.out.println("PROBLEM SOLVED");
                System.exit(0);
            }
            for (int i = 0; i < programs.size(); i++) {
                switch (random.nextInt(2)) {
                    case 0:  // Mutation
                        Program mutated = mutation(tournament());
                        mutated.fitness = fitness(mutated);
                        if (mutated.fitness > bestFitness)
                            bestFitness = mutated.fitness;

                        this.programs.add(mutated);
                        break;
                    case 1:  // Crossover
                        Program crossed = cross(tournament(), tournament());
                        crossed.fitness = fitness(crossed);

                        if (crossed.fitness > bestFitness)
                            bestFitness = crossed.fitness;

                        this.programs.add(crossed);
                        break;
                }
                this.programs.remove(negativeTournament());
            }
        }
    }

    private double fitness(Program program) {
        ProgramOutput output = App.run(program.toString(), path);
        return -Fitnesses.ex1(output.outputs);
    }

    private List<Integer> parseLine(String line) {
        return Stream.of(line.split(" "))
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    public Program tournament() {
        double bestFitness = Double.NEGATIVE_INFINITY;
        Program bestProgram = programs.get(0);

        for (int i = 0; i < tournamentSize; i++) {
            Program program = programs.get(random.nextInt(programs.size()));

            if (program.fitness > bestFitness) {
                bestProgram = program;
                bestFitness = program.fitness;
            }
        }

        return bestProgram;
    }

    public Program negativeTournament() {
        double worstFitness = Double.POSITIVE_INFINITY;
        Program worstProgram = programs.get(0);

        for (int i = 0; i < tournamentSize; i++) {
            Program program = programs.get(random.nextInt(programs.size()));

            if (program.fitness < worstFitness) {
                worstProgram = program;
                worstFitness = program.fitness;
            }
        }

        return worstProgram;
    }
}
