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

    protected abstract BinaryExpressionFactory factory();
    protected abstract String name();

    @Override
    public Expression mutate(Expression eNew) {
        if (Math.random() < PROB_MUTATE_SELF) {
            return eNew;
        }
        if (Math.random() < 0.5) {
            return factory().create(e0.mutate(eNew), e1);
        } else {
            return factory().create(e0, e1.mutate(eNew));
        }
    }

    @Override
    public boolean isGround() {
        return e0.isGround() && e1.isGround();
    }

    @Override
    public String toString() {
        return String.format("(%s %s %s)", name(), e0, e1);
    }
}
