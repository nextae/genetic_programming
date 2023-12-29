package genetic;

import java.util.List;

public class Fitnesses {
    private static final int INVALID_VALUE = 99999;
    private static final int OUTPUT_SIZE_DIFFERENCE_WEIGHT = 3;

    public static double ex1_1_A(List<List<Integer>> inputs, List<List<Integer>> outputs) {
        return calculateFitness(inputs, outputs, 1);
    }

    public static double ex1_1_B(List<List<Integer>> inputs, List<List<Integer>> outputs) {
        return calculateFitness(inputs, outputs, 789);
    }

    public static double ex1_1_C(List<List<Integer>> inputs, List<List<Integer>> outputs) {
        return calculateFitness(inputs, outputs, 31415);
    }

    public static double ex1_1_D(List<List<Integer>> inputs, List<List<Integer>> outputs) {
        return calculateFitnessExactPlace(inputs, outputs, 0, 1);
    }

    public static double ex1_1_E(List<List<Integer>> inputs, List<List<Integer>> outputs) {
        return calculateFitnessExactPlace(inputs, outputs, 0, 789);
    }

    public static double ex1_1_F(List<List<Integer>> inputs, List<List<Integer>> outputs) {
        if (inputs.size() < 2 || outputs.isEmpty())
            return Integer.MAX_VALUE;

        int outputSizeDifference = Math.abs(outputs.size() - 1);

        return (outputSizeDifference * OUTPUT_SIZE_DIFFERENCE_WEIGHT + Math.abs(outputs.get(0).get(0) - 1));
    }

    public static double ex1_2_A(List<List<Integer>> inputs, List<List<Integer>> outputs) {
        return calculateFitnessForArithmeticOperation(inputs, outputs, '+');
    }

    public static double ex1_2_B(List<List<Integer>> inputs, List<List<Integer>> outputs) {
        return ex1_2_A(inputs, outputs);
    }

    public static double ex1_2_C(List<List<Integer>> inputs, List<List<Integer>> outputs) {
        return ex1_2_A(inputs, outputs);
    }

    public static double ex1_2_D(List<List<Integer>> inputs, List<List<Integer>> outputs) {
        return calculateFitnessForArithmeticOperation(inputs, outputs, '-');
    }

    public static double ex1_2_E(List<List<Integer>> inputs, List<List<Integer>> outputs) {
        return calculateFitnessForArithmeticOperation(inputs, outputs, '*');
    }

    public static double ex1_3_A(List<List<Integer>> inputs, List<List<Integer>> outputs) {
        return calculateFitnessForComparisonOperation(inputs, outputs, "max");
    }

    public static double ex1_3_B(List<List<Integer>> inputs, List<List<Integer>> outputs) {
        return ex1_3_A(inputs, outputs);
    }

    public static double ex1_4_A(List<List<Integer>> inputs, List<List<Integer>> outputs) {
        long sum = 0;
        for (int i = 0; i < inputs.size(); i++) {
            List<Integer> input = inputs.get(i);
            List<Integer> output = outputs.get(i);
            if (input.size() < 2 || output.isEmpty()) {
                sum += INVALID_VALUE;
                continue;
            }

            sum += (long) calculateFitnessForMeanOperation(input, output, 2, false);
        }

        return (double) sum / inputs.size();
    }

    public static double ex1_4_B(List<List<Integer>> inputs, List<List<Integer>> outputs) {
        long sum = 0;
        for (int i = 0; i < inputs.size(); i++) {
            List<Integer> input = inputs.get(i);
            List<Integer> output = outputs.get(i);
            if (input.isEmpty() || output.isEmpty()) {
                sum += INVALID_VALUE;
                continue;
            }

            int n = input.get(0);
            if (input.size() < n + 1) {
                sum += INVALID_VALUE;
                continue;
            }

            sum += (long) calculateFitnessForMeanOperation(input, output, n, true);
        }

        return (double) sum / inputs.size();
    }

    private static double calculateFitness(List<List<Integer>> inputs, List<List<Integer>> outputs, int target) {
        long sum = 0;
        for (int i = 0; i < inputs.size(); i++) {
            List<Integer> output = outputs.get(i);
            if (output.isEmpty()) {
                sum += INVALID_VALUE;
                continue;
            }

            int minDistance = Integer.MAX_VALUE;

            for (int o : output) {
                int distance = Math.abs(o - target);
                if (distance < minDistance)
                    minDistance = distance;
            }

            sum += minDistance;
        }

        return (double) sum / inputs.size();
    }

    private static double calculateFitnessExactPlace(List<List<Integer>> inputs, List<List<Integer>> outputs, int index, int target) {
        long sum = 0;
        for (int i = 0; i < inputs.size(); i++) {
            List<Integer> output = outputs.get(i);
            if (output.isEmpty()) {
                sum += INVALID_VALUE;
                continue;
            }

            sum += Math.abs(output.get(index) - target);
        }

        return (double) sum / inputs.size();
    }

    private static double calculateFitnessForArithmeticOperation(List<List<Integer>> inputs, List<List<Integer>> outputs, char operation) {
        long sum = 0;
        for (int i = 0; i < inputs.size(); i++) {
            List<Integer> input = inputs.get(i);
            List<Integer> output = outputs.get(i);
            if (input.size() < 2 || output.isEmpty()) {
                sum += INVALID_VALUE;
                continue;
            }

            int a = input.get(0);
            int b = input.get(1);
            int result = switch (operation) {
                case '+' -> a + b;
                case '-' -> a - b;
                case '*' -> a * b;
                default -> 0;
            };

            int outputSizeDifference = Math.abs(output.size() - 1);

            sum += (long) outputSizeDifference * OUTPUT_SIZE_DIFFERENCE_WEIGHT + Math.abs(output.get(0) - result);
        }

        return (double) sum / inputs.size();
    }

    private static double calculateFitnessForComparisonOperation(List<List<Integer>> inputs, List<List<Integer>> outputs, String operation) {
        long sum = 0;
        for (int i = 0; i < inputs.size(); i++) {
            List<Integer> input = inputs.get(i);
            List<Integer> output = outputs.get(i);
            if (output.isEmpty()) {
                sum += INVALID_VALUE;
                continue;
            }

            int a = input.get(0);
            int b = input.get(1);
            int result = switch (operation) {
                case "max" -> Math.max(a, b);
                default -> 0;
            };

            int outputSizeDifference = Math.abs(output.size() - 1);

            sum += (long) outputSizeDifference * OUTPUT_SIZE_DIFFERENCE_WEIGHT + Math.abs(output.get(0) - result);
        }

        return (double) sum / inputs.size();
    }

    private static double calculateFitnessForMeanOperation(List<Integer> inputs, List<Integer> outputs, int count, boolean skipFirst) {
        if (inputs.size() < count || outputs.isEmpty())
            return INVALID_VALUE;

        int sum = 0;
        for (int i = 0; i < count; i++)
            if (!skipFirst || i != 0)
                sum += inputs.get(i);

        int mean = sum / count;

        int outputSizeDifference = Math.abs(outputs.size() - 1);

        return (outputSizeDifference * OUTPUT_SIZE_DIFFERENCE_WEIGHT + Math.abs(outputs.get(0) - mean));
    }
}