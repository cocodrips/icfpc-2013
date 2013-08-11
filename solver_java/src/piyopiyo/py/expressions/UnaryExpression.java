package piyopiyo.py.expressions;

/**
 *  Represents {@code (op1 e)}.
 */
public abstract class UnaryExpression extends Expression {
    protected final Expression e;

    protected UnaryExpression(Expression e) {
        this.e = e;
    }

    protected abstract UnaryExpressionFactory factory();
    protected abstract String name();

    public Expression mutate(Expression eNew) {
        if (Math.random() < PROB_MUTATE_SELF) {
            return eNew;
        }
        return factory().create(e.mutate(eNew));
    }

    @Override
    public boolean isGround() {
        return e.isGround();
    }

    @Override
    public String toString() {
        return String.format("(%s %s)", name(), e);
    }

    @Override
    public Expression replaceTerm(Variable x, Variable y) {
        return factory().create(e.replaceTerm(x, y));
    }
}
