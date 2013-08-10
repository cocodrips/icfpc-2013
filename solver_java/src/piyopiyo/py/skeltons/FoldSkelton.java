package piyopiyo.py.skeltons;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import piyopiyo.py.Operator;
import piyopiyo.py.expressions.Expression;
import piyopiyo.py.expressions.Fold;
import piyopiyo.py.expressions.Variable;

public class FoldSkelton extends Skelton {
    private final List<Skelton> s0s;
    private final List<Skelton> s1s;
    private final List<Skelton> s2s;

    public FoldSkelton(List<Skelton> s0s, List<Skelton> s1s, List<Skelton> s2s) {
        this.s0s = s0s;
        this.s1s = s1s;
        this.s2s = s2s;
    }

    @Override
    public List<Expression> buildExpressions(List<Operator> ops, List<Variable> vars) {
        if (!ops.contains(Operator.fold)) return ImmutableList.of();

        List<Variable> newVars = new ArrayList<Variable>(vars);
        int i = (vars.size() + 1) / 2;

        Variable x = new Variable("x" + i); newVars.add(x);
        Variable y = new Variable("y" + i); newVars.add(y);

        List<Expression> list = new ArrayList<Expression>();
        for (Skelton s0 : s0s) for (Expression e0 : s0.buildExpressions(ops, vars))
        for (Skelton s1 : s1s) for (Expression e1 : s1.buildExpressions(ops, vars))
        for (Skelton s2 : s2s) for (Expression e2 : s2.buildExpressions(ops, newVars)) {
            list.add(new Fold(e0, e1, x, y, e2));
        }
        return list;
    }
}
