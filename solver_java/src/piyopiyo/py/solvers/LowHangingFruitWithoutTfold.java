package piyopiyo.py.solvers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import piyopiyo.py.Operator;
import piyopiyo.py.Problem;
import piyopiyo.py.expressions.Expression;
import piyopiyo.py.expressions.Program;
import piyopiyo.py.expressions.Variable;
import piyopiyo.py.skeltons.Skelton;

import com.google.common.collect.ImmutableList;

public class LowHangingFruitWithoutTfold extends SimpleSolver {
    public static final LowHangingFruitWithoutTfold SOLVER =
            new LowHangingFruitWithoutTfold();
    private static final int SIZE_TO_TRY = 7;
    private LowHangingFruitWithoutTfold() {}

    @Override
    public boolean canSolve(Problem problem) {
        return (problem.size >= 25 &&
                !Arrays.asList(problem.operators).contains(Operator.tfold));
    }

    @Override
    protected List<Program> getCandidates(Problem problem) {
        List<List<Skelton>> skeltonLists = Skelton.buildSkeltons(SIZE_TO_TRY);

        List<Program> programs = new ArrayList<Program>();

        List<Operator> ops = Arrays.asList(problem.operators);
        Variable x = new Variable("x");
        List<Variable> vars = ImmutableList.of(x);
        for (List<Skelton> skeltons : skeltonLists) {
            for (Skelton skelton : skeltons) {
                for (Expression e : skelton.buildExpressions(ops, vars)) {
                    programs.add(new Program(x, e));
                }
            }
        }
        return programs;
    }
}
