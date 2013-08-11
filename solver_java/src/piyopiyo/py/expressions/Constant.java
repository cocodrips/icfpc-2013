package piyopiyo.py.expressions;

/**
 *  Represents a constant, i.e. either 0 or 1.
 */
public class Constant extends Term {
    private final long value;

    private Constant(long value) {
        this.value = value;
    }

    public static Constant of(long value) {
    	if (value == 0) return ZERO;
    	if (value == 1) return ONE;
    	throw new IllegalArgumentException("Invalid value.");
    }

    @Override
    public long eval() {
        return value;
    }

    @Override
    public boolean isGround() {
        return true;
    }

    @Override
    public String toString() {
        return Long.toString(value);
    }

    public static final Constant ZERO = new Constant(0);
    public static final Constant ONE = new Constant(1);

}
