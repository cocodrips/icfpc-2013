package piyopiyo.py;

public class EvalRequest {
    public String id;
    public long[] arguments;

    public EvalRequest(String id, long[] arguments) {
        this.id = id;
        this.arguments = arguments;
    }
}
