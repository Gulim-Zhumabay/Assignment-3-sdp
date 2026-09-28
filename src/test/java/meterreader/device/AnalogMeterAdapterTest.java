package meterreader.device;

import meterreader.legacy.LegacyAnalogMeter;
import meterreader.model.Reading;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnalogMeterAdapterTest {

    private static LegacyAnalogMeter legacyReturning(String raw) {
        return new LegacyAnalogMeter(Map.of(), Set.of()) {
            @Override
            public String readDial(int dialId, int decimals) {
                return raw;
            }
        };
    }

    @Test
    void converts_string_to_reading() {
        MeterDevice device = new AnalogMeterAdapter(legacyReturning("12.5"), "kWh");

        Reading reading = device.read(4);

        assertEquals(4, reading.getMeterId());
        assertEquals(12.5, reading.getValue());
        assertEquals("kWh", reading.getUnit());
    }

    @Test
    void passes_meter_id_and_decimals_to_legacy_meter() {
        int[] seen = new int[2];
        LegacyAnalogMeter legacy = new LegacyAnalogMeter(Map.of(), Set.of()) {
            @Override
            public String readDial(int dialId, int decimals) {
                seen[0] = dialId;
                seen[1] = decimals;
                return "1.0";
            }
        };

        new AnalogMeterAdapter(legacy, "m3").read(9);

        assertEquals(9, seen[0]);
        assertEquals(1, seen[1]);
    }

    @Test
    void jammed_dial_becomes_meter_read_exception() {
        MeterDevice device = new AnalogMeterAdapter(legacyReturning("ERR_JAMMED"), "kWh");

        MeterReadException e = assertThrows(MeterReadException.class, () -> device.read(5));

        assertTrue(e.getMessage().contains("jammed"));
        assertFalse(e.getMessage().contains("ERR_"));
    }

    @Test
    void missing_dial_becomes_meter_read_exception() {
        MeterDevice device = new AnalogMeterAdapter(legacyReturning("ERR_NO_DIAL"), "kWh");

        MeterReadException e = assertThrows(MeterReadException.class, () -> device.read(99));

        assertTrue(e.getMessage().contains("not found"));
        assertFalse(e.getMessage().contains("ERR_"));
    }

    @Test
    void unreadable_text_becomes_meter_read_exception() {
        MeterDevice device = new AnalogMeterAdapter(legacyReturning("abc"), "kWh");

        MeterReadException e = assertThrows(MeterReadException.class, () -> device.read(3));

        assertInstanceOf(NumberFormatException.class, e.getCause());
    }

    @Test
    void null_value_becomes_meter_read_exception() {
        MeterDevice device = new AnalogMeterAdapter(legacyReturning(null), "kWh");

        assertThrows(MeterReadException.class, () -> device.read(3));
    }

    @Test
    void works_with_real_legacy_meter() {
        LegacyAnalogMeter legacy = new LegacyAnalogMeter(Map.of(1, 5.0, 2, 7.0), Set.of(2));
        MeterDevice device = new AnalogMeterAdapter(legacy, "kWh");

        assertEquals(5.0, device.read(1).getValue());
        assertThrows(MeterReadException.class, () -> device.read(2));
    }
}
