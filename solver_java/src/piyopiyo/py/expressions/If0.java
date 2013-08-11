package piyopiyo.py.expressions;

/**
 *  Represents {@code (if0 e0 e1 e2)}.
 */
public class If0 extends Expression {
    private final Expression e0, e1, e2;

    public If0(Expression e0, Expression e1, Expression e2) {
        this.e0 = e0;
        this.e1 = e1;
        this.e2 = e2;
    }

    @Override
    public long eval() {
        return e0.eval() == 0 ? e1.eval() : e2.eval();
    }

    @Override
    public Expression mutate(Expression eNew) {
        if (Math.random() < PROB_MUTATE_SELF) {
            return eNew;
        }

        double p = Math.random() * 16.0;
        if (p < 5.0) {
            return new If0(e0.mutate(eNew), e1, e2);
        } else if (p < 10.0) {
            return new If0(e0, e1.mutate(eNew), e2);
        } else {
            return new If0(e0, e1, e2.mutate(eNew));
        }
    }

    @Override
    public String toString() {
        return String.format("(if0 %s %s %s)", e0, e1, e2);
    }
}
