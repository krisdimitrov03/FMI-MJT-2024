package bg.sofia.uni.fmi.mjt.gym.member;

import bg.sofia.uni.fmi.mjt.gym.workout.Exercise;
import bg.sofia.uni.fmi.mjt.gym.workout.Workout;

import java.time.DayOfWeek;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Member implements GymMember, Comparable<GymMember> {
    private String personalIdNumber;
    private String name;
    private int age;
    private Gender gender;
    private Address address;
    private Map<DayOfWeek, Workout> trainingProgram;

    public Member(Address address, String name, int age, String personalIdNumber, Gender gender) {
        this.address = address;
        this.name = name;
        this.age = age;
        this.personalIdNumber = personalIdNumber;
        this.gender = gender;
        this.trainingProgram = new HashMap<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public String getPersonalIdNumber() {
        return personalIdNumber;
    }

    @Override
    public Gender getGender() {
        return gender;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public Map<DayOfWeek, Workout> getTrainingProgram() {
        return trainingProgram;
    }

    @Override
    public void setWorkout(DayOfWeek day, Workout workout) {
        if (day == null) {
            throw new IllegalArgumentException("Day is null");
        }

        if (workout == null) {
            throw new IllegalArgumentException("Workout is null");
        }

        if (!trainingProgram.containsKey(day)) {
            trainingProgram.put(day, workout);
        }
    }

    @Override
    public Collection<DayOfWeek> getDaysFinishingWith(String exerciseName) {
        if (exerciseName == null || exerciseName.isEmpty()) {
            throw new IllegalArgumentException("Exercise name is null or empty");
        }

        Collection<DayOfWeek> result = new HashSet<>();

        Collection<DayOfWeek> days = new HashSet<>(trainingProgram.keySet());

        for (DayOfWeek day : days) {
            if (trainingProgram.get(day).exercises().getLast().name().equals(exerciseName)) {
                result.add(day);
            }
        }

        return result;
    }

    @Override
    public void addExercise(DayOfWeek day, Exercise exercise) throws DayOffException {
        validateExerciseAddition(day, exercise);

        trainingProgram.get(day).exercises().add(exercise);
    }

    @Override
    public void addExercises(DayOfWeek day, List<Exercise> exercises) {
        validateExerciseAddition(day, exercises);

        for (Exercise e : exercises) {
            addExercise(day, e);
        }
    }

    protected void validateExerciseAddition(DayOfWeek day, Exercise exercise) {
        if (day == null) {
            throw new IllegalArgumentException("Day is null");
        }

        if (exercise == null) {
            throw new IllegalArgumentException("Exercise is null");
        }

        if (!trainingProgram.containsKey(day)) {
            throw new DayOffException("You have no exercises this day");
        }
    }

    protected void validateExerciseAddition(DayOfWeek day, List<Exercise> exercises) {
        if (day == null) {
            throw new IllegalArgumentException("Day is null");
        }

        if (exercises == null || exercises.isEmpty()) {
            throw new IllegalArgumentException("Exercise is null");
        }

        if (!trainingProgram.containsKey(day)) {
            throw new DayOffException("You have no workout this day");
        }
    }

    @Override
    public int compareTo(GymMember o) {
        return this.getPersonalIdNumber().compareTo(o.getPersonalIdNumber());
    }

    @Override
    public int hashCode() {
        return this.personalIdNumber.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Member m) {
            return m.personalIdNumber.equals(this.personalIdNumber);
        }

        return false;
    }
}
