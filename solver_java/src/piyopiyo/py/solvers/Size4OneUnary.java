package piyopiyo.py.solvers;

import static piyopiyo.py.Operator.UNARY_OPERATORS;
import static piyopiyo.py.expressions.Constant.ONE;
import static piyopiyo.py.expressions.Constant.ZERO;

import java.util.ArrayList;
import java.util.List;

import piyopiyo.py.Problem;
import piyopiyo.py.expressions.Expression;
import piyopiyo.py.expressions.Program;
import piyopiyo.py.expressions.UnaryExpressionFactory;
import piyopiyo.py.expressions.Variable;

public class Size4OneUnary extends SimpleSolver {
    public static final Size4OneUnary SOLVER = new Size4OneUnary();

    private Size4OneUnary() {}

    @Override
    public boolean canSolve(Problem problem) {
        return (problem.size == 4 && problem.operators.length == 1 &&
                UNARY_OPERATORS.containsKey(problem.operators[0]));
    }

    @Override
    protected List<Program> getCandidates(Problem problem) {
        UnaryExpressionFactory f = UNARY_OPERATORS.get(problem.operators[0]);
        Variable x = new Variable("x");
        Expression[] values = new Expression[] { x, ZERO, ONE };
        List<Program> programs = new ArrayList<Program>();
        for (Expression value : values) {
            programs.add(new Program(x, f.create(f.create(value))));
        }
        return programs;
    }
}
