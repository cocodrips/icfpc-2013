package piyopiyo.py;


import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONHint;

import piyopiyo.py.solvers.SkeltonBasedWithTfold;
import piyopiyo.py.solvers.SkeltonBasedWithoutTfold;
import piyopiyo.py.solvers.Solver;

import com.google.common.collect.ImmutableList;


public class AutoAnswerer {
	public static void main(String[] args) throws Exception {
		List<String> argList = Arrays.asList(args);
		Scanner scanner = new Scanner(System.in);
		Problem[] problems;

		// status check
	 	StatusResponse firstStatus = IcfpClient.status();
	 	System.out.printf("%s%n", JSON.encode(firstStatus, true));

		// size of problem
		int size = argList.indexOf("-size");
		if (size > 0) {
			size = Integer.parseInt(args[size + 1]);
		}

		// train
		boolean train = argList.indexOf("-train") > 0;
		if (!train) {
			// type y to continue
			System.out.printf("[WARNING] NOT TRAINING. OK? Y/[n]:%n");
			String ans = scanner.nextLine();
			if (!ans.equalsIgnoreCase("y")) {
				train = true;
			}
		}

		train = true;
		if (!train) {
			System.out.println("Getting my problems...");
			problems = IcfpClient.myproblems();			
		} else {
			System.out.println("Getting training problems...");
			problems = new Problem[3];
			Random random = new Random();
			for (int i = 0; i < 3; i ++) {
				int newSize = size > 0 ? size : random.nextInt(10) + 3;
				TrainRequest request = new TrainRequest(newSize, null);
				problems[i] = IcfpClient.train(request);
			}
		}
		System.out.printf("Got %d problems.%n", problems.length);

		int success = 0;
		int fail = 0;

		for (Problem problem : problems) {
			if (problem.solved || (size > 0 && problem.size != size)) {
				continue;
			}
			// type y to continue
			System.out.printf("Do you solve %s? Y/[n]:%n", problem.id);
			String ans = scanner.nextLine();
			if (!ans.equalsIgnoreCase("y")) {
				break;
			}
			// solve!!
	        Solver solver = findSolver(problem);
	        if (solver != null) {
	            System.out.printf("Using %s.%n", solver.getClass().getName());
	            try {
	            	solver.solve(problem);
	            	success++;
	            } catch (Exception e) {
	            	System.err.println(e.getMessage());
	            	fail++;
	            }
	        } else {
	            System.out.println("No solver found. Problem remained untouched.%n");
	        }
		}

	 	// check status change
	 	StatusResponse lastStatus = IcfpClient.status();
	 	System.out.format("%s%n", JSON.encode(lastStatus, true));

	 	int diff;
	 	if (train) {
	 		diff = lastStatus.trainingScore - firstStatus.trainingScore;
	 	} else {
	 		diff = lastStatus.contestScore - firstStatus.contestScore;
	 	}

		System.out.format("success: %d, fail %d%n", success, fail);
	 	System.out.format("last score - first socore = %d%n", diff);
	}
	
    private static final List<Solver> SOLVERS = ImmutableList.<Solver> of(
            SkeltonBasedWithoutTfold.SOLVER,
            SkeltonBasedWithTfold.SOLVER);

    private static Solver findSolver(Problem problem) {
        for (Solver solver : SOLVERS) {
            if (solver.canSolve(problem)) return solver;
        }
        return null;
    }
}
