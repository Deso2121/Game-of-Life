package agh.cs.gameOfLife;

import java.util.*;

public class RandomIntPositionGenerator {
    private int[] x;
    private int[] y;
    private Random random;

    public RandomIntPositionGenerator() {
        random = new Random();
    }

    public ArrayList<Vector2d> generateVectors(int[] x, int[] y, int n) {
        this.x = x;
        this.y = y;
        ArrayList<Vector2d> vectors = new ArrayList<>();
        ArrayList<Vector2d> chosenVectors = new ArrayList<>();
        for (int i = 0; i < x.length; i ++)
            for (int j = 0; j < y.length; j ++) {
                vectors.add(new Vector2d(x[i], y[j]));
            }
        Collections.shuffle(vectors);
            for (int i = 0; i < n; i ++)
                chosenVectors.add(vectors.get(i));
            return chosenVectors;
    }

    public Vector2d getRandomVectorFromArray(ArrayList<Vector2d> vectorArray) {
        return vectorArray.get(random.nextInt(vectorArray.size()));
    }
}

