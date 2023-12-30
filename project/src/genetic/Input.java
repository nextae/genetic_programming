package genetic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Input {
    public static List<List<Integer>> fromFile(String path) throws FileNotFoundException {
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
        return inputs;
    }

    public static List<List<Integer>> random(int rows, int cols, int lowerBound, int upperBound) {
        Random random = new Random();
        List<List<Integer>> inputs = new ArrayList<>();
        for(int i = 0; i<rows; i++){
            inputs.add(new ArrayList<>());
            for(int j = 0; j<cols; j++){
                inputs.get(i).add(random.nextInt(lowerBound, upperBound));
            }
        }
        return inputs;
    }

    public static List<List<Integer>> truthTable(int k) {
        List<List<Integer>> inputs = new ArrayList<>();
        for(int i = 0; i < Math.pow(2, k); i++) {
            List<Integer> row = new ArrayList<>();

            for (int j = k - 1; j >= 0; j--)
                row.add((i >> j) & 1);

            inputs.add(row);
        }
        return inputs;
    }
}
