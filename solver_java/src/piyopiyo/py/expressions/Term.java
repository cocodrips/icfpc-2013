package piyopiyo.py.expressions;

public abstract class Term extends Expression {
    @Override
    public Expression mutate(Expression eNew) {
        return eNew;
    }

    @Override
    public Expression replaceTerm(Variable x, Variable y) {
        double p = Math.random() * 3;
        if (p < 1.0) {
            return x;
        } else if (p < 2.0) {
            return y;
        } else {
            return this;
        }
    }
}
