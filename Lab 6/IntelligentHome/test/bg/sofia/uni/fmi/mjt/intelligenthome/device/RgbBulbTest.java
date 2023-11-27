package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static bg.sofia.uni.fmi.mjt.intelligenthome.device.DeviceType.BULB;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RgbBulbTest {
    private final String name = "bulb";
    private final double powerConsumption = 1.8;
    private final LocalDateTime installationDateTime = LocalDateTime.now().minusHours(2);
    private IoTDevice device;

    @BeforeEach
    void setup() {
        device = new RgbBulb(name, powerConsumption, installationDateTime);
    }

    @Test
    void getIdShouldReturnProperValue() {
        String expectedId = device.getType().getShortName() + "-" + device.getName() + "-" + (IoTDeviceBase.uniqueNumberDevice - 1);

        assertEquals(expectedId, device.getId());
    }

    @Test
    void getTypeShouldReturnProperValue() {
        assertEquals(BULB, device.getType());
    }
}
