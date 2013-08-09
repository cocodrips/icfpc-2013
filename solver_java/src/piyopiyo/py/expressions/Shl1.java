package piyopiyo.py.expressions;

public class Shl1 extends UnaryExpression {
    public Shl1(Expression e) {
        super(e);
    }

    @Override
    public long eval() { return e.eval() << 1; }

    @Override
    public String name() { return "shl1"; }
}
