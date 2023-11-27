package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import java.time.LocalDateTime;

public class AmazonAlexa extends IoTDeviceBase {
    public AmazonAlexa(String name, double powerConsumption, LocalDateTime installationDateTime) {
        super(name, powerConsumption, installationDateTime);

        super.setType(DeviceType.SMART_SPEAKER);
        super.setDeviceID(uniqueNumberDevice);

        uniqueNumberDevice++;
    }
}
