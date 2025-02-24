package persistence;

public class HighScore {
    private final String name;
    private final int time;

    public HighScore(String name, int time) {
        this.name = name;
        this.time = time;
    }

    public String getName() { return name; }
    public int getTime() { return time; }

    @Override
    public String toString() {
        return "HighScore: [ name = " + name + " | time = " + time + " ]";
    }
}
