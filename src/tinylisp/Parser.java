package tinylisp;

import java.util.ArrayList;
import java.util.List;

import static tinylisp.TokenType.*;


public class Parser {
	
	int current;
	List<Token> tokens;
	
	Parser(List<Token> tokens) {
		this.tokens = tokens;
	}
	
	public List<Expression> parse() {
		List<Expression> expressions = new ArrayList<Expression>();
		while (!atEnd()) {
			expressions.add(expression());
		}
		return expressions;
	}
	
	private Expression expression() {
		consume(LEFT_PAREN, "Expect '(' at start of expression");
		if (match(LAMBDA)) return lambda();
		return call();
	}
	
	private Expression lambda() {
		
	}
	
	private Expression call() {
		
	}
	
	private void consume(TokenType type, String errorMessage) {
		if (!match(type)) {
			TinyLisp.error(tokens.get(current).line, errorMessage);
		}
	}
	
	private boolean match(TokenType type) {
		if (tokens.get(current).type == type) {
			advance();
			return true;
		}
		return false;
	}
	
	private boolean atEnd() {
		return current >= tokens.size();
	}
	
	private void advance() {
		current++;
	}
}
