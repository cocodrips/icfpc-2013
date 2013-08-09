package piyopiyo.py;

import piyopiyo.py.expressions.Program;

public class GuessRequest {
	public GuessRequest(String id, Program program) {
		this.id = id;
		this.program = program.toString();
	}
	public String id;
	public String program;
}
