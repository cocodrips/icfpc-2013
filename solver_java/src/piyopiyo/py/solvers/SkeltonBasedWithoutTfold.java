package piyopiyo.py.solvers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableList;

import piyopiyo.py.Operator;
import piyopiyo.py.Problem;
import piyopiyo.py.expressions.Expression;
import piyopiyo.py.expressions.Program;
import piyopiyo.py.expressions.Variable;
import piyopiyo.py.skeltons.Skelton;

public class SkeltonBasedWithoutTfold extends SimpleSolver {
    public static final SkeltonBasedWithoutTfold SOLVER =
        new SkeltonBasedWithoutTfold();

    private SkeltonBasedWithoutTfold() {}

    @Override
    public boolean canSolve(Problem problem) {
        return (problem.size <= 11 &&
                !Arrays.asList(problem.operators).contains(Operator.tfold));
    }

    @Override
    protected List<Program> getCandidates(Problem problem) {
        List<Skelton> skeltons = Skelton.buildSkeltons(problem.size - 1)
            .get(problem.size - 1);

        List<Program> programs = new ArrayList<Program>();
        
        List<Operator> ops = Arrays.asList(problem.operators);
        Variable x = new Variable("x");
        List<Variable> vars = ImmutableList.of(x);
        for (Skelton skelton : skeltons) {
            for (Expression e : skelton.buildExpressions(ops, vars)) {
                programs.add(new Program(x, e));
            }
        }

        return programs;
    }
}
