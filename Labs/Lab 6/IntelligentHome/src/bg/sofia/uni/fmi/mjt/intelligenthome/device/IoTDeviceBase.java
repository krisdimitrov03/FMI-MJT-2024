package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import java.time.Duration;
import java.time.LocalDateTime;

public abstract class IoTDeviceBase implements IoTDevice {
    private String id;
    DeviceType type;
    static int uniqueNumberDevice = 0;
    String name;
    double powerConsumption;
    LocalDateTime installationDateTime;
    LocalDateTime registration;

    public IoTDeviceBase(String name, double powerConsumption, LocalDateTime installationDateTime) {
        this.name = name;
        this.powerConsumption = powerConsumption;
        this.installationDateTime = installationDateTime;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPowerConsumption() {
        return powerConsumption;
    }

    @Override
    public LocalDateTime getInstallationDateTime() {
        return installationDateTime;
    }

    @Override
    public DeviceType getType() {
        return type;
    }

    protected void setType(DeviceType type) {
        this.type = type;
    }

    protected void setDeviceID(int uniqueNumberDevice) {
        id = type.getShortName() + '-' + name + '-' + uniqueNumberDevice;
    }

    @Override
    public long getRegistration() {
        return Duration.between(registration, LocalDateTime.now()).toHours();
    }

    @Override
    public void setRegistration(LocalDateTime registration) {
        this.registration = registration;
    }

    @Override
    public long getPowerConsumptionKWh() {
        long duration = Duration.between(getInstallationDateTime(), LocalDateTime.now()).toHours();
        return (long) (duration *  powerConsumption);
    }

}
