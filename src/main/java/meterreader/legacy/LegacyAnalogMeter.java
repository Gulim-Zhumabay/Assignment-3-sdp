package meterreader.legacy;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class LegacyAnalogMeter {

    private final Map<Integer, Double> dials;
    private final Set<Integer> jammed;

    public LegacyAnalogMeter(Map<Integer, Double> dials, Set<Integer> jammed) {
        this.dials = Map.copyOf(dials);
        this.jammed = Set.copyOf(jammed);
    }

    public String readDial(int dialId, int decimals) {
        if (jammed.contains(dialId)) {
            return "ERR_JAMMED";
        }
        Double value = dials.get(dialId);
        if (value == null) {
            return "ERR_NO_DIAL";
        }
        return String.format(Locale.ROOT, "%." + decimals + "f", value);
    }
}
