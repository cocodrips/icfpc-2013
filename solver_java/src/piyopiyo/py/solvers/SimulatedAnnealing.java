package piyopiyo.py.solvers;

import static com.google.common.base.Preconditions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;

import piyopiyo.py.EvalRequest;
import piyopiyo.py.EvalResponse;
import piyopiyo.py.GuessRequest;
import piyopiyo.py.GuessResponse;
import piyopiyo.py.IcfpClient;
import piyopiyo.py.Operator;
import piyopiyo.py.Problem;
import piyopiyo.py.expressions.*;
import piyopiyo.py.skeltons.Skelton;

public class SimulatedAnnealing extends Solver {
    public static final SimulatedAnnealing SOLVER = new SimulatedAnnealing();

    private static final int NUM_SIMPLE_ARGS = 64;
    private static final int NUM_ARGS = 256;

    private static final double ANNEALING_RATIO = 0.5;
    private static final int MAX_UPDATES = 10000;
    private static final int MAX_RETRIES = 10;

    private final Random random = new Random();

    private SimulatedAnnealing() {}

    @Override
    public boolean canSolve(Problem problem) {
        return true;
    }

    @Override
    public void solve(Problem problem) throws Exception {
    	checkArgument(canSolve(problem));
   
        long[] args = new long[NUM_ARGS];
        for (int i = 0; i < NUM_ARGS; i++) {
            args[i] = (i < NUM_SIMPLE_ARGS) ? i : random.nextLong();
        }

        EvalRequest evalReq = new EvalRequest(problem.id, args);
        EvalResponse evalRes = IcfpClient.eval(evalReq);

        List<Long> inputs = new ArrayList<Long>();
        List<Long> outputs = new ArrayList<Long>();
        for (int i = 0; i < args.length; i++) {
            inputs.add(args[i]); outputs.add(evalRes.outputs[i]);
        }

        while (true) {
            Program program = findProgram(Arrays.asList(problem.operators),
                                          inputs, outputs);

            GuessRequest guessReq = new GuessRequest(problem.id, program);
            GuessResponse guessRes = IcfpClient.guess(guessReq);

            if (guessRes.status == GuessResponse.Status.win) {
                System.err.println("Solved.");
                return;
            } else if(guessRes.status == GuessResponse.Status.mismatch) {
                System.err.printf("Adding to ArgsList: (%d, %d)%n", guessRes.getInput(), guessRes.getExpectedOutput());
                inputs.add(guessRes.getInput());
                outputs.add(guessRes.getExpectedOutput());
            } else {
            	throw new RuntimeException(guessRes.message);
            }
        }
    }

    private Program findProgram(List<Operator> operators,
                                List<Long> inputs, List<Long> outputs) {
        Variable x = new Variable("x");

        List<List<Skelton>> allSkeltons = Skelton.buildSkeltons(5);
        List<Expression> exprs = new ArrayList<Expression>();
        for (int size = 1; size <= 5; size++) {
            for (Skelton skelton : allSkeltons.get(size)) {
                exprs.addAll(skelton.buildExpressions(operators, ImmutableList.of(x)));
            }
        }

        for (int retries = 1; retries <= MAX_RETRIES; retries++) {
            System.err.printf("Attempt #%d%n", retries);

            Program bestProgram = new Program(x, x);
            int bestScore = 0;
            double temp = 1.0;
            for (int updates = 0; updates < MAX_UPDATES; updates++) {
                Program program = bestProgram.mutate(
                    exprs.get(random.nextInt(exprs.size())));
                int score = 0;
                for (int i = 0; i < inputs.size(); i++) {
                    if (program.eval(inputs.get(i)) == outputs.get(i)) {
                        ++score;
                    }
                }
                if (score == inputs.size()) return program;

                if (bestScore < score || Math.random() < temp) {
                    bestProgram = program;
                    bestScore = score;
                }

                temp *= ANNEALING_RATIO;
            }
        }

        throw new SolutionNotFoundException();
    }
}
