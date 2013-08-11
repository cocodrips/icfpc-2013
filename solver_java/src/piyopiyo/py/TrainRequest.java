package piyopiyo.py;

public class TrainRequest {
	public TrainRequest(int size, Operator[] operators)
	{
		this.size = size;
		this.operators = operators;
	}
	public int size;
	public Operator[] operators;
}
