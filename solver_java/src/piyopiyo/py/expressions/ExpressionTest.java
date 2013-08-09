package piyopiyo.py.expressions;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExpressionTest {
    @Test
    public void testShl1() {
    	Variable x = new Variable("x");
    	Program p = new Program(x, new Shl1(x));
        assertEquals(0xFFFFFFFFFFFFFFFEL, p.eval(0xFFFFFFFFFFFFFFFFL));
    }

    @Test
    public void testShr1() {
    	Variable x = new Variable("x");
    	Program p = new Program(x, new Shr1(x));
        assertEquals(0x7FFFFFFFFFFFFFFFL, p.eval(0xFFFFFFFFFFFFFFFFL));
    }

    @Test
    public void testShr4() {
    	Variable x = new Variable("x");
    	Program p = new Program(x, new Shr4(x));
        assertEquals(0x0FFFFFFFFFFFFFFFL, p.eval(0xFFFFFFFFFFFFFFFFL));
    }

    @Test
    public void testShr16() {
    	Variable x = new Variable("x");
    	Program p = new Program(x, new Shr16(x));
        assertEquals(0x0000FFFFFFFFFFFFL, p.eval(0xFFFFFFFFFFFFFFFFL));
    }

    @Test
    public void testFold() {
        Variable x = new Variable("x");
        Variable y = new Variable("y");
        Variable z = new Variable("z");
        Expression e = new Fold(x, Constant.ZERO, y, z, new Plus(y, z));
        Program p = new Program(x, e);

        assertEquals("(lambda (x) (fold x 0 (lambda (y z) (plus y z))))",
                     p.toString());
        assertEquals(36, p.eval(0x0102030405060708L));
        assertEquals(576, p.eval(0x8070605040302010L));
    }
}
