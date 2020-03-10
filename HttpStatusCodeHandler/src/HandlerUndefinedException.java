public class HandlerUndefinedException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private int code;
	public HandlerUndefinedException(int code) {
		super(String.valueOf(code));
		this.code = code;
	}
	public final int getCode() {
		return code;
	}
}