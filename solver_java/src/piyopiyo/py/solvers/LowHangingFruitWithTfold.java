package piyopiyo.py.solvers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import piyopiyo.py.Operator;
import piyopiyo.py.Problem;
import piyopiyo.py.expressions.Constant;
import piyopiyo.py.expressions.Expression;
import piyopiyo.py.expressions.Fold;
import piyopiyo.py.expressions.Program;
import piyopiyo.py.expressions.Variable;
import piyopiyo.py.skeltons.Skelton;

import com.google.common.collect.ImmutableList;

public class LowHangingFruitWithTfold extends SimpleSolver {
    public static final LowHangingFruitWithTfold SOLVER =
            new LowHangingFruitWithTfold();
    private static final int SIZE_TO_TRY = 7;
    private LowHangingFruitWithTfold() {}

    @Override
    public boolean canSolve(Problem problem) {
        List<Operator> ops = Arrays.asList(problem.operators);
        return (problem.size >= 14 &&
                ops.contains(Operator.tfold) &&
                !ops.contains(Operator.plus) &&
                !ops.contains(Operator.fold));
    }

    @Override
    protected List<Program> getCandidates(Problem problem) {
        List<List<Skelton>> skeltonLists = Skelton.buildSkeltons(SIZE_TO_TRY);

        List<Program> programs = new ArrayList<Program>();

        List<Operator> ops = Arrays.asList(problem.operators);
        Variable x = new Variable("x");
        Variable x0 = new Variable("x0");
        Variable y0 = new Variable("y0");
        List<Variable> vars = ImmutableList.of(x);
        for (List<Skelton> skeltons : skeltonLists) {
            for (Skelton skelton : skeltons) {
                for (Expression e : skelton.buildExpressions(ops, vars)) {
                    programs.add(new Program(x, new Fold(x, Constant.ZERO, x0, y0, e)));
                }
            }
        }
        return programs;
    }
}
