package piyopiyo.py.expressions;

public class Not extends UnaryExpression {
    public Not(Expression e) {
        super(e);
    }

    @Override
    public long eval() { return ~e.eval(); }

    @Override
    public String name() { return "not"; }
}
