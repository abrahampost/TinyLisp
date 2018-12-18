package tinylisp;

public class RuntimeError extends RuntimeException {
	String message;
	int line;
	RuntimeError(int line, String message) {
		this.line = line;
		this.message = message;
	}
}
