package genetic;

import java.util.List;

public class Fitnesses {
    private static final int OUTPUT_SIZE_DIFFERENCE_WEIGHT = 5;

    public static double ex1_1_A(List<Integer> inputs, List<Integer> outputs) {
        int minDistance = Integer.MAX_VALUE;

        for (int output : outputs) {
            int distance = Math.abs(output - 1);
            if (distance < minDistance)
                minDistance = distance;
        }

        return minDistance;
    }

    public static double ex1_1_B(List<Integer> inputs, List<Integer> outputs) {
        int minDistance = Integer.MAX_VALUE;

        for (int output : outputs) {
            int distance = Math.abs(output - 789);
            if (distance < minDistance)
                minDistance = distance;
        }

        return minDistance;
    }

    public static double ex1_1_C(List<Integer> inputs, List<Integer> outputs) {
        int minDistance = Integer.MAX_VALUE;

        for (int output : outputs) {
            int distance = Math.abs(output - 31415);
            if (distance < minDistance)
                minDistance = distance;
        }

        return minDistance;
    }

    public static double ex1_1_D(List<Integer> inputs, List<Integer> outputs) {
        if (outputs.isEmpty())
            return Integer.MAX_VALUE;

        return Math.abs(outputs.get(0) - 1);
    }

    public static double ex1_1_E(List<Integer> inputs, List<Integer> outputs) {
        if (outputs.isEmpty())
            return Integer.MAX_VALUE;

        return Math.abs(outputs.get(0) - 789);
    }

    public static double ex1_1_F(List<Integer> inputs, List<Integer> outputs) {
        if (outputs.isEmpty())
            return Integer.MAX_VALUE;

        int outputSizeDifference = Math.abs(outputs.size() - 1);

        return outputSizeDifference * OUTPUT_SIZE_DIFFERENCE_WEIGHT + Math.abs(outputs.get(0) - 1);
    }

    public static double ex1_2_A(List<Integer> inputs, List<Integer> outputs) {
        if (inputs.size() < 2)
            return Integer.MAX_VALUE;

        int a = inputs.get(0);
        int b = inputs.get(1);

        int outputSizeDifference = Math.abs(outputs.size() - 1);

        return outputSizeDifference * OUTPUT_SIZE_DIFFERENCE_WEIGHT + Math.abs(outputs.get(0) - (a + b));
    }

    public static double ex1_2_B(List<Integer> inputs, List<Integer> outputs) {
        return ex1_2_A(inputs, outputs);
    }

    public static double ex1_2_C(List<Integer> inputs, List<Integer> outputs) {
        return ex1_2_A(inputs, outputs);
    }

    public static double ex1_2_D(List<Integer> inputs, List<Integer> outputs) {
        if (inputs.size() < 2)
            return Integer.MAX_VALUE;

        int a = inputs.get(0);
        int b = inputs.get(1);

        int outputSizeDifference = Math.abs(outputs.size() - 1);

        return outputSizeDifference * OUTPUT_SIZE_DIFFERENCE_WEIGHT + Math.abs(outputs.get(0) - (a - b));
    }

    public static double ex1_2_E(List<Integer> inputs, List<Integer> outputs) {
        if (inputs.size() < 2)
            return Integer.MAX_VALUE;

        int a = inputs.get(0);
        int b = inputs.get(1);

        int outputSizeDifference = Math.abs(outputs.size() - 1);

        return outputSizeDifference * OUTPUT_SIZE_DIFFERENCE_WEIGHT + Math.abs(outputs.get(0) - (a * b));
    }

    public static double ex1_3_A(List<Integer> inputs, List<Integer> outputs) {
        if (inputs.size() < 2)
            return Integer.MAX_VALUE;

        int a = inputs.get(0);
        int b = inputs.get(1);

        int outputSizeDifference = Math.abs(outputs.size() - 1);

        return outputSizeDifference * OUTPUT_SIZE_DIFFERENCE_WEIGHT + Math.abs(outputs.get(0) - Math.max(a, b));
    }

    public static double ex1_3_B(List<Integer> inputs, List<Integer> outputs) {
        return ex1_3_A(inputs, outputs);
    }

    public static double ex1_4_A(List<Integer> inputs, List<Integer> outputs) {
        if (inputs.size() < 10)
            return Integer.MAX_VALUE;

        int sum = 0;
        for (int i = 0; i < 10; i++)
            sum += inputs.get(i);

        int mean = sum / 10;

        int outputSizeDifference = Math.abs(outputs.size() - 1);

        return outputSizeDifference * OUTPUT_SIZE_DIFFERENCE_WEIGHT + Math.abs(outputs.get(0) - mean);
    }

    public static double ex1_4_B(List<Integer> inputs, List<Integer> outputs) {
        if (inputs.isEmpty())
            return Integer.MAX_VALUE;

        int n = inputs.get(0);
        if (inputs.size() < n + 1)
            return Integer.MAX_VALUE;

        int sum = 0;
        for (int i = 0; i < n; i++)
            sum += inputs.get(i + 1);

        int mean = sum / n;

        int outputSizeDifference = Math.abs(outputs.size() - 1);

        return outputSizeDifference * OUTPUT_SIZE_DIFFERENCE_WEIGHT + Math.abs(outputs.get(0) - mean);
    }
}
