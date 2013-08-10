package piyopiyo.py.solvers;

import piyopiyo.py.Problem;

public abstract class Solver {
    public abstract void solve(Problem problem) throws Exception;
    public abstract boolean canSolve(Problem problem);
}
