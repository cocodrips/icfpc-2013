package piyopiyo.py.solvers;

import static piyopiyo.py.Operator.UNARY_OPERATORS;

import java.util.List;

import com.google.common.collect.ImmutableList;

import piyopiyo.py.Operator;
import piyopiyo.py.expressions.Constant;
import piyopiyo.py.expressions.Program;
import piyopiyo.py.expressions.UnaryExpressionFactory;
import piyopiyo.py.expressions.Variable;

public class Size3Solver extends SimpleSolver {
    public static Size3Solver SOLVER = new Size3Solver();

    private Size3Solver() {}

    @Override
    protected List<Program> getCandidates(Operator[] ops) {
        if (ops.length != 1) {
            throw new SolutionNotFoundException("Expecting one unary operator.");
        }

        UnaryExpressionFactory factory = UNARY_OPERATORS.get(ops[0]);
        if (factory == null) {
            throw new SolutionNotFoundException("Expecting one unary operator.");
        }
        Variable x = new Variable("x");
        return ImmutableList.of(new Program(x, factory.create(x)),
                                new Program(x, factory.create(Constant.ZERO)),
                                new Program(x, factory.create(Constant.ONE)));
    }
}
