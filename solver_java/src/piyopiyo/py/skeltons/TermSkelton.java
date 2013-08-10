package piyopiyo.py.skeltons;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import piyopiyo.py.Operator;
import piyopiyo.py.expressions.Constant;
import piyopiyo.py.expressions.Expression;
import piyopiyo.py.expressions.Variable;

public class TermSkelton extends Skelton {
    public static final TermSkelton TERM = new TermSkelton();

    private TermSkelton() {}

    @Override
    public List<Expression> buildExpressions(List<Operator> ops,
                                             List<Variable> vars) {
        List<Expression> terms = new ArrayList<Expression>(vars);
        terms.add(Constant.ZERO);
        terms.add(Constant.ONE);
        return terms;
    }
}
