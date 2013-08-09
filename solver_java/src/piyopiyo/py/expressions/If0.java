package piyopiyo.py.expressions;

/**
 *  Represents {@code (if0 e0 e1 e2)}.
 */
public class If0 extends Expression {
    private final Expression e0, e1, e2;

    public If0(Expression e0, Expression e1, Expression e2) {
        this.e0 = e0;
        this.e1 = e1;
        this.e2 = e2;
    }

    @Override
    public long eval() {
        return e0.eval() == 0 ? e1.eval() : e2.eval();
    }

    @Override
    public String toString() {
        return String.format("(if0 %s %s %s)", e0, e1, e2);
    }
}
