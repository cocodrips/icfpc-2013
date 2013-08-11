package piyopiyo.py.solvers;

import java.util.Arrays;
import java.util.List;

import piyopiyo.py.Operator;
import piyopiyo.py.Problem;
import piyopiyo.py.expressions.Expression;
import piyopiyo.py.expressions.Program;
import piyopiyo.py.expressions.Variable;

public class SAWithoutTfold extends SimulatedAnnealing {
    public static final SAWithoutTfold SOLVER = new SAWithoutTfold();

    private SAWithoutTfold() {}

    @Override
    public boolean canSolve(Problem problem) {
        List<Operator> ops = Arrays.asList(problem.operators);
        return (!ops.contains(Operator.tfold) && !ops.contains(Operator.fold) &&
                !ops.contains(Operator.bonus));
    }

    @Override
    protected Program findProgram(List<Long> inputs,
                                  List<Long> outputs,
                                  Problem problem) {
        Variable x = new Variable("x");

        List<Expression> seeds = getSeeds(problem.operators, x);

        for (int retries = 1; retries <= MAX_RETRIES; retries++) {
            System.err.printf("Attempt #%d", retries);

            Expression bestExpr = x;
            int bestScore = 0;
            double temp = 1.0;

            for (int updates = 0; updates < MAX_UPDATES; updates++) {
                Expression subexpr = seeds.get(random.nextInt(seeds.size()));
                Expression expr = bestExpr.mutate(subexpr);
                Program program = new Program(x, expr);

                int score = countScore(program, inputs, outputs);
                if (score == outputs.size()) {
                    System.err.println(" - Success.");
                    return program;
                }
                if (bestScore < score || Math.random() < temp) {
                    bestScore = score;
                    bestExpr = expr;
                }

                temp *= ANNEALING_RATIO;
            }

            System.err.printf(" - Score = %d.%n", bestScore);
            if (retries % 5 == 0) addMoreArgs(inputs, outputs, problem);
        }

        throw new SolutionNotFoundException();
    }
}
