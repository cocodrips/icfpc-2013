package piyopiyo.py;
import java.awt.Window;

public class GuessResponse {
	public String message;
	public Status status;
	public long[] values;
	public enum Status {win, mismatch, error}
	
	public long getInput() {
		return values[0];
	}

	public long getExpectedOutput() {
		return values[1];
	}

	public long getYourOutput() {
		return values[2];
	}
}
