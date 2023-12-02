package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static bg.sofia.uni.fmi.mjt.intelligenthome.device.DeviceType.SMART_SPEAKER;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

public class AmazonAlexaTest {
    private final String name = "alexa";
    private final double powerConsumption = 3.5;
    private final LocalDateTime installationDateTime = LocalDateTime.now().minusHours(2);
    private IoTDevice device;

    @BeforeEach
    void setup() {
        device = new AmazonAlexa(name, powerConsumption, installationDateTime);
    }

    @Test
    void getIdShouldReturnProperValue() {
        String expectedId = device.getType().getShortName() + "-" + device.getName() + "-" + (IoTDeviceBase.uniqueNumberDevice - 1);

        assertEquals(expectedId, device.getId());
    }

    @Test
    void getTypeShouldReturnProperValue() {
        assertEquals(SMART_SPEAKER, device.getType());
    }
}
