package piyopiyo.py;


import java.util.List;
import java.util.Random;

import net.arnx.jsonic.JSON;

import piyopiyo.py.solvers.SkeltonBasedWithTfold;
import piyopiyo.py.solvers.SkeltonBasedWithoutTfold;
import piyopiyo.py.solvers.Solver;

import com.google.common.collect.ImmutableList;


public class AutoAnswerer {
    private static final int WAIT_SECS_BEFORE_SOLVE = 10;

    private static final List<Solver> SOLVERS = ImmutableList.<Solver> of(
        SkeltonBasedWithoutTfold.SOLVER,
        SkeltonBasedWithTfold.SOLVER);

    private static Solver findSolver(Problem problem) {
        for (Solver solver : SOLVERS) {
            if (solver.canSolve(problem)) return solver;
        }
        return null;
    }

    public static void doit(boolean training) throws Exception {
        // Check the current status.
        StatusResponse firstStatus = IcfpClient.status();
        System.err.println(JSON.encode(firstStatus));

        Problem[] problems;
        if (!training) {
            System.err.println("Getting my problems...");
            problems = IcfpClient.myproblems();
        } else {
            System.err.println("Getting training problems...");
            problems = new Problem[3];
            Random random = new Random();
            for (int i = 0; i < 3; i ++) {
                int size = random.nextInt(15) + 3;
                TrainRequest request = new TrainRequest(size, null);
                problems[i] = IcfpClient.train(request);
            }
        }
        System.err.printf("Got %d problems.%n", problems.length);

        int success = 0;
        int failure = 0;

        for (Problem problem : problems) {
            if (problem.timeLeft <= 0.0) continue;

            System.err.printf("Trying: %s%n", JSON.encode(problem));
            Solver solver = findSolver(problem);
            if (solver != null) {
                Thread.sleep(WAIT_SECS_BEFORE_SOLVE * 1000);
                System.err.printf("Using %s.%n", solver.getClass().getName());
                try {
                    solver.solve(problem);
                    success++;
                } catch (Exception e) {
                    if (!training) throw e;
                    System.err.println(e.getMessage());
                    failure++;
                }
            } else {
                System.err.println("No solver found. Problem remained untouched.");
            }
        }

        // See the updated status.
        StatusResponse lastStatus = IcfpClient.status();
        System.err.println(JSON.encode(lastStatus));

        int diff = (training) ? lastStatus.trainingScore - firstStatus.trainingScore
            : lastStatus.contestScore - firstStatus.contestScore;

        System.err.printf("Success: %d, Failure: %d%nNewly solved: %d%n",
                          success, failure, diff);
    }
}
