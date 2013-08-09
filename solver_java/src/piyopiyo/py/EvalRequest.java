package piyopiyo.py;

class EvalRequest {

	public static final int ARGS_MAX = 256;

	public String id;
	public String[] arguments;
	public long[] argValues;

	public EvalRequest(String id, long[] args) {
		this.id = id;
		this.argValues = args;
		this.arguments = new String[Math.min(args.length, ARGS_MAX)];
		for (int i = 0; i < arguments.length; i++) {
			arguments[i] = longToHex(args[i]);
		}
	}

	private String longToHex(long num) {
		return "0x" + Long.toHexString(num).toUpperCase();
	}
}