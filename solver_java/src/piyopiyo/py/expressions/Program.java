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

    public Program mutate(Expression eNew) {
        return new Program(x, e.mutate(eNew));
    }

    @Override
    public String toString() {
        return String.format("(lambda (%s) %s)", x, e);
    }
}
