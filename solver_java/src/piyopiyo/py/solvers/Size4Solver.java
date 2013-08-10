package piyopiyo.py.solvers;

import static piyopiyo.py.Operator.BINARY_OPERATORS;
import static piyopiyo.py.Operator.UNARY_OPERATORS;

import java.util.ArrayList;
import java.util.List;

import piyopiyo.py.Operator;
import piyopiyo.py.expressions.Constant;
import piyopiyo.py.expressions.Expression;
import piyopiyo.py.expressions.Program;
import piyopiyo.py.expressions.UnaryExpressionFactory;
import piyopiyo.py.expressions.Variable;

public class Size4Solver extends SimpleSolver {
    public static Size4Solver SOLVER = new Size4Solver();

    private Size4Solver() {}

	@Override
	protected List<Program> getCandidates(Operator[] ops) {
		Variable x = new Variable("x");
		Expression[] vals = new Expression[]{Constant.ZERO, Constant.ONE, x};
		List<Program> result = new ArrayList<Program>();
		switch (ops.length) {
		case 1:
			if (UNARY_OPERATORS.containsKey(ops[0])) {
				UnaryExpressionFactory factory = UNARY_OPERATORS.get(ops[0]);
				for (Expression val : vals) {
					Expression inner = factory.create(val);
					Expression outer = factory.create(inner);
					Program program = new Program(x, outer);
					result.add(program);
				}
			} else if (BINARY_OPERATORS.containsKey(ops[0])) {
				for (Expression val : vals) {
					for (Expression val2 : vals) {
						Expression exp = BINARY_OPERATORS.get(ops[0]).create(val, val2);
						Program program = new Program(x, exp);
						result.add(program);
					}
				}
			} else {
				throw new SolutionNotFoundException("Operator isn't unary nor binary.");
			}
			return result;

		case 2:
			UnaryExpressionFactory[] factories = new UnaryExpressionFactory[] {
					UNARY_OPERATORS.get(ops[0]),
					UNARY_OPERATORS.get(ops[1])
			};
			if (factories[0] == null || factories[1] == null) {
				throw new SolutionNotFoundException("The two operators are not unary.");
			}
			for (int i = 0; i < 2; i ++) {
				for (Expression val : vals) {
					Expression inner = factories[i].create(val);
					Expression outer = factories[(i + 1) % 2].create(inner);
					Program program = new Program(x, outer);
					result.add(program);
				}
			}
			return result;

		default:
			throw new SolutionNotFoundException("Too much operators.");
		}
		
	}
	
}
