package model;

import sim.util.Double2D;

import java.util.HashMap;
import java.util.Map;


public enum Direction {
    Zero(new Double2D(0, 0), 4.0 / 9.0),
    North(new Double2D(0, -1), 1.0 / 9.0),
    NorthEast(new Double2D(1, -1), 1.0 / 36.0),
    East(new Double2D(1, 0), 1.0 / 9.0),
    SouthEast(new Double2D(1, 1), 1.0 / 36.0),
    South(new Double2D(0, 1), 1.0 / 9.0),
    SouthWest(new Double2D(-1, 1), 1.0 / 36.0),
    West(new Double2D(-1, 0), 1.0 / 9.0),
    NorthWest(new Double2D(-1, -1), 1.0 / 36.0);

    private final Double2D vector;
    private final double weight;

    private static final Map<Double2D, Direction> vectorMap = new HashMap<>();

    static {
        for (Direction dir : values()) {
            vectorMap.put(dir.vector, dir);
        }
    }

    public static Direction fromVector(Double2D vector) {
        return vectorMap.getOrDefault(vector, null);
    }

    Direction(Double2D vector, double weight) {
        this.vector = vector;
        this.weight = weight;
    }

    Direction opposite() {
        return switch (this) {
            case Zero -> Direction.Zero;
            case North -> Direction.South;
            case NorthEast -> Direction.SouthWest;
            case East -> Direction.West;
            case SouthEast -> Direction.NorthWest;
            case South -> Direction.North;
            case SouthWest -> Direction.NorthEast;
            case West -> Direction.East;
            case NorthWest -> Direction.SouthEast;
        };
    }

    Double2D getVector() {
        return vector;
    }

    double getWeight() {
        return weight;
    }
}