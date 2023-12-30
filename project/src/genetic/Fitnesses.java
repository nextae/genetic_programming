package genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

interface BooleanFunction {
    boolean execute(List<Boolean> inputs);
}

public class Fitnesses {

    public interface Fitness {
        double calcFitness(List<List<Integer>> inputs, List<List<Integer>> outputs);
    }

    private static final int INVALID_VALUE = 99999;
    private static final int OUTPUT_SIZE_DIFFERENCE_WEIGHT = 3;
    private static final int DEFAULT_VALUE = 1;

    public static Fitness ex1_1_A() {
        return Fitnesses::ex1_1_A_Impl;
    }

    public static Fitness ex1_1_B() {
        return Fitnesses::ex1_1_B_Impl;
    }

    public static Fitness ex1_1_C() {
        return Fitnesses::ex1_1_C_Impl;
    }

    public static Fitness ex1_1_D() {
        return Fitnesses::ex1_1_D_Impl;
    }

    public static Fitness ex1_1_E() {
        return Fitnesses::ex1_1_E_Impl;
    }

    public static Fitness ex1_1_F() {
        return Fitnesses::ex1_1_F_Impl;
    }

    public static Fitness ex1_2_A() {
        return Fitnesses::ex1_2_A_Impl;
    }

    public static Fitness ex1_2_B() {
        return Fitnesses::ex1_2_B_Impl;
    }

    public static Fitness ex1_2_C() {
        return Fitnesses::ex1_2_C_Impl;
    }

    public static Fitness ex1_2_D() {
        return Fitnesses::ex1_2_D_Impl;
    }

    public static Fitness ex1_2_E() {
        return Fitnesses::ex1_2_E_Impl;
    }

    public static Fitness ex1_3_A() {
        return Fitnesses::ex1_3_A_Impl;
    }

    public static Fitness ex1_3_B() {
        return Fitnesses::ex1_3_B_Impl;
    }

    public static Fitness ex1_4_A() {
        return Fitnesses::ex1_4_A_Impl;
    }

    public static Fitness ex1_4_B() {
        return Fitnesses::ex1_4_B_Impl;
    }

    public static Fitness negativeToZero = (inputs, outputs) -> {
        long sum = 0;
        for (int i = 0; i < inputs.size(); i++) {
            List<Integer> input = inputs.get(i);
            List<Integer> output = outputs.get(i);
            if (input.isEmpty() || output.isEmpty()) {
                sum += INVALID_VALUE;
                continue;
            }
            int outputSizeDifference = Math.abs(output.size() - input.size());

            int sumDifferences = 0;

            for (int j = 0; j < input.size(); j++) {
                int value = input.get(j);
                if (value < 0)
                    value = 0;

                int outputValue = j < output.size() ? output.get(j) : DEFAULT_VALUE;

                sumDifferences += Math.abs(value - outputValue);
            }

            sum += (long) outputSizeDifference * OUTPUT_SIZE_DIFFERENCE_WEIGHT + sumDifferences;
        }

        return (double) sum / inputs.size();
    };

    public static Fitness smallest = (inputs, outputs) -> {
        long sum = 0;
        for (int i = 0; i < inputs.size(); i++) {
            List<Integer> input = inputs.get(i);
            List<Integer> output = outputs.get(i);
            if (input.size() < 4 || output.isEmpty()) {
                sum += INVALID_VALUE;
                continue;
            }

            int outputSizeDifference = Math.abs(output.size() - 1);

            sum += (long) outputSizeDifference * OUTPUT_SIZE_DIFFERENCE_WEIGHT + Math.abs(Collections.min(input) - output.get(0));
        }

        return (double) sum / inputs.size();
    };

    public static Fitness gecco_4 = (inputs, outputs) -> {
        long sum = 0;
        for (int i = 0; i < inputs.size(); i++) {
            List<Integer> input = inputs.get(i);
            List<Integer> output = outputs.get(i);
            int[] inputValues = new int[3];
            if (input.isEmpty() || output.isEmpty()) {
                inputValues = new int[]{1, 1, 1};
            } else {
                for (int ii = 0; ii < 3; ii++) {
                    inputValues[ii] = input.get(ii % input.size());
                }
            }
            int expected = (inputValues[0] < inputValues[1] && inputValues[1] < inputValues[2]) ? 1 : -1;
            if (output.isEmpty()) sum += INVALID_VALUE;
            else {
                sum += output.get(0) == expected ? 0 : 1;
                int outputSizeDifference = Math.abs(output.size() - 1);
                sum += (long) outputSizeDifference * OUTPUT_SIZE_DIFFERENCE_WEIGHT;
            }
        }

        return (double) sum / inputs.size();
    };

    private static double ex1_1_A_Impl(List<List<Integer>> inputs, List<List<Integer>> outputs) {
        return calculateFitness(inputs, outputs, 1);
    }

    private static double ex1_1_B_Impl(List<List<Integer>> inputs, List<List<Integer>> outputs) {
        return calculateFitness(inputs, outputs, 789);
    }

