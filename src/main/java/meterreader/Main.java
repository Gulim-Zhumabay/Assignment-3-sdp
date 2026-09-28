package meterreader;

import meterreader.device.AnalogMeterAdapter;
import meterreader.device.DeviceSelector;
import meterreader.device.DigitalMeter;
import meterreader.device.MeterDevice;
import meterreader.device.MeterReadException;
import meterreader.device.WirelessMeter;
import meterreader.legacy.LegacyAnalogMeter;
import meterreader.reader.ElectricityReader;
import meterreader.reader.MeterReader;
import meterreader.reader.WaterReader;

import java.util.Map;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        DeviceSelector electricity = new DeviceSelector();
        electricity.register("digital", new DigitalMeter(Map.of(1, 1250.5, 2, 830.0), "kWh"));
        electricity.register("wireless", new WirelessMeter(Map.of(3, 402.7), "kWh", 80));
        electricity.register("analog", new AnalogMeterAdapter(
                new LegacyAnalogMeter(Map.of(4, 2210.3, 5, 97.8), Set.of(5)), "kWh"));

        DeviceSelector water = new DeviceSelector();
        water.register("digital", new DigitalMeter(Map.of(6, 45.2), "m3"));
        water.register("wireless", new WirelessMeter(Map.of(7, 12.9), "m3", 10));
        water.register("analog", new AnalogMeterAdapter(
                new LegacyAnalogMeter(Map.of(8, 130.4), Set.of()), "m3"));

        Map<String, DeviceSelector> selectors = Map.of("electricity", electricity, "water", water);

        String[][] requests = {
                {"electricity", "digital", "1"},
                {"electricity", "wireless", "3"},
                {"electricity", "analog", "4"},
                {"electricity", "analog", "5"},
                {"water", "digital", "6"},
                {"water", "wireless", "7"},
                {"water", "analog", "8"},
                {"water", "analog", "99"},
                {"water", "satellite", "1"}
        };

        for (String[] request : requests) {
            String resource = request[0];
            String connection = request[1];
            int meterId = Integer.parseInt(request[2]);
            try {
                DeviceSelector selector = selectors.get(resource);
                if (selector == null) {
                    throw new IllegalArgumentException("unknown resource: " + resource);
                }
                MeterReader reader = createReader(resource, selector.select(connection));
                System.out.println(reader.report(meterId));
            } catch (MeterReadException | IllegalArgumentException e) {
                System.out.println("error: " + e.getMessage());
            }
        }
    }

    private static MeterReader createReader(String resource, MeterDevice device) {
        return switch (resource) {
            case "electricity" -> new ElectricityReader(device, 25.0);
            case "water" -> new WaterReader(device, 100.0);
            default -> throw new IllegalArgumentException("unknown resource: " + resource);
        };
    }
}