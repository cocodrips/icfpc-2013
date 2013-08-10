package piyopiyo.py.parser;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import piyopiyo.py.Operator;
import piyopiyo.py.expressions.BinaryExpressionFactory;
import piyopiyo.py.expressions.Constant;
import piyopiyo.py.expressions.Expression;
import piyopiyo.py.expressions.Fold;
import piyopiyo.py.expressions.If0;
import piyopiyo.py.expressions.Program;
import piyopiyo.py.expressions.UnaryExpressionFactory;
import piyopiyo.py.expressions.Variable;

public class BVParser {

	public static final BVParser PARSER = new BVParser();

	public Program parse(String source) {
		Reader reader = new Reader(source);
		try {
			reader.readBra();
			if (!reader.readToken().equals("lambda")){
				throw new ParseException("lambda not found.");
			}
			// read variable
			if(!reader.readBra()){
				throw new ParseException("argument not found.");
			}
			Map<String, Variable> env = new HashMap<String, Variable>();
			String varName = reader.readToken();
			Variable variable = new Variable(varName);
			env.put(varName, variable);		
			if(!reader.readKet()) {
				throw new ParseException("ket not found.");
			}

			// read expression
			Expression expression;
			expression = parseRec(reader, env);
	
			Program program = new Program(variable, expression);
			return program;
		} catch (Exception e) {
			throw new ParseException(
					String.format("at %d: %s", reader.getIndex(), e.getMessage()));
		}
	}

	private static Expression parseRec(Reader reader, Map<String, Variable> env) {
		if (reader.readBra()) {
			String opName = reader.readToken();
			Operator operator = Operator.valueOf(opName);
			UnaryExpressionFactory uFactory = Operator.UNARY_OPERATORS.get(operator);
			BinaryExpressionFactory bFactory = Operator.BINARY_OPERATORS.get(operator);
			Expression result;
			if (uFactory != null) {
				result = uFactory.create(parseRec(reader, env));
			} else if (bFactory != null) {
				Expression e1 = parseRec(reader, env);
				Expression e2 = parseRec(reader, env);
				result =  bFactory.create(e1, e2);
			} else if (operator == Operator.if0) {
				Expression e1 = parseRec(reader, env);
				Expression e2 = parseRec(reader, env);
				Expression e3 = parseRec(reader, env);
				result = new If0(e1, e2, e3);
			} else if (operator == Operator.fold) {
				Expression e1 = parseRec(reader, env);
				Expression e2 = parseRec(reader, env);
				if(!reader.readBra() || !reader.readToken().equals("lambda")){
					throw new ParseException("lambda not found.");
				}
				// variables
				if(!reader.readBra()){
					throw new ParseException("argument not found.");
				}
				String varName1 = reader.readToken();
				Variable variable1 = new Variable(varName1);
				String varName2 = reader.readToken();
				Variable variable2 = new Variable(varName2);
				Variable temp1 = env.remove(varName1);
				Variable temp2 = env.remove(varName2);
				if(!reader.readKet()) {
					throw new ParseException("ket not found.");
				}

				// make temp env
				env.put(varName1, variable1);
				env.put(varName2, variable2);
				Expression e3 = parseRec(reader, env);
				
				// rollback empv
				if (temp1 != null) {
					env.remove(varName1);
					env.put(varName1, temp1);
				}
				if (temp2 != null) {
					env.remove(varName2);
					env.put(varName2, temp2);
				}
				if(!reader.readKet()) {
					throw new ParseException("ket not found.");
				}
				result = new Fold(e1, e2, variable1, variable2, e3);
			} else {
				throw new ParseException(opName + "is not a operator.");
			}
			if(!reader.readKet()) {
				throw new ParseException("ket not found.");
			}
			return result;
		} else {
			String varName = reader.readToken();
			if (varName.equals("0")) {
				return Constant.ZERO;
			} else if (varName.equals("1")) {
				return Constant.ONE;
			}
			Variable variable = env.get(varName);
			if (variable == null)
				throw new ParseException("undeclared variable " + varName + ".");
			return variable;
		}
	}

	public static class Reader{
		private String source;
		int index;
		public Reader(String source) {
			this.source = source;
			index = 0;
		}
		
		public int getIndex() {
			return index;
		}

		public boolean readBra() {
			skipSpaces();
			if(source.charAt(index) != '(') {
				return false;
			} else {
				index ++;
				return true;
			}
		}

		public boolean readKet() {
			skipSpaces();
			if(source.charAt(index) != ')') {
				return false;
			} else {
				index ++;
				return true;
			}
		}

		public String readToken() {
			skipSpaces();
			int startIndex = index;
			char ch = source.charAt(index);
			while(ch != ' ' && ch != '(' && ch != ')') {
				index++;
				ch = source.charAt(index);
			}
			return source.substring(startIndex, index);
		}

		private void skipSpaces() {
			while(source.charAt(index) == ' ') {
				index++;
			}
		}
	}

}
