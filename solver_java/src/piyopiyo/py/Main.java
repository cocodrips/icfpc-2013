package piyopiyo.py;

import static piyopiyo.py.IcfpJson.ICFPJSON;
import piyopiyo.py.solvers.Size3Solver;

public class Main {
    public static void main(String[] args) throws Exception {
        Size3Solver.SOLVER.solve(ICFPJSON.parse(System.in, Problem.class));
    }
}
