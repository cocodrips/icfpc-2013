package piyopiyo.py;
import java.awt.Window;

public class GuessResponse {
	public String message;
	public Status status;
	public long[] values;
	public enum Status {win, mismatch, error}
	
	public long getArg() {
		return values[0];
	}

	public long getAns() {
		return values[1];
	}

	public long getWrong() {
		return values[2];
	}
}
