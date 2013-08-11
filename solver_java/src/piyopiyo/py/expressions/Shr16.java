package piyopiyo.py.expressions;

public class Shr16 extends UnaryExpression {
    public static UnaryExpressionFactory FACTORY =
        new UnaryExpressionFactory() {
            @Override
            public UnaryExpression create(Expression e) { return new Shr16(e); }
        };

    public Shr16(Expression e) {
        super(e);
    }

    @Override
    public long eval() { return e.eval() >>> 16; }

    @Override
    public UnaryExpressionFactory factory() { return FACTORY; }

    @Override
    public String name() { return "shr16"; }

    @Override
    public boolean isRedudant() {
        return e instanceof Constant;
    }
}
