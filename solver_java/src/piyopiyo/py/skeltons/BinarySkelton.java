package piyopiyo.py.skeltons;

import static piyopiyo.py.Operator.BINARY_OPERATORS;

import java.util.ArrayList;
import java.util.List;

import piyopiyo.py.Operator;
import piyopiyo.py.expressions.BinaryExpressionFactory;
import piyopiyo.py.expressions.Expression;
import piyopiyo.py.expressions.Variable;

public class BinarySkelton extends Skelton {
    private final List<Skelton> s0s;
    private final List<Skelton> s1s;

    public BinarySkelton(List<Skelton> s0s, List<Skelton> s1s) {
        this.s0s = s0s;
        this.s1s = s1s;
    }

    @Override
    public List<Expression> buildExpressions(List<Operator> ops, List<Variable> vars) {
        List<Expression> list = new ArrayList<Expression>();
        for (Operator op : ops) {
            BinaryExpressionFactory f = BINARY_OPERATORS.get(op);
            if (f == null) continue;
            for (Skelton s0 : s0s) for (Expression e0 : s0.buildExpressions(ops, vars))
            for (Skelton s1 : s1s) for (Expression e1 : s1.buildExpressions(ops, vars)) {
                list.add(f.create(e0, e1));
            }
        }
        return list;
    }
}
