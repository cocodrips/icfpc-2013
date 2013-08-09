package piyopiyo.py.expressions;

// TODO(yuizumi): interface?
public abstract class BinaryExpressionFactory {
    public abstract BinaryExpression create(Expression e0, Expression e1);
}
