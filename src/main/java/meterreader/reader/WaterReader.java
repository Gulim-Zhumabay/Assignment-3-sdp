package meterreader.reader;

import meterreader.device.MeterDevice;
import meterreader.model.Reading;

import java.util.Locale;

public class WaterReader extends MeterReader {

    private final double warningLimit;

    public WaterReader(MeterDevice device, double warningLimit) {
        super(device);
        this.warningLimit = warningLimit;
    }

    @Override
    public String report(int meterId) {
        Reading reading = read(meterId);
        String status = reading.getValue() > warningLimit ? "high consumption" : "normal";
        return String.format(Locale.ROOT, "water meter %d: %.1f %s, status %s",
                meterId, reading.getValue(), reading.getUnit(), status);
    }
}
