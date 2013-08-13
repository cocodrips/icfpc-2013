package piyopiyo.py.solvers;

import static com.google.common.base.Preconditions.*;

import java.io.ObjectInputStream.GetField;
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
import piyopiyo.py.expressions.Constant;
import piyopiyo.py.expressions.Expression;
import piyopiyo.py.expressions.If0;
import piyopiyo.py.expressions.Program;
import piyopiyo.py.expressions.Variable;
import piyopiyo.py.skeltons.Skelton;

public abstract class SimulatedAnnealing extends Solver {
    private static final int NUM_SIMPLE_ARGS = 64;
    private static final int NUM_ARGS = 256;

    protected static final double ANNEALING_RATIO = 0.5;

    protected static final int MAX_UPDATES = 30000;
    protected static final int MAX_RETRIES = 400;

    protected Random random = new Random();

    // Arguments seen in mismatch responses in the past.
    private static final List<Long> FIXED_ARGS = ImmutableList.of(
        -281470681808895L, -6148914691236517205L,
        -9223372036854764882L, 9223367698971042820L,
        9558L, 4611686018427365634L, 20964L, -4611686018427386366L,
        11250L, 440350111957104L, 4611967493674041345L, 
        576462746036467839L, 281479271677952L, -2049638230411249474L,
        131071L, 81917L, -9223372036854775808L, -9223372036854775808L,
        -4412748309372840944L, 8722152108262174013L);

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
        for (int i = 0; i < FIXED_ARGS.size(); i++) {
            args[index++] = FIXED_ARGS.get(i);
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

            random = new Random();  // Set another random seed.
        }
    }

    protected abstract Program findProgram(List<Long> inputs,
                                           List<Long> outputs,
                                           Operator[] operators);

    protected static  List<Expression> getSeeds(Operator[] operators,
                                                Variable... variables) {
        List<Operator> opsList = Arrays.asList(operators);
        List<Variable> varsList = Arrays.asList(variables);

        List<Expression> seeds = new ArrayList<Expression>();
        List<List<Skelton>> allSkeltons = Skelton.buildSkeltons(5);
        for (List<Skelton> skeltons : allSkeltons) {
            for (Skelton skelton : skeltons) {
                for (Expression e : skelton.buildExpressions(opsList, varsList)) {
                    if (e.isGround() && !(e instanceof Constant)) {
                        long value = e.eval();
                        if (value == 0 || value == 1) continue;
                    }
                    if (e.isRedundant()) continue;
                    seeds.add(e);
                }
            }
        }
        return seeds;
    }

    protected static int countScore(Program program, List<Long> inputs,
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
