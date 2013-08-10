package piyopiyo.py;

import static piyopiyo.py.IcfpJson.ICFPJSON;

import java.util.List;

import com.google.common.collect.ImmutableList;

import piyopiyo.py.solvers.*;

public class Main {
    private static final List<Solver> SOLVERS = ImmutableList.<Solver> of(
        SkeltonBasedWithoutTfold.SOLVER,
        SkeltonBasedWithTfold.SOLVER);

    public static void main(String[] args) throws Exception {
        Problem problem = ICFPJSON.parse(System.in, Problem.class);
        Solver solver = findSolver(problem);
        if (solver != null) {
            System.err.printf("Using %s.%n", solver.getClass().getName());
            solver.solve(problem);
        } else {
            System.err.println("No solver found. Problem remained untouched.");
        }
    }

    private static Solver findSolver(Problem problem) {
        for (Solver solver : SOLVERS) {
            if (solver.canSolve(problem)) return solver;
        }
        return null;
    }
}
