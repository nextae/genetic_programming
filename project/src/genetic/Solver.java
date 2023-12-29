package genetic;

import base.Node;
import interpreter.app.App;
import interpreter.app.ProgramOutput;
import nodes.BlockNode;
import nodes.LineNode;
import nodes.Program;
import nodes.Token;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Solver {
    private final ArrayList<Program> programs = new ArrayList<>();
    private int epochs;
    private double bestFitness = Double.NEGATIVE_INFINITY;
    private static Random random = new Random();

    private int tournamentSize = 3;
    public List<List<Integer>> inputs;

    public Solver(String path, int numberOfPrograms, int maxDepth, int maxWidth, int epochs) throws FileNotFoundException {
        this.epochs = epochs;
        for (int i = 0; i < numberOfPrograms; i++)
            programs.add(new Program(maxDepth, maxWidth, GenerationMethods.GROW));

        Scanner scanner = new Scanner(new File(path));

        List<List<Integer>> inputs = new ArrayList<>();

        // Iterate through each line in the file
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            String[] tokens = line.split(" ");
            List<Integer> integers = new ArrayList<>();

            for (String token : tokens)
                integers.add(Integer.parseInt(token));

            inputs.add(integers);
        }

        scanner.close();

        this.inputs = inputs;
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

        Program clone = program.clone(null);

//        int randomInt = rng.nextInt(program.maxDepth);
//        while (randomInt > 0) {
//            randomInt--;
//            if (node.children.isEmpty()) {
//                break;
//            }
//            node = node.children.get(rng.nextInt(node.children.size()));
//        }

        List<Node> allNodes = getAllNodes(clone);
        Node node = allNodes.get(random.nextInt(allNodes.size()));  // Get a random node to mutate
        if (node instanceof Token)
            node = node.parent;

        node.children.clear();
        node.root = clone;

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
        Set<Node> visitedNodes = new HashSet<>();
        List<Node> nodes = new ArrayList<>();

        getAllNodesHelper(node, visitedNodes, nodes);
        return nodes;
    }

    private static void getAllNodesHelper(Node node, Set<Node> visitedNodes, List<Node> nodes) {
        if (!visitedNodes.contains(node)) {
            visitedNodes.add(node);

            for (Node child : node.children) {
                getAllNodesHelper(child, visitedNodes, nodes);
            }

            nodes.add(node);
        }
    }

    private static void getMaxIndent(Node node, int[] maxIndent){
        for (Node child : node.children) {
            getMaxIndent(child, maxIndent);
        }
        maxIndent[0] = Math.max(maxIndent[0], node.indent);
    }

    public static void setChildren(Node node, List<Node> new_children) {
        node.children = new ArrayList<>();
        for (Node child : new_children)
            node.children.add(child.clone(node));

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

        List<Node> nodeList2 = getAllNodes(program2.clone(null))
                .stream().filter(n -> n instanceof LineNode).toList();
        int r_2 = random.nextInt(nodeList2.size());
        Node p2 = nodeList2.get(r_2);
        List<Node> nodeList1 = getAllNodes(program1.clone(null))
                .stream().filter(n -> n instanceof LineNode).filter(n -> {
                    int[] maxIndent = {Integer.MIN_VALUE};
                    getMaxIndent(n, maxIndent);
                    return maxIndent[0] - n.indent <= p2.root.maxDepth - p2.indent;
                }).toList();

        if (verbose) {
            System.out.println("\n---------------------------------------CROSS P1----------------------------------------\n");
            System.out.println(program1);
            System.out.println("\n---------------------------------------CROSS P1 END----------------------------------------\n");
            System.out.println("\n---------------------------------------CROSS P2----------------------------------------\n");
            System.out.println(program2);
            System.out.println("\n---------------------------------------CROSS P2 END----------------------------------------\n");
        }
        int r_1 = random.nextInt(nodeList1.size());

        Node p1 = nodeList1.get(r_1);

        setChildren(p2, p1.children);

        if (verbose) {
            System.out.println("\n---------------------------------------CROSS-RESULT-CHANGE-P1--------------------------------------\n");
            System.out.println(p1);
            System.out.println("\n---------------------------------------CROSS-RESULT-CHANGE-P2--------------------------------------\n");
            System.out.println(p2);
            System.out.println("\n---------------------------------------CROSS-RESULT-CHANGE-END----------------------------------------\n");
            System.out.println("\n---------------------------------------CROSS-RESULT----------------------------------------\n");
            System.out.println(p2.root);
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

        int epoch;
        for (epoch = 0; epoch < epochs; epoch++) {
            if (bestFitness > -1e-4) {
                System.out.println("PROBLEM SOLVED");
                printBestProgram(epoch);
                System.exit(0);
            } else {
                System.out.println(epoch + ". Best fitness: " + bestFitness);
                printBestProgram(epoch);
            }
            for (int i = 0; i < programs.size(); i++) {
                switch (random.nextInt(10)) {
                    case 0:  // Mutation
                        Program mutated = mutation(tournament());
                        mutated.fitness = fitness(mutated);
                        if (mutated.fitness > bestFitness)
                            bestFitness = mutated.fitness;

                        this.programs.add(mutated);
                        break;
                    default:  // Crossover
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
        System.out.println("PROBLEM NOT SOLVED");
        printBestProgram(epoch);
    }

    private void printBestProgram(int epoch) {
        for (Program program : programs) {
            if (program.fitness == bestFitness) {
                System.out.println(program);
                ProgramOutput output = App.run(program.toString(), inputs);

                System.out.println("======= EPOCH " + epoch + ", BEST FITNESS " + bestFitness + ", OUTPUTS =======");
                for (List<Integer> outputList : output.outputs) {
                    for (int o: outputList)
                        System.out.printf("%d ", o);

                    System.out.print("\n");
                }
                System.out.println("=======================");
                break;
            }
        }
    }

    private double fitness(Program program) {
        ProgramOutput result = App.run(program.toString(), inputs);
        return -Fitnesses.ex1_3_A(result.inputs, result.outputs);
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
