package piyopiyo.py.solvers;

import static piyopiyo.py.Operator.UNARY_OPERATORS;
import static piyopiyo.py.expressions.Constant.ONE;
import static piyopiyo.py.expressions.Constant.ZERO;

import java.util.List;

import com.google.common.collect.ImmutableList;

import piyopiyo.py.Problem;
import piyopiyo.py.expressions.Program;
import piyopiyo.py.expressions.UnaryExpressionFactory;
import piyopiyo.py.expressions.Variable;

public class Size3Solver extends SimpleSolver {
    public static Size3Solver SOLVER = new Size3Solver();

    private Size3Solver() {}

    @Override
    public boolean canSolve(Problem problem) {
        return (problem.operators.length == 1 &&
                UNARY_OPERATORS.containsKey(problem.operators[0]));
    }

    @Override
    protected List<Program> getCandidates(Problem problem) {
        UnaryExpressionFactory f = UNARY_OPERATORS.get(problem.operators[0]);
        Variable x = new Variable("x");
        return ImmutableList.of(new Program(x, f.create(x)),
                                new Program(x, f.create(ZERO)),
                                new Program(x, f.create(ONE)));
    }
}
