package meterreader.device;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DeviceSelector {

    private final Map<String, MeterDevice> devices = new HashMap<>();

    public void register(String connectionType, MeterDevice device) {
        devices.put(connectionType.toLowerCase(Locale.ROOT), device);
    }

    public MeterDevice select(String connectionType) {
        MeterDevice device = devices.get(connectionType.toLowerCase(Locale.ROOT));
        if (device == null) {
            throw new IllegalArgumentException("unknown connection type: " + connectionType);
        }
        return device;
    }
}
