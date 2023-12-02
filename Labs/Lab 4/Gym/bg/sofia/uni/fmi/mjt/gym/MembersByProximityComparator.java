package bg.sofia.uni.fmi.mjt.gym;

import bg.sofia.uni.fmi.mjt.gym.member.Address;
import bg.sofia.uni.fmi.mjt.gym.member.GymMember;

import java.util.Comparator;

public class MembersByProximityComparator implements Comparator<GymMember> {
    private Address gymAddress;

    public MembersByProximityComparator(Address gymAddress) {
        this.gymAddress = gymAddress;
    }

    @Override
    public int compare(GymMember o1, GymMember o2) {
        double o1Dist = o1.getAddress().getDistanceTo(gymAddress);
        double o2Dist = o2.getAddress().getDistanceTo(gymAddress);

        return Double.compare(o1Dist, o2Dist);
    }
}
