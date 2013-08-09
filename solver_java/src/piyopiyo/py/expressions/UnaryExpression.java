package piyopiyo.py.expressions;

/**
 *  Represents {@code (op1 e)}.
 */
public abstract class UnaryExpression extends Expression {
    protected final Expression e;

    protected UnaryExpression(Expression e) {
        this.e = e;
    }

    protected abstract String name();

    @Override
    public String toString() {
        return String.format("(%s %s)", name(), e);
    }
}
