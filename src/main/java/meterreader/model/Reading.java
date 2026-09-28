package meterreader.model;

import java.time.Instant;

public final class Reading {

    private final int meterId;
    private final double value;
    private final String unit;
    private final Instant timestamp;

    public Reading(int meterId, double value, String unit, Instant timestamp) {
        this.meterId = meterId;
        this.value = value;
        this.unit = unit;
        this.timestamp = timestamp;
    }

    public int getMeterId() {
        return meterId;
    }

    public double getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "reading{meterId=" + meterId + ", value=" + value + " " + unit + ", at=" + timestamp + "}";
    }
}