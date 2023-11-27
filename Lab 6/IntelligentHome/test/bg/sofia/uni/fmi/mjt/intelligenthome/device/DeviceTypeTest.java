package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import org.junit.jupiter.api.Test;

import static bg.sofia.uni.fmi.mjt.intelligenthome.device.DeviceType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeviceTypeTest {

    @Test
    void constructorShouldSetShortNameProperly() {
        assertEquals("SPKR", SMART_SPEAKER.getShortName(), "Short name of SmartSpeaker must be SPKR");
        assertEquals("BLB", BULB.getShortName(), "Short name of Bulb must be BLB");
        assertEquals("TMST", THERMOSTAT.getShortName(), "Short name of Thermostat must be TMST");
    }
}
