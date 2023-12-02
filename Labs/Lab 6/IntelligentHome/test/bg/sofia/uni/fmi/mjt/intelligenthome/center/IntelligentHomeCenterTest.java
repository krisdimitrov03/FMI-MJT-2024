package bg.sofia.uni.fmi.mjt.intelligenthome.center;

import bg.sofia.uni.fmi.mjt.intelligenthome.center.exceptions.DeviceAlreadyRegisteredException;
import bg.sofia.uni.fmi.mjt.intelligenthome.center.exceptions.DeviceNotFoundException;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.AmazonAlexa;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.DeviceType;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.IoTDevice;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.RgbBulb;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.WiFiThermostat;
import bg.sofia.uni.fmi.mjt.intelligenthome.storage.DeviceStorage;
import bg.sofia.uni.fmi.mjt.intelligenthome.storage.MapDeviceStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class IntelligentHomeCenterTest {
    private IntelligentHomeCenter homeCenter;

    private IoTDevice device;

    @BeforeEach
    void setup() {
        DeviceStorage storage = new MapDeviceStorage();
        homeCenter = new IntelligentHomeCenter(storage);
        device = new AmazonAlexa("alexa", 3.5, LocalDateTime.now().plusHours(2));
    }

    @Test
    void registerShouldThrowIfDeviceNull() throws IllegalArgumentException {
        assertThrows(IllegalArgumentException.class, () -> homeCenter.register(null));
    }

    @Test
    void registerShouldThrowIfDeviceAlreadyRegistered() throws DeviceAlreadyRegisteredException {
        homeCenter.register(device);

        assertThrows(DeviceAlreadyRegisteredException.class, () -> homeCenter.register(device));
    }

    @Test
    void registerShouldAddDeviceToStorage() throws DeviceAlreadyRegisteredException, DeviceNotFoundException {
        homeCenter.register(device);

        assertEquals(device, homeCenter.getDeviceById(device.getId()));
    }

    @Test
    void unregisterShouldThrowIfDeviceNull() {
        assertThrows(IllegalArgumentException.class, () -> homeCenter.unregister(null));
    }

    @Test
    void unregisterShouldThrowIfDeviceNotFound() {
        assertThrows(DeviceNotFoundException.class, () -> homeCenter.unregister(device));
    }

    @Test
    void unregisterShouldRemoveDeviceFromStorage() throws DeviceNotFoundException, DeviceAlreadyRegisteredException {
        homeCenter.register(device);
        homeCenter.unregister(device);

        assertThrows(DeviceNotFoundException.class, () -> homeCenter.getDeviceById(device.getId()));
    }

    @Test
    void getDeviceByIdShouldThrowIfIdNull() {
        assertThrows(IllegalArgumentException.class, () -> homeCenter.getDeviceById(null));
    }

    @Test
    void getDeviceByIdShouldThrowIfDeviceNotFound() {
        assertThrows(DeviceNotFoundException.class, () -> homeCenter.getDeviceById(device.getId()));
    }

    @Test
    void getDeviceByIdShouldReturnTheDevice() throws DeviceAlreadyRegisteredException, DeviceNotFoundException {
        homeCenter.register(device);

        assertEquals(device, homeCenter.getDeviceById(device.getId()));
    }

    @Test
    void getDeviceQuantityPerTypeShouldThrowIfTypeNull() {
        assertThrows(IllegalArgumentException.class, () -> homeCenter.getDeviceQuantityPerType(null));
    }

    @Test
    void getDeviceQuantityPerTypeShouldReturnProperQuantity() throws DeviceAlreadyRegisteredException {
        IoTDevice firstBulb = new RgbBulb("rgb-bulb", 2.5, LocalDateTime.now());
        IoTDevice secondBulb = new RgbBulb("cmyk-bulb", 1.8, LocalDateTime.now());

        homeCenter.register(device);
        homeCenter.register(firstBulb);
        homeCenter.register(secondBulb);

        assertEquals(2, homeCenter.getDeviceQuantityPerType(DeviceType.BULB));
        assertEquals(1, homeCenter.getDeviceQuantityPerType(DeviceType.SMART_SPEAKER));
        assertEquals(0, homeCenter.getDeviceQuantityPerType(DeviceType.THERMOSTAT));
    }

    @Test
    void getTopNDevicesByPowerConsumptionShouldThrowIfNIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> homeCenter.getTopNDevicesByPowerConsumption(-1));
        assertThrows(IllegalArgumentException.class, () -> homeCenter.getTopNDevicesByPowerConsumption(0));
    }

    @Test
    void getTopNDevicesByPowerConsumptionShouldReturnProperCollection() throws DeviceAlreadyRegisteredException {
        IoTDevice firstBulb = new RgbBulb("rgb-bulb", 2.5, LocalDateTime.now().plusHours(2));
        IoTDevice secondBulb = new RgbBulb("cmyk-bulb", 1.8, LocalDateTime.now().plusHours(2));
        IoTDevice thermostat = new WiFiThermostat("tst", 0.5, LocalDateTime.now().plusHours(2));

        homeCenter.register(device);
        homeCenter.register(firstBulb);
        homeCenter.register(secondBulb);
        homeCenter.register(thermostat);

        Collection<String> expected = List.of(device.getId(), firstBulb.getId());

        assertIterableEquals(expected, homeCenter.getTopNDevicesByPowerConsumption(2));
    }

    @Test
    void getTopNDevicesByRegistrationShouldThrowIfNIsNegativeOrZero() {
        assertThrows(IllegalArgumentException.class, () -> homeCenter.getFirstNDevicesByRegistration(-1));
        assertThrows(IllegalArgumentException.class, () -> homeCenter.getFirstNDevicesByRegistration(0));
    }

    @Test
    void getTopNDevicesByRegistrationShouldReturnProperCollection() throws DeviceAlreadyRegisteredException {
        IoTDevice firstBulb = new RgbBulb("rgb-bulb", 2.5, LocalDateTime.now());
        IoTDevice secondBulb = new RgbBulb("cmyk-bulb", 1.8, LocalDateTime.now());
        IoTDevice thermostat = new WiFiThermostat("tst", 0.5, LocalDateTime.now());

        homeCenter.register(device);
        device.setRegistration(LocalDateTime.now().minusHours(4));

        homeCenter.register(firstBulb);
        firstBulb.setRegistration(LocalDateTime.now().minusHours(3));

        homeCenter.register(secondBulb);
        secondBulb.setRegistration(LocalDateTime.now().minusHours(2));

        homeCenter.register(thermostat);
        thermostat.setRegistration(LocalDateTime.now().minusHours(1));

        Collection<IoTDevice> expected = List.of(thermostat, secondBulb);

        assertIterableEquals(expected, homeCenter.getFirstNDevicesByRegistration(2));
    }
}
