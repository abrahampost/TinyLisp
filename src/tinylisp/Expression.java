package tinylisp;

import java.util.List;

public abstract class Expression {
	
	interface Visitor<R> {
		R visitCall(Call c);
		R visitLambda(Lambda l);
	}
	
	static class Call extends Expression {
		Token symbol;
		List<Token> args;
		
		@Override
		<R> R accept(Visitor<R> visitor) {
			return visitor.visitCall(this);
		}
		
		
	}
	
	static class Lambda extends Expression {
		Token symbol;
		List<Token> params;
		Expression body;
		
		@Override
		<R> R accept(Visitor<R> visitor) {
			return visitor.visitLambda(this);
		}
		
	}
	
	abstract<R> R accept(Visitor<R> visitor);
}
