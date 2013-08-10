package piyopiyo.py.parser;

import static org.junit.Assert.*;

import org.junit.Test;

import piyopiyo.py.expressions.Program;
import piyopiyo.py.parser.BVParser.Reader;

public class BVParserTest {

	@Test
	public void testReadToken() {
		BVParser.Reader reader = new BVParser.Reader("(lambda piyo");
		assertEquals(true, reader.readBra());
		assertEquals("lambda", reader.readToken());
	}

	@Test
	public void test() {
		String[] sources = new String[] {
			"(lambda (x) (plus x 1))",
			"(lambda (x) (fold x 0 (lambda (x y) (or (not x) y))))"
		};
		for (String source : sources) {
			Program program = BVParser.PARSER.parse(source);
			assertEquals(source, program.toString());
		}
	}

}
