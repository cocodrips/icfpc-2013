package piyopiyo.py;

import static org.junit.Assert.*;

import java.util.Random;

import net.arnx.jsonic.JSON;

import org.junit.Test;

import piyopiyo.py.expressions.Constant;
import piyopiyo.py.expressions.Not;
import piyopiyo.py.expressions.Program;
import piyopiyo.py.expressions.Variable;

public class IcfpClientTest {
	
	@Test
	public void test() throws Exception {
		TrainRequest trainRequest = new TrainRequest(3, null);
		Problem problem = IcfpClient.train(trainRequest);
		assertEquals(problem.size, trainRequest.size);
	
		EvalRequest evalRequest = new EvalRequest(problem.id, generateArgs(10));
		EvalResponse evalResponse = IcfpClient.eval(evalRequest);
		assertEquals(10, evalResponse.outputs.length);

		Program ans = dummySolve(problem, evalRequest.arguments, evalResponse.outputs);
		GuessRequest guessRequest = new GuessRequest(problem.id, ans);
		GuessResponse guessResponse = IcfpClient.guess(guessRequest);
		assertEquals(guessResponse.status, GuessResponse.Status.win);
	}
	
	static Program dummySolve(Problem pr, long[] args, long[] results) {
		Variable var = new Variable("x");
		Program varPro, onePro, zeroPro;
		switch (pr.operators[0]) {
		case not:
			varPro = new Program(var, new Not(var));
			onePro = new Program(var, new Not(Constant.ONE));
			zeroPro = new Program(var, new Not(Constant.ZERO));
		case shl1:
			varPro = new Program(var, new Not(var));
			onePro = new Program(var, new Not(Constant.ONE));
			zeroPro = new Program(var, new Not(Constant.ZERO));
		case shr1:
			varPro = new Program(var, new Not(var));
			onePro = new Program(var, new Not(Constant.ONE));
			zeroPro = new Program(var, new Not(Constant.ZERO));
		case shr4:
			varPro = new Program(var, new Not(var));
			onePro = new Program(var, new Not(Constant.ONE));
			zeroPro = new Program(var, new Not(Constant.ZERO));
		case shr16:
			varPro = new Program(var, new Not(var));
			onePro = new Program(var, new Not(Constant.ONE));
			zeroPro = new Program(var, new Not(Constant.ZERO));
			break;
		default:
			return null;
		}
		return varPro;
	}

	static long[] generateArgs(int length) {
		Random random = new Random();
		random.setSeed(System.currentTimeMillis());
		long[] result = new long[length];
		for (int i = 0; i < length; i++) {
			result[i] = random.nextLong();
		}
		return result;
	}
}
