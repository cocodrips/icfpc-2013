package piyopiyo.py.solvers;

import static piyopiyo.py.Operator.BINARY_OPERATORS;
import static piyopiyo.py.expressions.Constant.ONE;
import static piyopiyo.py.expressions.Constant.ZERO;

import java.util.ArrayList;
import java.util.List;

import piyopiyo.py.Problem;
import piyopiyo.py.expressions.BinaryExpressionFactory;
import piyopiyo.py.expressions.Expression;
import piyopiyo.py.expressions.Program;
import piyopiyo.py.expressions.Variable;

public class Size4OneBinary extends SimpleSolver {
    public static final Size4OneBinary SOLVER = new Size4OneBinary();

    private Size4OneBinary() {}

    @Override
    public boolean canSolve(Problem problem) {
        return (problem.size == 4 && problem.operators.length == 1 &&
                BINARY_OPERATORS.containsKey(problem.operators[0]));
    }

    @Override
    protected List<Program> getCandidates(Problem problem) {
        BinaryExpressionFactory f = BINARY_OPERATORS.get(problem.operators[0]);
        Variable x = new Variable("x");
        Expression[] values = new Expression[] { x, ZERO, ONE };
        List<Program> programs = new ArrayList<Program>();
        for (Expression e0 : values) {
            for (Expression e1 : values) {
                programs.add(new Program(x, f.create(e0, e1)));
            }
        }
        return programs;
    }
}
