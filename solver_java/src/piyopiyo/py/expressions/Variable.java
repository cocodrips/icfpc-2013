package piyopiyo.py.expressions;

/**
 *  Represents a variable.
 */
public class Variable extends Term {
    private final String name;

    private long value;
    private boolean hasValue = false;

    public Variable(String name) {
        this.name = name;
    }

    void bind(long value) {
        this.value = value;
        hasValue = true;
    }

    void unbind() {
        hasValue = false;
    }

    @Override
    public long eval() {
        if (hasValue) {
            return value;
        } else {
            throw new IllegalStateException("Variable is unbound.");
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
