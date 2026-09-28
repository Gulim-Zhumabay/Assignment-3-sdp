package meterreader.device;

import meterreader.model.Reading;

public interface MeterDevice {

    Reading read(int meterId) throws MeterReadException;
}