    private static double ex1_1_C_Impl(List<List<Integer>> inputs, List<List<Integer>> outputs) {
        return calculateFitness(inputs, outputs, 31415);
    }

    private static double ex1_1_D_Impl(List<List<Integer>> inputs, List<List<Integer>> outputs) {
        return calculateFitnessExactPlace(inputs, outputs, 0, 1);
    }

    private static double ex1_1_E_Impl(List<List<Integer>> inputs, List<List<Integer>> outputs) {
        return calculateFitnessExactPlace(inputs, outputs, 0, 789);
    }

    private static double ex1_1_F_Impl(List<List<Integer>> inputs, List<List<Integer>> outputs) {
        long sum = 0;
        for (int i = 0; i < inputs.size(); i++) {
            List<Integer> output = outputs.get(i);
            if (output.isEmpty()) {
                sum += INVALID_VALUE;
                continue;
            }

            int outputSizeDifference = Math.abs(output.size() - 1);

            sum += (long) outputSizeDifference * OUTPUT_SIZE_DIFFERENCE_WEIGHT + Math.abs(output.get(0) - 1);
        }

        return (double) sum / inputs.size();
    }

    private static double ex1_2_A_Impl(List<List<Integer>> inputs, List<List<Integer>> outputs) {
        return calculateFitnessForArithmeticOperation(inputs, outputs, '+');
    }

    private static double ex1_2_B_Impl(List<List<Integer>> inputs, List<List<Integer>> outputs) {
        return ex1_2_A_Impl(inputs, outputs);
    }

    private static double ex1_2_C_Impl(List<List<Integer>> inputs, List<List<Integer>> outputs) {
        return ex1_2_A_Impl(inputs, outputs);
    }

    private static double ex1_2_D_Impl(List<List<Integer>> inputs, List<List<Integer>> outputs) {
        return calculateFitnessForArithmeticOperation(inputs, outputs, '-');
    }

    private static double ex1_2_E_Impl(List<List<Integer>> inputs, List<List<Integer>> outputs) {
        return calculateFitnessForArithmeticOperation(inputs, outputs, '*');
    }

    private static double ex1_3_A_Impl(List<List<Integer>> inputs, List<List<Integer>> outputs) {
        return calculateFitnessForComparisonOperation(inputs, outputs, "max");
    }

    private static double ex1_3_B_Impl(List<List<Integer>> inputs, List<List<Integer>> outputs) {
        return ex1_3_A_Impl(inputs, outputs);
    }

    private static double ex1_4_A_Impl(List<List<Integer>> inputs, List<List<Integer>> outputs) {
        long sum = 0;
        for (int i = 0; i < inputs.size(); i++) {
            List<Integer> input = inputs.get(i);
            List<Integer> output = outputs.get(i);
            if (input.size() < 2 || output.isEmpty()) {
                sum += INVALID_VALUE;
                continue;
            }

            sum += calculateFitnessForMeanOperation(input, output, 10, false);
        }

        return (double) sum / inputs.size();
    }

    private static double ex1_4_B_Impl(List<List<Integer>> inputs, List<List<Integer>> outputs) {
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

            sum += calculateFitnessForMeanOperation(input, output, n, true);
        }

