package meterreader.device;

import meterreader.legacy.LegacyAnalogMeter;
import meterreader.model.Reading;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DeviceSelectorTest {

    private static final MeterDevice STUB = id -> new Reading(id, 1.0, "kWh", Instant.EPOCH);

    @Test
    void returns_registered_device() {
        DeviceSelector selector = new DeviceSelector();
        selector.register("digital", STUB);

        assertSame(STUB, selector.select("digital"));
    }

    @Test
    void selection_ignores_case() {
        DeviceSelector selector = new DeviceSelector();
        selector.register("Wireless", STUB);

        assertSame(STUB, selector.select("WIRELESS"));
    }

    @Test
    void unknown_type_throws() {
        DeviceSelector selector = new DeviceSelector();

        assertThrows(IllegalArgumentException.class, () -> selector.select("satellite"));
    }

    @Test
    void selected_adapter_works_like_any_other_device() {
        DeviceSelector selector = new DeviceSelector();
        LegacyAnalogMeter legacy = new LegacyAnalogMeter(Map.of(4, 2.0), Set.of());
        selector.register("analog", new AnalogMeterAdapter(legacy, "kWh"));

        assertEquals(2.0, selector.select("analog").read(4).getValue());
    }
}