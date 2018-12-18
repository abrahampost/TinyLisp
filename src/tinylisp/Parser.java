package tinylisp;

import java.util.ArrayList;
import java.util.List;

import static tinylisp.TokenType.*;


public class Parser {
	private static class ParseError extends RuntimeException { 
		String message;
		ParseError(String message) {
			this.message = message;
		}
	}

	
	int current;
	List<Token> tokens;
	
	Parser(List<Token> tokens) {
		this.tokens = tokens;
	}
	
	public List<Expression> parse() {
		List<Expression> expressions = new ArrayList<Expression>();
		try {
			while (!atEnd()) {
					expressions.add(expression());				
			}
		} catch (ParseError e) {
			TinyLisp.error(previous().line, e.message);
		}
		
		return expressions;
	}
	
	private Expression expression() {
		if (match(LEFT_PAREN)) return function();
		return value();
	}
	
	private Expression function() {
		if (match(SYMBOL)) return call();
		if (match(LAMBDA)) return lambda();
		if (match(DEFINE)) return define();
		if (match(PRINT)) return print();
		if (match(IF)) return ifExpr();
		if (current() == null) {
			throw new ParseError("unable to parse expression after " + previous().type + ": " + (previous().text != null ? previous().literal : ""));
		}
		throw new ParseError("" + current().text + " is not a function");
	}
	
	private Expression call() {
		Token callee = previous();
		List<Expression> args = new ArrayList<Expression>();
		do {
			args.add(expression());
		} while (!check(RIGHT_PAREN) && !atEnd());
		consume(RIGHT_PAREN, "Expect right paren after call");
		return new Expression.Call(callee, args);
	}

	private Expression lambda() {
		consume(LEFT_PAREN, "Expect '(' after lambda");
		List<Token> params = new ArrayList<Token>();
		do {
			consume(SYMBOL, "Expect parameter in lambda expression");
			params.add(previous());
		} while (!check(RIGHT_PAREN) && !atEnd());
		consume(RIGHT_PAREN, "Expect ')' after param list");
		Expression body = expression();
		consume(RIGHT_PAREN, "Expect ')' after expression");
		return new Expression.Lambda(params, body);
	}
	
	private Expression define() {
		consume(SYMBOL, "Expect symbol after 'define'");
		Token name = previous();
		Expression body = expression();
		consume(RIGHT_PAREN, "Expect ')' after 'define'");
		return new Expression.Define(name, body);
	}
	
	private Expression print() {
		Expression expr = expression();
		consume(RIGHT_PAREN, "Expect ')' after 'print'");
		return new Expression.Print(expr);
	}
	
	private Expression ifExpr() {
		Expression condition = expression();
		Expression then = expression();
		Expression elseExpr;
		if (check(RIGHT_PAREN)) {
			elseExpr = new Expression.Value(null);
		} else {
			elseExpr = expression();
		}
		consume(RIGHT_PAREN, "Expect right paren after call");
		return new Expression.If(condition, then, elseExpr);
	}
	
	private Expression value() {
		if (match(STRING, NUM, TRUE, FALSE)) {
			return new Expression.Value(previous().literal);						
		}
		if (match(SYMBOL)) {
			return new Expression.Lookup(previous());
		}
		if (current() == null) {
			throw new ParseError("unable to parse expression after " + previous().type + ": " + (previous().text != null ? previous().literal : ""));			
		}
		throw new ParseError("unable to parse expression at " + current().literal);
	}
	
	private void consume(TokenType type, String errorMessage) {
		if (!match(type)) {
			throw new ParseError(errorMessage);
			
		}
	}
	
	private TokenType peek() {
		if (current >= tokens.size()) {
			return null;
		}
		return tokens.get(current).type;
	}
	
	private boolean check(TokenType type) {
		return peek() == type;
	}
	
	private boolean match(TokenType... types) {
		for (TokenType type: types) {
			if (peek() == type) {
				advance();
				return true;
			}
		}
		return false;
	}
	
	private Token current() {
		if (!atEnd()) tokens.get(current);
		return null;
	}
	
	private Token previous() {
		return tokens.get(current - 1);
	}
	
	private boolean atEnd() {
		return current >= tokens.size();
	}
	
	private void advance() {
		current++;
	}
}
