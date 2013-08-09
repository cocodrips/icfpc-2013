package piyopiyo.py.solvers;

import java.util.List;
import java.util.Random;

import piyopiyo.py.EvalRequest;
import piyopiyo.py.EvalResponse;
import piyopiyo.py.Operator;
import piyopiyo.py.Problem;
import piyopiyo.py.expressions.Program;

public abstract class SimpleSolver {
    private static final int NUM_SIMPLE_ARGS = 64;
    private static final int NUM_ARGS = 256;

    private final Random random = new Random();

    public void solve(Problem problem) {
        long[] args = new long[NUM_ARGS];

        for (int i = 0; i < NUM_ARGS; i++) {
            args[i] = (i < NUM_SIMPLE_ARGS) ? i : random.nextLong();
        }

        EvalRequest req = new EvalRequest(problem.id, args);
        // TODO(yuizumi): Send a /eval request.
        EvalResponse res = null;

        Program program = findProgram(getCandidates(problem.operators),
                                      args, res.outputs);
        if (program == null) {
            throw new SolutionNotFoundException();
        }
        // TODO(yuizumi): Send a /guess request.
    }

    private static Program findProgram(List<Program> programs,
                                       long[] inputs,
                                       long[] outputs) {
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

    protected abstract List<Program> getCandidates(Operator[] ops);
}
