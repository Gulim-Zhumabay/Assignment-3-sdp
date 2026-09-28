package meterreader.device;

import meterreader.legacy.LegacyAnalogMeter;
import meterreader.model.Reading;

import java.time.Instant;

public class AnalogMeterAdapter implements MeterDevice {

    private static final int DECIMALS = 1;
    private static final String ERR_JAMMED = "ERR_JAMMED";
    private static final String ERR_NO_DIAL = "ERR_NO_DIAL";

    private final LegacyAnalogMeter legacyMeter;
    private final String unit;

    public AnalogMeterAdapter(LegacyAnalogMeter legacyMeter, String unit) {
        this.legacyMeter = legacyMeter;
        this.unit = unit;
    }

    @Override
    public Reading read(int meterId) throws MeterReadException {
        String raw = legacyMeter.readDial(meterId, DECIMALS);
        if (ERR_JAMMED.equals(raw)) {
            throw new MeterReadException("analog meter " + meterId + ": dial jammed");
        }
        if (ERR_NO_DIAL.equals(raw)) {
            throw new MeterReadException("analog meter " + meterId + " not found");
        }
        try {
            return new Reading(meterId, Double.parseDouble(raw), unit, Instant.now());
        } catch (NumberFormatException | NullPointerException e) {
            throw new MeterReadException("analog meter " + meterId + ": unreadable value", e);
        }
    }
}
