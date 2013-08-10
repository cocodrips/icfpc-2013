package piyopiyo.py.skeltons;

import static piyopiyo.py.Operator.UNARY_OPERATORS;

import java.util.ArrayList;
import java.util.List;

import piyopiyo.py.Operator;
import piyopiyo.py.expressions.Expression;
import piyopiyo.py.expressions.UnaryExpressionFactory;
import piyopiyo.py.expressions.Variable;

public class UnarySkelton extends Skelton {
    private final List<Skelton> ss;

    public UnarySkelton(List<Skelton> ss) {
        this.ss = ss;
    }

    @Override
    public List<Expression> buildExpressions(List<Operator> ops, List<Variable> vars) {
        List<Expression> list = new ArrayList<Expression>();
        for (Operator op : ops) {
            UnaryExpressionFactory f = UNARY_OPERATORS.get(op);
            if (f == null) continue;
            for (Skelton s : ss) for (Expression e : s.buildExpressions(ops, vars)) {
                list.add(f.create(e));
            }
        }
        return list;
    }
}
