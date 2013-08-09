package piyopiyo.py.expressions;

public class Shr4 extends UnaryExpression {
    public Shr4(Expression e) {
        super(e);
    }

    @Override
    public long eval() { return e.eval() >>> 4; }

    @Override
    public String name() { return "shr4"; }
}
