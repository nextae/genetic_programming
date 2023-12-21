package genetic;

import java.util.List;

public class Fitnesses {

    public static double ex1(List<Integer> outputs) {
        int minDistance = Integer.MAX_VALUE;

        for (int output : outputs) {
            int distance = Math.abs(output - 1);
            if (distance < minDistance)
                minDistance = distance;
        }

        return minDistance;
    }
    public static double ex4(List<Integer> outputs) {
        if (outputs.isEmpty())
            return Integer.MAX_VALUE;

        return Math.abs(outputs.get(0) - 1);
    }
}
