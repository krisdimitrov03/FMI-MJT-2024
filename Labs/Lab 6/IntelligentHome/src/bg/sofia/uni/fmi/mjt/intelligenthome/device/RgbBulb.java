package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import java.time.LocalDateTime;

public class RgbBulb extends IoTDeviceBase {
    public RgbBulb(String name, double powerConsumption, LocalDateTime installationDateTime) {
        super(name, powerConsumption, installationDateTime);

        super.setType(DeviceType.BULB);
        super.setDeviceID(uniqueNumberDevice);

        uniqueNumberDevice++;
    }

}
