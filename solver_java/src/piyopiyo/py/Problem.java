package piyopiyo.py;

enum Operator {
	not, shl1, shr1, shr4, shr16,
    op2, and, or, xor, plus, 
    if0, fold, tfold
}

public class Problem {
	public String id;
	public int size;
	public Operator[] operators;
}
