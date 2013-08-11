package piyopiyo.py.solvers;

import static com.google.common.base.Preconditions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import piyopiyo.py.EvalRequest;
import piyopiyo.py.EvalResponse;
import piyopiyo.py.GuessRequest;
import piyopiyo.py.GuessResponse;
import piyopiyo.py.IcfpClient;
import piyopiyo.py.Operator;
import piyopiyo.py.Problem;
import piyopiyo.py.expressions.Expression;
import piyopiyo.py.expressions.Program;
import piyopiyo.py.expressions.Variable;
import piyopiyo.py.skeltons.Skelton;

public abstract class SimulatedAnnealing extends Solver {
    private static final int NUM_SIMPLE_ARGS = 64;
    private static final int NUM_ARGS = 256;

    protected static final double ANNEALING_RATIO = 0.5;

    protected static final int MAX_UPDATES = 100000;
    protected static final int MAX_RETRIES = 30;

    protected final Random random = new Random();

    @Override
    public void solve(Problem problem) throws Exception {
    	checkArgument(canSolve(problem));
   
        long[] args = new long[NUM_ARGS];
        int index = 0;
        for (int i = 0; i < NUM_SIMPLE_ARGS; i++) {
            args[index++] = i;
        }
        for (int i = 1; i < NUM_SIMPLE_ARGS; i++) {
            args[index++] = -i;
        }
        while (index < NUM_ARGS) {
            args[index++] = random.nextLong();
        }

        EvalRequest evalReq = new EvalRequest(problem.id, args);
        EvalResponse evalRes = IcfpClient.eval(evalReq);

        List<Long> inputs = new ArrayList<Long>();
        List<Long> outputs = new ArrayList<Long>();
        for (int i = 0; i < args.length; i++) {
            inputs.add(args[i]); outputs.add(evalRes.outputs[i]);
        }

        while (true) {
            Program program = findProgram(inputs, outputs, problem.operators);

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

    protected abstract Program findProgram(List<Long> inputs,
                                           List<Long> outputs,
                                           Operator[] operators);

    protected List<Expression> getSeeds(Operator[] operators,
                                        Variable... variables) {
        List<Operator> opsList = Arrays.asList(operators);
        List<Variable> varsList = Arrays.asList(variables);

        List<Expression> seeds = new ArrayList<Expression>();
        List<List<Skelton>> allSkeltons = Skelton.buildSkeltons(5);
        for (List<Skelton> skeltons : allSkeltons) {
            for (Skelton skelton : skeltons) {
                seeds.addAll(skelton.buildExpressions(opsList, varsList));
            }
        }
        return seeds;
    }

    protected int countScore(Program program, List<Long> inputs,
                             List<Long> outputs) {
        int score = 0;
        for (int i = 0; i < inputs.size(); i++) {
            if (program.eval(inputs.get(i)) == outputs.get(i)) {
                ++score;
            }
        }
        return score;
    }
}
