package piyopiyo.py.expressions;

public class Shr1 extends UnaryExpression {
    public static UnaryExpressionFactory FACTORY =
        new UnaryExpressionFactory() {
            @Override
            public UnaryExpression create(Expression e) { return new Shr1(e); }
        };

    public Shr1(Expression e) {
        super(e);
    }

    @Override
    public long eval() { return e.eval() >>> 1; }

    @Override
    public UnaryExpressionFactory factory() { return FACTORY; }

    @Override
    public String name() { return "shr1"; }
}
