package piyopiyo.py.expressions;

public abstract class Term extends Expression {
    @Override
    public Expression mutate(Expression eNew) {
        return eNew;
    }
    @Override
    public boolean isRedundant() {
        return false;
    }
}
