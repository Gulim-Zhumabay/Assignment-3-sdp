package meterreader.reader;

import meterreader.device.MeterDevice;
import meterreader.model.Reading;

public abstract class MeterReader {

    private final MeterDevice device;

    protected MeterReader(MeterDevice device) {
        this.device = device;
    }

    public Reading read(int meterId) {
        return device.read(meterId);
    }

    public abstract String report(int meterId);
}
