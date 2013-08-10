package piyopiyo.py.skeltons;

import java.util.ArrayList;
import java.util.List;

import piyopiyo.py.Operator;
import piyopiyo.py.expressions.Expression;
import piyopiyo.py.expressions.Variable;

import com.google.common.collect.ImmutableList;

public abstract class Skelton {
    public abstract List<Expression> buildExpressions(List<Operator> ops,
                                                      List<Variable> vars);

    public static List<List<Skelton>> buildSkeltons(int maxSize) {
        List<List<Skelton>> lists = new ArrayList<List<Skelton>>();
        // size == 0.
        lists.add(ImmutableList.<Skelton> of());
        // size == 1.
        lists.add(ImmutableList.<Skelton> of(TermSkelton.TERM));

        for (int size = 2; size <= maxSize; size++) {
            List<Skelton> list = new ArrayList<Skelton>();
            // (op1 e).
            list.add(new UnarySkelton(lists.get(size - 1)));
            // (op2 e0 e1). Since all binary operators are commutative,
            // we can safely assume |e0| <= |e1|.
            for (int i = 1; i <= size - 1 - i; i++) {
                list.add(new BinarySkelton(lists.get(i), lists.get(size - 1 - i)));
            }
            // (if0 e0 e1 e2).
            for (int i = 1; i < size - 1; i++) {
                for (int j = 1; i + j < size - 1; j++) {
                    list.add(new If0Skelton(lists.get(i), lists.get(j), lists.get(size - 1 - i - j)));
                }
            }
            // (fold e0 e1 (lambda (x y) e2)).
            for (int i = 1; i < size - 2; i++) {
                for (int j = 1; i + j < size - 2; j++) {
                    list.add(new FoldSkelton(lists.get(i), lists.get(j), lists.get(size - 2 - i - j)));
                }
            }
            lists.add(ImmutableList.copyOf(list));
        }

        return lists;
    }
}
