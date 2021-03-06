package piyopiyo.py.solvers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableList;

import piyopiyo.py.Operator;
import piyopiyo.py.Problem;
import piyopiyo.py.expressions.Constant;
import piyopiyo.py.expressions.Expression;
import piyopiyo.py.expressions.Fold;
import piyopiyo.py.expressions.Program;
import piyopiyo.py.expressions.Variable;
import piyopiyo.py.skeltons.Skelton;

public class SkeltonBasedWithTfold extends SimpleSolver {
    public static final SkeltonBasedWithTfold SOLVER =
        new SkeltonBasedWithTfold();

    private SkeltonBasedWithTfold() {}

    @Override
    public boolean canSolve(Problem problem) {
        return (problem.size <= 14 &&
                Arrays.asList(problem.operators).contains(Operator.tfold) &&
                problem.operators.length <= 5);
    }

    @Override
    protected List<Program> getCandidates(Problem problem) {
        List<Skelton> skeltons = Skelton.buildSkeltons(problem.size - 5)
            .get(problem.size - 5);

        List<Program> programs = new ArrayList<Program>();

        List<Operator> ops = Arrays.asList(problem.operators);
        Variable x = new Variable("x");
        Variable x0 = new Variable("x0");
        Variable y0 = new Variable("y0");
        List<Variable> vars = ImmutableList.of(x, x0, y0);
        for (Skelton skelton : skeltons) {
            for (Expression e : skelton.buildExpressions(ops, vars)) {
                programs.add(new Program(x, new Fold(x, Constant.ZERO, x0, y0, e)));
            }
        }

        return programs;
    }
}
