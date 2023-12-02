package bg.sofia.uni.fmi.mjt.gym.workout;

public record Exercise(String name, int sets, int repetitions) implements Comparable<Exercise> {
    @Override
    public int compareTo(Exercise o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Exercise e) {
            return this.name().equals(e.name());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
