package tinylisp;

enum TokenType {
	//single character tokens
	LEFT_PAREN, RIGHT_PAREN,

	//double character tokens
	BANG_EQUAL, LESS_EQUAL, GREATER_EQUAL,
	
	//multi-character tokens
	SYMBOL, STRING, NUM,
	
	//reserved words
	TRUE, FALSE, LAMBDA, DEFINE,
	
	EOF
}

class Token {
	TokenType type;
	String text;
	Object literal;
	int line;
	Token(TokenType type, String text, Object literal, int line) {
		this.type = type;
		this.text = text;
		this.literal = literal;
		this.line = line;
	}
}