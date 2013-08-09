package piyopiyo.py.expressions;

/**
 *  Represents {@code (op2 e0 e1)}.
 */
public abstract class BinaryExpression extends Expression {
    protected final Expression e0, e1;

    protected BinaryExpression(Expression e0, Expression e1) {
        this.e0 = e0;
        this.e1 = e1;
    }

    protected abstract String name();

    @Override
    public String toString() {
        return String.format("(%s %s %s)", name(), e0, e1);
    }
}
