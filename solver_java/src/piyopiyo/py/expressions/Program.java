package piyopiyo.py.expressions;

/**
 *  Represents a program, i.e. {@code (lambda (x) e)}.
 */
public class Program {
    private final Variable x;
    private final Expression e;

    public Program(Variable x, Expression e) {
        this.x = x;
        this.e = e;
    }

    public long eval(long input) {
        x.bind(input);
        long value = e.eval();
        x.unbind();
        return value;
    }

    @Override
    public String toString() {
        return String.format("(lambda (%s) %s)", x, e);
    }
}
