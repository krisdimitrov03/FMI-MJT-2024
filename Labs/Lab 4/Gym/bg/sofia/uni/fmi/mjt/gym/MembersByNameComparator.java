package bg.sofia.uni.fmi.mjt.gym;

import bg.sofia.uni.fmi.mjt.gym.member.GymMember;

import java.util.Comparator;

public class MembersByNameComparator implements Comparator<GymMember> {
    @Override
    public int compare(GymMember o1, GymMember o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
