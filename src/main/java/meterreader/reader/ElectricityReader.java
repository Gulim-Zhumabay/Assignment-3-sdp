package meterreader.reader;

import meterreader.device.MeterDevice;
import meterreader.model.Reading;

import java.util.Locale;

public class ElectricityReader extends MeterReader {

    private final double tariffPerKwh;

    public ElectricityReader(MeterDevice device, double tariffPerKwh) {
        super(device);
        this.tariffPerKwh = tariffPerKwh;
    }

    @Override
    public String report(int meterId) {
        Reading reading = read(meterId);
        double cost = reading.getValue() * tariffPerKwh;
        return String.format(Locale.ROOT, "electricity meter %d: %.1f %s, cost %.2f",
                meterId, reading.getValue(), reading.getUnit(), cost);
    }
}
