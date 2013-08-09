package piyopiyo.py.expressions;

public class Xor extends BinaryExpression {
    public Xor(Expression e0, Expression e1) {
        super(e0, e1);
    }

    @Override
    public long eval() { return e0.eval() ^ e1.eval(); }

    @Override
    public String name() { return "xor"; }
}
