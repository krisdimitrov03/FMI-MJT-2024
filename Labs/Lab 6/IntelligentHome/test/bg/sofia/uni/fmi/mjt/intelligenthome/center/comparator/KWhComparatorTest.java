package bg.sofia.uni.fmi.mjt.intelligenthome.center.comparator;

import bg.sofia.uni.fmi.mjt.intelligenthome.device.AmazonAlexa;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.IoTDevice;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.RgbBulb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KWhComparatorTest {
    private final LocalDateTime installationDateTime = LocalDateTime.now().minusHours(2);
    private Comparator<IoTDevice> comparator;

    @BeforeEach
    void setup() {
        comparator = new KWhComparator();
    }

    @Test
    void compareShouldReturnMinusOneIfLeftIsLess() {
        IoTDevice firstDevice = new RgbBulb("bulb", 1.8, installationDateTime);
        IoTDevice secondDevice = new AmazonAlexa("alexa", 3.5, installationDateTime);

        assertEquals(-1, comparator.compare(firstDevice, secondDevice));
    }

    @Test
    void compareShouldReturnZeroIfLeftIsEqualToRight() {
        IoTDevice firstDevice = new RgbBulb("bulb", 1.8, installationDateTime);
        IoTDevice secondDevice = new AmazonAlexa("alexa", 1.8, installationDateTime);

        assertEquals(0, comparator.compare(firstDevice, secondDevice));
    }

    @Test
    void compareShouldReturnOneIfRightIsLess() {
        IoTDevice firstDevice = new RgbBulb("bulb", 2.0, installationDateTime);
        IoTDevice secondDevice = new AmazonAlexa("alexa", 1.8, installationDateTime);

        assertEquals(1, comparator.compare(firstDevice, secondDevice));
    }
}
