package meterreader.device;

import meterreader.model.Reading;

import java.time.Instant;
import java.util.Map;

public class DigitalMeter implements MeterDevice {

    private final Map<Integer, Double> values;
    private final String unit;

    public DigitalMeter(Map<Integer, Double> values, String unit) {
        this.values = Map.copyOf(values);
        this.unit = unit;
    }

    @Override
    public Reading read(int meterId) throws MeterReadException {
        Double value = values.get(meterId);
        if (value == null) {
            throw new MeterReadException("digital meter " + meterId + " not found");
        }
        return new Reading(meterId, value, unit, Instant.now());
    }
}
