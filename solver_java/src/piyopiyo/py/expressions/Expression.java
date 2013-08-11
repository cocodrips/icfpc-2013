package piyopiyo.py.expressions;

public abstract class Expression {
    protected static final double PROB_MUTATE_SELF = 0.25;

    public abstract long eval();
    public abstract Expression mutate(Expression eNew);
    public abstract boolean isGround();
    public boolean isRedundant() {
        return false;
    }
    public abstract Expression replaceTerm(Variable x, Variable y);
    // TODO(yuizumi): Define size() when the need arises.
}
