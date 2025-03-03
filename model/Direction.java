package model;

public enum Direction {
    UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0), NOWHERE(0, 0);

    public int x, y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
