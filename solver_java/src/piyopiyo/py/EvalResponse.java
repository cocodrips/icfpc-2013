package piyopiyo.py;

public class EvalResponse {
    public Status status;
    public long[] outputs;
    public String message;

    public enum Status { ok, error }
}
