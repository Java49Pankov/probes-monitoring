package telran.probes.exceptions;

@SuppressWarnings("serial")
public class IllegalSensorRangeException extends IllegalStateException {
	public IllegalSensorRangeException() {
		super("max value must be greater than min value");
	}

}