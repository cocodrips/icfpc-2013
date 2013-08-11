package piyopiyo.py.expressions;

public class And extends BinaryExpression {
    public static BinaryExpressionFactory FACTORY =
        new BinaryExpressionFactory() {
            @Override
            public BinaryExpression create(Expression e0, Expression e1) {
                return new And(e0, e1);
            }
        };

    public And(Expression e0, Expression e1) {
        super(e0, e1);
    }

    @Override
    public long eval() { return e0.eval() & e1.eval(); }

    @Override
    public BinaryExpressionFactory factory() { return FACTORY; }

    @Override
    public String name() { return "and"; }

    @Override
    public boolean isRedudant() {
        return e0 == Constant.ZERO ||
                e1 == Constant.ZERO ||
                e0 == e1;
    }
}
