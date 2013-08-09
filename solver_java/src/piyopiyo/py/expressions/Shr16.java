package piyopiyo.py.expressions;

public class Shr16 extends UnaryExpression {
    public Shr16(Expression e) {
        super(e);
    }

    @Override
    public long eval() { return e.eval() >>> 16; }

    @Override
    public String name() { return "shr16"; }
}
