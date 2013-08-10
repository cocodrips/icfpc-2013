package piyopiyo.py;

import static piyopiyo.py.IcfpJson.ICFPJSON;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import piyopiyo.py.solvers.Size3Solver;
import piyopiyo.py.solvers.Size4OneBinary;
import piyopiyo.py.solvers.Size4OneUnary;
import piyopiyo.py.solvers.Size4TwoUnary;
import piyopiyo.py.solvers.Solver;

import com.google.common.collect.ImmutableList;


public class AutoAnswerer {
	public static void main(String[] args) throws Exception {
		List<String> argList = Arrays.asList(args);
		Scanner scanner = new Scanner(System.in);
		Problem[] problems;

		// size of problem
		int size = argList.indexOf("-size");
		if (size > 0) {
			size = Integer.parseInt(args[size + 1]);
		}

		size = 4;
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

		if (!train) {
			System.out.println("Getting my problems...");
			problems = IcfpClient.myproblems();			
		} else {
			System.out.println("Getting training problems...");
			problems = new Problem[3];
			Random random = new Random();
			for (int i = 0; i < 3; i ++) {
				int newSize = size > 0 ? size : random.nextInt(28) + 3;
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
	            	//solver.solve(problem);
	            	success++;
	            } catch (Exception e) {
	            	System.err.println(e.getMessage());
	            	fail++;
	            }
	        } else {
	            System.out.println("No solver found. Problem remained untouched.%n");
	        }
		}

		// print result
		System.out.format("success: %d, fail %d%n", success, fail);
	}
	
    private static final List<Solver> SOLVERS = ImmutableList.<Solver> of(
            Size3Solver.SOLVER,
            Size4OneUnary.SOLVER,
            Size4TwoUnary.SOLVER,
            Size4OneBinary.SOLVER);

    private static Solver findSolver(Problem problem) {
        for (Solver solver : SOLVERS) {
            if (solver.canSolve(problem)) return solver;
        }
        return null;
    }
}