        return (double) sum / inputs.size();
    }

    public static Fitness not() {
        return (inputs, outputs) -> calculateBooleanFunction(inputs, outputs, 1, in -> !in.get(0));
    }

    public static Fitness and() {
        return (inputs, outputs) -> calculateBooleanFunction(inputs, outputs, 2, in -> in.get(0) && in.get(1));
    }

    public static Fitness xor() {
        return (inputs, outputs) -> calculateBooleanFunction(inputs, outputs, 2, in -> in.get(0) ^ in.get(1));
    }

    public static Fitness and_or() {
        return (inputs, outputs) -> calculateBooleanFunction(inputs, outputs, 3, in -> in.get(0) && in.get(1) || in.get(2));
    }

    public static Fitness or_and() {
        return (inputs, outputs) -> calculateBooleanFunction(inputs, outputs, 3, in -> in.get(0) || in.get(1) && in.get(2));
    }

    public static Fitness xor_and() {
        return (inputs, outputs) -> calculateBooleanFunction(inputs, outputs, 3, in -> in.get(0) ^ in.get(1) && in.get(2));
    }

    public static Fitness or_and_or() {
        return (inputs, outputs) -> calculateBooleanFunction(inputs, outputs, 4, in -> in.get(0) || in.get(1) && in.get(2) || in.get(3));
    }

    public static Fitness and_xor_or() {
        return (inputs, outputs) -> calculateBooleanFunction(inputs, outputs, 4, in -> in.get(0) && in.get(1) ^ in.get(2) || in.get(3));
    }

    public static Fitness and_and_or() {
        return (inputs, outputs) -> calculateBooleanFunction(inputs, outputs, 4, in -> in.get(0) && in.get(1) && in.get(2) || in.get(3));
    }

    public static Fitness and_or_xor_and() {
        return (inputs, outputs) -> calculateBooleanFunction(inputs, outputs, 5, in -> in.get(0) && in.get(1) || in.get(2) ^ in.get(3) && in.get(4));
    }

    public static Fitness xor_and_xor_or() {
        return (inputs, outputs) -> calculateBooleanFunction(inputs, outputs, 5, in -> in.get(0) ^ in.get(1) && in.get(2) ^ in.get(3) || in.get(4));
    }

    public static Fitness and_and_or_xor_or() {
        return (inputs, outputs) -> calculateBooleanFunction(inputs, outputs, 6, in -> in.get(0) && in.get(1) && in.get(2) || in.get(3) ^ in.get(4) || in.get(5));
    }

    public static Fitness or_and_xor_xor_and() {
        return (inputs, outputs) -> calculateBooleanFunction(inputs, outputs, 6, in -> in.get(0) || in.get(1) && in.get(2) ^ in.get(3) ^ in.get(4) && in.get(5));
    }

    public static Fitness xor_xor_or_and_and_or() {
        return (inputs, outputs) -> calculateBooleanFunction(inputs, outputs, 7, in -> in.get(0) ^ in.get(1) ^ in.get(2) || in.get(3) && in.get(4) && in.get(5) || in.get(6));
    }
    public static Fitness and_or_or_xor_and_xor() {
        return (inputs, outputs) -> calculateBooleanFunction(inputs, outputs, 7, in -> in.get(0) && in.get(1) || in.get(2) || in.get(3) ^ in.get(4) && in.get(5) ^ in.get(6));
    }

    public static Fitness xor_and_and_and_or_or_xor() {
        return (inputs, outputs) -> calculateBooleanFunction(inputs, outputs, 8, in -> in.get(0) ^ in.get(1) && in.get(2) && in.get(3) && in.get(4) || in.get(5) || in.get(6) ^ in.get(7));
    }

    public static Fitness and_or_xor_and_or_xor_and() {
        return (inputs, outputs) -> calculateBooleanFunction(inputs, outputs, 8, in -> in.get(0) && in.get(1) || in.get(2) ^ in.get(3) && in.get(4) || in.get(5) ^ in.get(6) && in.get(7));
    }

    public static Fitness or_and_xor_and_or_or_xor_and() {
        return (inputs, outputs) -> calculateBooleanFunction(inputs, outputs, 9, in -> in.get(0) || in.get(1) && in.get(2) ^ in.get(3) && in.get(4) || in.get(5) || in.get(6) ^ in.get(7) && in.get(8));
    }

    public static Fitness and_xor_xor_xor_and_and_xor_or() {
        return (inputs, outputs) -> calculateBooleanFunction(inputs, outputs, 9, in -> in.get(0) && in.get(1) ^ in.get(2) ^ in.get(3) ^ in.get(4) && in.get(5) && in.get(6) ^ in.get(7) || in.get(8));
    }

    public static Fitness xor_xor_xor_and_and_and_or_or_or() {
        return (inputs, outputs) -> calculateBooleanFunction(inputs, outputs, 10, in -> in.get(0) ^ in.get(1) ^ in.get(2) ^ in.get(3) && in.get(4) && in.get(5) && in.get(6) || in.get(7) || in.get(8) || in.get(9));
    }

    public static Fitness xor_and_or_and_or_or_xor_or_and() {
        return (inputs, outputs) -> calculateBooleanFunction(inputs, outputs, 10, in -> in.get(0) ^ in.get(1) && in.get(2) || in.get(3) && in.get(4) || in.get(5) || in.get(6) ^ in.get(7) || in.get(8) && in.get(9));
    }

    private static double calculateBooleanFunction(List<List<Integer>> inputs, List<List<Integer>> outputs, int k, BooleanFunction function) {
        long sum = 0;
        for (int i = 0; i < inputs.size(); i++) {
            List<Boolean> input = inputs.get(i).stream().map(n -> n == 1).toList();
            List<Integer> output = outputs.get(i);
            if (input.size() < k || output.isEmpty())
                return INVALID_VALUE;

            int outputInt = output.get(0);
            if (outputInt != 1 && outputInt != 0)
                return INVALID_VALUE;

            boolean outputBool = outputInt == 1;

            int outputSizeDifference = Math.abs(output.size() - 1);

            sum += (long) outputSizeDifference * OUTPUT_SIZE_DIFFERENCE_WEIGHT + (function.execute(input) == outputBool ? 0 : 1);
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

    private static int calculateFitnessForMeanOperation(List<Integer> inputs, List<Integer> outputs, int count, boolean skipFirst) {
        if (inputs.size() < (count + (skipFirst ? 1 : 0)) || outputs.isEmpty())
            return INVALID_VALUE;

        int sum = 0;
        for (int i = 0; i < count + (skipFirst ? 1 : 0); i++)
            if (!skipFirst || i != 0)
                sum += inputs.get(i);

        int mean = sum / count;

        int outputSizeDifference = Math.abs(outputs.size() - 1);

        return outputSizeDifference * OUTPUT_SIZE_DIFFERENCE_WEIGHT + Math.abs(outputs.get(0) - mean);
    }


}