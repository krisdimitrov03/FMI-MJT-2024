package bg.sofia.uni.fmi.mjt.intelligenthome.center.comparator;

import bg.sofia.uni.fmi.mjt.intelligenthome.device.AmazonAlexa;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.IoTDevice;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.RgbBulb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegistrationComparatorTest {
    private Comparator<IoTDevice> comparator;

    private IoTDevice firstDevice;
    private IoTDevice secondDevice;

    @BeforeEach
    void setup() {
        comparator = new RegistrationComparator();

        firstDevice = new RgbBulb("bulb", 1.8, LocalDateTime.now());
        secondDevice = new AmazonAlexa("alexa", 3.5, LocalDateTime.now());
    }

    @Test
    void compareShouldReturnMinusOneIfLeftIsLess() {
        firstDevice.setRegistration(LocalDateTime.now().minusHours(3));
        secondDevice.setRegistration(LocalDateTime.now().minusHours(5));

        assertEquals(-1, comparator.compare(firstDevice, secondDevice));
    }

    @Test
    void compareShouldReturnZeroIfLeftIsEqualToRight() {
        firstDevice.setRegistration(LocalDateTime.now().minusHours(3));
        secondDevice.setRegistration(LocalDateTime.now().minusHours(3));

        assertEquals(0, comparator.compare(firstDevice, secondDevice));
    }

    @Test
    void compareShouldReturnOneIfRightIsLess() {
        firstDevice.setRegistration(LocalDateTime.now().minusHours(5));
        secondDevice.setRegistration(LocalDateTime.now().minusHours(3));

        assertEquals(1, comparator.compare(firstDevice, secondDevice));
    }
}
