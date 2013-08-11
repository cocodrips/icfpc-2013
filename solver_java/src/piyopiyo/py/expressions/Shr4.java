package piyopiyo.py.expressions;

public class Shr4 extends UnaryExpression {
    public static UnaryExpressionFactory FACTORY =
        new UnaryExpressionFactory() {
            @Override
            public UnaryExpression create(Expression e) { return new Shr4(e); }
        };

    public Shr4(Expression e) {
        super(e);
    }

    @Override
    public long eval() { return e.eval() >>> 4; }

    @Override
    public UnaryExpressionFactory factory() { return FACTORY; }

    @Override
    public String name() { return "shr4"; }

    @Override
    public boolean isRedudant() {
        return e instanceof Constant;
    }
}
