package piyopiyo.py.skeltons;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import piyopiyo.py.Operator;
import piyopiyo.py.expressions.Expression;
import piyopiyo.py.expressions.If0;
import piyopiyo.py.expressions.Variable;

public class If0Skelton extends Skelton {
    private final List<Skelton> s0s;
    private final List<Skelton> s1s;
    private final List<Skelton> s2s;

    public If0Skelton(List<Skelton> s0s, List<Skelton> s1s, List<Skelton> s2s) {
        this.s0s = s0s;
        this.s1s = s1s;
        this.s2s = s2s;
    }

    @Override
    public List<Expression> buildExpressions(List<Operator> ops, List<Variable> vars) {
        if (!ops.contains(Operator.if0)) return ImmutableList.of();

        List<Expression> list = new ArrayList<Expression>();
        for (Skelton s0 : s0s) for (Expression e0 : s0.buildExpressions(ops, vars))
        for (Skelton s1 : s1s) for (Expression e1 : s1.buildExpressions(ops, vars))
        for (Skelton s2 : s2s) for (Expression e2 : s2.buildExpressions(ops, vars)) {
            list.add(new If0(e0, e1, e2));
        }
        return list;
    }
}
