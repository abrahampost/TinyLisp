package tinylisp;

public class RuntimeError extends RuntimeException {
	String message;
	Token token;
	RuntimeError(Token token, String message) {
		this.token = token;
		this.message = message;
	}
}
