package tinylisp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static tinylisp.TokenType.*;

public class Tokenizer {
	String source;
	int start;
	int current;
	int line;
	List<Token> tokens = new ArrayList<Token>();
	Map<String, TokenType> reserved = new HashMap<String, TokenType>();
	
	public Tokenizer(String source) {
		this.source = source;
		this.reserved.put("true", TRUE);
		this.reserved.put("false", FALSE);
		this.reserved.put("lambda", LAMBDA);
		this.reserved.put("define", DEFINE);
	}
	
	public List<Token> Tokenize() {
		while (!atEnd()) {
			start = current;
			nextToken();
		}
		return tokens;
	}
	
	public void nextToken() {		
		char c = advance();
		switch (c) {
			//ignore whitespace
			case ' ':
			case '\t':
				break;
			//make sure line count is updated
			case '\n': line++; break;
			case '(': addToken(LEFT_PAREN); break;
			case ')': addToken(RIGHT_PAREN); break;
			
			case '"': string(); break;
			default:
				if (isNum(c)) {
					number();
				} else if (isValidAscii(c)) {
					symbol();
				} else {
					TinyLisp.error(line, "Unrecognized token -> " + c);
				}
				break;
		}
	}
	
	private void string() {
		while (peek() != '"' && !atEnd()) {
			if (peek() == '\n') line++;
			advance();
		}
		
		if (atEnd()) {
			TinyLisp.error(line, "unclosed string literal");
			return;
		}
		
		advance();
		//get the string without the quotes
		String value = source.substring(start + 1, current - 1);
		
		addToken(STRING, value);
	}
	
	private void number() {
		while (isNum(peek())) {
			advance();
		}
		String num = source.substring(start, current);
		
		addToken(NUM, Integer.parseInt(num));
	}
	
	private void symbol() {
		while (isValidAscii(peek())) {
			advance();
		}
		
		String text = source.substring(start, current);
		
		TokenType type = reserved.get(text);
		
		if (type == null) type = SYMBOL;
		
		addToken(type, text);
	}
	
	private boolean isNum(char c) {
		return c >= '0' && c <= '9';
	}
	
	private boolean isAlpha(char c) {
		return 	(c >= 'a' && c <= 'z') ||
				(c >= 'A' && c <= 'Z') ||
				c == '_';
	}
	
	private boolean isAlphaNum(char c) {
		return isNum(c) || isAlpha(c);
	}
	
	private boolean isValidAscii(char c) {
		//all non-stop characters
		return c >= 33 && c <= 127 && c != '(' && c != ')';
	}
	
	private char advance() {
		current++;
		return source.charAt(current - 1);
	}
	
	private char peek() {
		if (atEnd()) return '\0';
			return source.charAt(current);
	}
	
	
	private boolean match(char expected) {
		if (atEnd()) return false;
		if (source.charAt(current) != expected) return false;
		current++;
		return true;
	}
	
	private void addToken(TokenType type) {
		addToken(type, null);
	}
	
	private void addToken(TokenType type, Object literal) {
		tokens.add(new Token(type, source.substring(start, current), literal, line));
	}
	
	private boolean atEnd() {
		return current >= source.length();
	}
	
	
	
}
