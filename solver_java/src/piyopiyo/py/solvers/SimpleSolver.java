package piyopiyo.py.solvers;

import static com.google.common.base.Preconditions.*;

import java.util.List;
import java.util.Random;

import piyopiyo.py.EvalRequest;
import piyopiyo.py.EvalResponse;
import piyopiyo.py.GuessRequest;
import piyopiyo.py.GuessResponse;
import piyopiyo.py.IcfpClient;
import piyopiyo.py.Problem;
import piyopiyo.py.expressions.Program;

public abstract class SimpleSolver extends Solver {
    private static final int NUM_SIMPLE_ARGS = 64;
    private static final int NUM_ARGS = 256;

    private final Random random = new Random();

    @Override
    public void solve(Problem problem) throws Exception {
    	checkArgument(canSolve(problem));
   
        long[] args = new long[NUM_ARGS];

        for (int i = 0; i < NUM_ARGS; i++) {
            args[i] = (i < NUM_SIMPLE_ARGS) ? i : random.nextLong();
        }

        System.err.println("Generating candidates...");
        List<Program> candidates = getCandidates(problem);

        EvalRequest evalReq = new EvalRequest(problem.id, args);
        EvalResponse evalRes = IcfpClient.eval(evalReq);

        Program program = findProgram(candidates, args, evalRes.outputs);
        if (program == null) {
            throw new SolutionNotFoundException();
        }
        GuessRequest guessReq = new GuessRequest(problem.id, program);
        GuessResponse guessRes = IcfpClient.guess(guessReq);
        if (guessRes.status != GuessResponse.Status.win) {
            throw new SolutionNotFoundException("Failed to answer the problem.");
        }
    }

    private static Program findProgram(List<Program> programs,
                                       long[] inputs, long[] outputs) {
        for (Program program : programs) {
            boolean isGood = true;
            for (int i = 0; i < inputs.length; i++) {
                if (program.eval(inputs[i]) != outputs[i]) {
                    isGood = false;
                    break;
                }
            }
            if (isGood) return program;
        }
        return null;
    }

    protected abstract List<Program> getCandidates(Problem problem);
}
