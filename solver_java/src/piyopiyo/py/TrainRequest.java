package piyopiyo.py;

public class TrainRequest {
	public TrainRequest(int size, Operators operators)
	{
		this.size = size;
		this.operators = operators;
	}
	public int size;
	public Operators operators;
	public enum Operators{
		fold, tfold
	}
}
