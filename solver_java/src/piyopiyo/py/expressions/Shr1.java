package piyopiyo.py.expressions;

public class Shr1 extends UnaryExpression {
    public Shr1(Expression e) {
        super(e);
    }

    @Override
    public long eval() { return e.eval() >>> 1; }

    @Override
    public String name() { return "shr1"; }
}
