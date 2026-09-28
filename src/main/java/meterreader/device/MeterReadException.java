package meterreader.device;

public class MeterReadException extends RuntimeException {

    public MeterReadException(String message) {
        super(message);
    }

    public MeterReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
