package meterreader.reader;

import meterreader.device.MeterDevice;
import meterreader.device.MeterReadException;
import meterreader.model.Reading;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MeterReaderTest {

    private static MeterDevice stub(double value, String unit) {
        return id -> new Reading(id, value, unit, Instant.EPOCH);
    }

    @Test
    void electricity_reader_delegates_and_calculates_cost() {
        List<Integer> calls = new ArrayList<>();
        MeterDevice device = id -> {
            calls.add(id);
            return new Reading(id, 100.0, "kWh", Instant.EPOCH);
        };
        MeterReader reader = new ElectricityReader(device, 2.5);

        String report = reader.report(7);

        assertEquals(List.of(7), calls);
        assertEquals("electricity meter 7: 100.0 kWh, cost 250.00", report);
    }

    @Test
    void water_reader_reports_high_consumption() {
        MeterReader reader = new WaterReader(stub(80.0, "m3"), 50.0);

        assertEquals("water meter 3: 80.0 m3, status high consumption", reader.report(3));
    }

    @Test
    void water_reader_reports_normal_consumption() {
        MeterReader reader = new WaterReader(stub(20.0, "m3"), 50.0);

        assertEquals("water meter 3: 20.0 m3, status normal", reader.report(3));
    }

    @Test
    void device_error_reaches_caller() {
        MeterDevice broken = id -> {
            throw new MeterReadException("boom");
        };
        MeterReader reader = new ElectricityReader(broken, 2.5);

        assertThrows(MeterReadException.class, () -> reader.report(1));
    }
}