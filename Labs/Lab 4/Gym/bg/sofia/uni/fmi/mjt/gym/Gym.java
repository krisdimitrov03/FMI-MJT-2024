package bg.sofia.uni.fmi.mjt.gym;

import bg.sofia.uni.fmi.mjt.gym.member.Address;
import bg.sofia.uni.fmi.mjt.gym.member.GymMember;
import bg.sofia.uni.fmi.mjt.gym.workout.Exercise;
import bg.sofia.uni.fmi.mjt.gym.workout.Workout;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class Gym implements GymAPI {
    private HashSet<GymMember> members;
    private int capacity;
    private Address address;

    public Gym(int capacity, Address address) {
        this.address = address;
        this.members = HashSet.newHashSet(capacity);
        this.capacity = capacity;
    }

    @Override
    public SortedSet<GymMember> getMembers() {
        return new TreeSet<>(members);
    }

    @Override
    public SortedSet<GymMember> getMembersSortedByName() {
        Comparator<GymMember> comparator = new MembersByNameComparator();

        SortedSet<GymMember> result = new TreeSet<>(comparator);
        result.addAll(members);

        return result;
    }

    @Override
    public SortedSet<GymMember> getMembersSortedByProximityToGym() {
        Comparator<GymMember> comparator = new MembersByProximityComparator(address);

        SortedSet<GymMember> result = new TreeSet<>(comparator);
        result.addAll(members);

        return result;
    }

    @Override
    public void addMember(GymMember member) throws GymCapacityExceededException {
        if (member == null) {
            throw new IllegalArgumentException("Member is null");
        }

        if (members.size() >= capacity) {
            throw new GymCapacityExceededException("Gym is full");
        }

        members.add(member);
    }

    @Override
    public void addMembers(Collection<GymMember> members) throws GymCapacityExceededException {
        if (members == null || members.isEmpty()) {
            throw new IllegalArgumentException("Member is null or empty");
        }

        if (this.members.size() + members.size() > capacity) {
            throw new GymCapacityExceededException("Gym is full");
        }

        for (GymMember m : members) {
            addMember(m);
        }
    }

    @Override
    public boolean isMember(GymMember member) {
        if (member == null) {
            throw new IllegalArgumentException("Member is null");
        }

        return this.members.contains(member);
    }

    @Override
    public boolean isExerciseTrainedOnDay(String exerciseName, DayOfWeek day) {
        if (day == null) {
            throw new IllegalArgumentException("Day is null");
        }

        if (exerciseName == null || exerciseName.isEmpty()) {
            throw new IllegalArgumentException("Exercise is null or empty");
        }

        for (GymMember m : members) {
            Map<DayOfWeek, Workout> program = m.getTrainingProgram();

            if (program.containsKey(day)) {
                for (Exercise e : program.get(day).exercises()) {
                    if (e.name().equals(exerciseName)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public Map<DayOfWeek, List<String>> getDailyListOfMembersForExercise(String exerciseName) {
        if (exerciseName == null || exerciseName.isEmpty()) {
            throw new IllegalArgumentException("Exercise is null or empty");
        }

        Map<DayOfWeek, List<String>> result = new HashMap<>();

        for (GymMember m : this.members) {
            var program = m.getTrainingProgram();
            var daysTraining = program.keySet();

            for (DayOfWeek d : daysTraining) {
                for (Exercise e : program.get(d).exercises()) {
                    if (e.name().equals(exerciseName)) {
                        if (!result.containsKey(d)) {
                            result.put(d, new ArrayList<>());
                        }

                        result.get(d).add(m.getName());
                        break;
                    }
                }
            }
        }

        return result;
    }
}
