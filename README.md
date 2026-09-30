# Meter Reader

This is my Assignment 3 for the Software Design Patterns course: Adapter and Bridge used together in one system.

## Project theme

The project reads utility meters. A report can be about electricity or water, and each report gets its value from a meter device: a digital one, a wireless one, or an old analog meter with a dial. The two things vary independently, which is why the domain fits Bridge, and the old analog meter has a genuinely different interface, which is why it needs an Adapter.

## Package structure

- meterreader - Main, the demo entry point.
- meterreader.reader - MeterReader (abstract), ElectricityReader, WaterReader. This is the abstraction side of the Bridge.
- meterreader.device - the MeterDevice interface and its three implementations (DigitalMeter, WirelessMeter, AnalogMeterAdapter), plus DeviceSelector and MeterReadException.
- meterreader.legacy - LegacyAnalogMeter, the incompatible class. It is never modified.
- meterreader.model - Reading, the small immutable value every device returns.

## How the patterns are used

**Bridge.** MeterReader holds a MeterDevice and only ever talks to it through that interface, so it never knows which concrete device it has. ElectricityReader turns a reading into a cost using a tariff; WaterReader flags high consumption against a limit. A new report type or a new device type can be added on its own, without touching the other side.

**Adapter.** LegacyAnalogMeter does not fit MeterDevice in three separate ways: its method is readDial(int dialId, int decimals) instead of read(int meterId), it returns a String instead of a Reading, and it never throws — on failure it returns the strings ERR_JAMMED or ERR_NO_DIAL. AnalogMeterAdapter wraps it, parses the string into a Reading, and turns every failure into a MeterReadException, so nothing about the legacy class leaks out. Only the adapter and Main know that LegacyAnalogMeter exists.

**Dynamic implementor selection** (the required extra module). DeviceSelector keeps a map from a connection type ("digital", "wireless", "analog") to a MeterDevice. Main goes through a list of requests, each with a resource, a connection type, and a meter id, and picks both the reader and the device from those strings at runtime — the adapter is chosen exactly the same way as any native device.

## Build, run, test

Requires Java 25 and Maven.