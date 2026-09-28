package meterreader.device;

import meterreader.model.Reading;

import java.time.Instant;
import java.util.Map;

public class WirelessMeter implements MeterDevice {

    private static final int MIN_SIGNAL = 20;

    private final Map<Integer, Double> values;
    private final String unit;
    private final int signalStrength;

    public WirelessMeter(Map<Integer, Double> values, String unit, int signalStrength) {
        this.values = Map.copyOf(values);
        this.unit = unit;
        this.signalStrength = signalStrength;
    }

    @Override
    public Reading read(int meterId) throws MeterReadException {
        if (signalStrength < MIN_SIGNAL) {
            throw new MeterReadException("wireless meter " + meterId + ": signal too weak");
        }
        Double value = values.get(meterId);
        if (value == null) {
            throw new MeterReadException("wireless meter " + meterId + " not found");
        }
        return new Reading(meterId, value, unit, Instant.now());
    }
}
