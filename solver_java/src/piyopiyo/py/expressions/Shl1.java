package piyopiyo.py.expressions;

public class Shl1 extends UnaryExpression {
    public static UnaryExpressionFactory FACTORY =
        new UnaryExpressionFactory() {
            @Override
            public UnaryExpression create(Expression e) { return new Shl1(e); }
        };

    public Shl1(Expression e) {
        super(e);
    }

    @Override
    public long eval() { return e.eval() << 1; }

    @Override
    public String name() { return "shl1"; }
}
