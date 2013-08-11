package piyopiyo.py.expressions;

/**
 *  Represents {@code (fold e0 e1 (lambda (x y) e2))}.
 */
public class Fold extends Expression {
    private final Expression e0, e1, e2;
    private final Variable x, y;

    public Fold(Expression e0, Expression e1, Variable x, Variable y, Expression e2) {
        this.e0 = e0;
        this.e1 = e1;
        this.x = x;
        this.y = y;
        this.e2 = e2;
    }

    @Override
    public long eval() {
        long bytes = e0.eval();
        long value = e1.eval();
        for (int i = 0; i < 8; i++) {
            x.bind(bytes & 0xFF); y.bind(value);
            value = e2.eval();
            bytes >>>= 8;
        }
        x.unbind();
        y.unbind();
        return value;
    }

    @Override
    public boolean isGround() {
        return e0.isGround() && e1.isGround() && e2.isGround();
    }

    @Override
    public Expression mutate(Expression eNew) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return String.format("(fold %s %s (lambda (%s %s) %s))", e0, e1, x, y, e2);
    }

    @Override
    public boolean isRedudant() {
        return e2 == y;
    }
}
