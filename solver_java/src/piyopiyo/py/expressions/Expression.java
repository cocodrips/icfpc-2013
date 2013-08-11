package piyopiyo.py.expressions;

public abstract class Expression {
    protected static final double PROB_MUTATE_SELF = 0.25;

    public abstract long eval();
    public abstract Expression mutate(Expression eNew);

    // TODO(yuizumi): Define size() when the need arises.
}
