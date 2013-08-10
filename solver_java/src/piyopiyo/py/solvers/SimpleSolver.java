package piyopiyo.py.solvers;

import static com.google.common.base.Preconditions.*;

import java.util.ArrayList;
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
    private static final int NUM_ARGS = 1;

    private final Random random = new Random();

    @Override
    public void solve(Problem problem) throws Exception {
    	checkArgument(canSolve(problem));
   
        List<Long> argsList = new ArrayList<Long>();
        for (int i = 0; i < NUM_ARGS; i++) {
        	argsList.add((i < NUM_SIMPLE_ARGS) ? i : random.nextLong());
        }
        
        long[] args = new long[NUM_ARGS];
        for (int i = 0; i < argsList.size(); i++) {
        	args[i] = argsList.get(i);
        }


        System.err.println("Generating candidates...");
        List<Program> candidates = getCandidates(problem);

        EvalRequest evalReq = new EvalRequest(problem.id, args);
        EvalResponse evalRes = IcfpClient.eval(evalReq);
        
        // Create list of evalRes
        List<Long> evalResList = new ArrayList<Long>();
        for (int i = 0; i < evalRes.outputs.length; i++) {
			evalResList.add(evalRes.outputs[i]);
		}
        
        for (Program program : candidates) {
            boolean isGood = true;
            for (int i = 0; i < argsList.size(); i++) {
            	if (program.eval(argsList.get(i)) != evalResList.get(i)) {
                    isGood = false;
                    break;
                }
			}
            if (!isGood) continue;

            GuessRequest guessReq = new GuessRequest(problem.id, program);
            GuessResponse guessRes = IcfpClient.guess(guessReq);

            if (guessRes.status == GuessResponse.Status.win) {
                System.err.println("Solved.");
                return;
            } else if(guessRes.status == GuessResponse.Status.mismatch) {
                System.err.printf("Adding to ArgsList: (%d, %d)%n", guessRes.getInput(), guessRes.getExpectedOutput());
                argsList.add(guessRes.getInput());
                evalResList.add(guessRes.getExpectedOutput());
            } else{
            	throw new RuntimeException();
            }
        }
        throw new SolutionNotFoundException();
    }

    protected abstract List<Program> getCandidates(Problem problem);
}
