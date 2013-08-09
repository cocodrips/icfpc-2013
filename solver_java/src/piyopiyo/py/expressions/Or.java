package piyopiyo.py.expressions;

public class Or extends BinaryExpression {
    public Or(Expression e0, Expression e1) {
        super(e0, e1);
    }

    @Override
    public long eval() { return e0.eval() | e1.eval(); }

    @Override
    public String name() { return "or"; }
}
