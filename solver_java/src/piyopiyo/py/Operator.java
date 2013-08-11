package piyopiyo.py;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import piyopiyo.py.expressions.*;

public enum Operator {
    not, shl1, shr1, shr4, shr16,
    and, or, xor, plus,
    if0, fold, tfold, bonus;

    public static final Map<Operator, UnaryExpressionFactory> UNARY_OPERATORS =
        ImmutableMap.<Operator, UnaryExpressionFactory> builder()
            .put(not, Not.FACTORY)
            .put(shl1, Shl1.FACTORY)
            .put(shr1, Shr1.FACTORY)
            .put(shr4, Shr4.FACTORY)
            .put(shr16, Shr16.FACTORY).build();

    public static final Map<Operator, BinaryExpressionFactory> BINARY_OPERATORS =
        ImmutableMap.<Operator, BinaryExpressionFactory> builder()
            .put(and, And.FACTORY)
            .put(or, Or.FACTORY)
            .put(xor, Xor.FACTORY)
            .put(plus, Plus.FACTORY).build();
}
