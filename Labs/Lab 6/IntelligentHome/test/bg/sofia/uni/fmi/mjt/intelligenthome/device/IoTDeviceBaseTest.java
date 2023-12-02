package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IoTDeviceBaseTest {
    private final String name = "RgbBulb";
    private final double powerConsumption = 3.5;
    private final LocalDateTime installationDateTime = LocalDateTime.now().minusHours(2);
    private final LocalDateTime registration = LocalDateTime.now().minusHours(10);

    private IoTDeviceBase device;

    @BeforeEach
    void setup() {
        device = new IoTDeviceBase(name, powerConsumption, installationDateTime) {};
    }

    @Test
    void getNameShouldReturnProperValue() {
        assertEquals(name, device.getName());
    }

    @Test
    void getPowerConsumptionShouldReturnProperValue() {
        assertEquals(powerConsumption, device.getPowerConsumption());
    }

    @Test
    void getInstallationDateTimeShouldReturnProperValue() {
        assertEquals(installationDateTime, device.getInstallationDateTime());
    }

    @Test
    void setRegistrationShouldSetProperValue() {
        device.setRegistration(registration);

        assertEquals(10, device.getRegistration());
    }

    @Test
    void getPowerConsumptionKWhShouldReturnProperValue() {
        assertEquals(2 * powerConsumption, device.getPowerConsumptionKWh());
    }
}
