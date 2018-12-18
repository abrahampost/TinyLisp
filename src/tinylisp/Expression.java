package tinylisp;

import java.util.List;

public abstract class Expression {
	
	interface Visitor<R> {
		R visitCall(Call c);
		R visitLambda(Lambda l);
		R visitValue(Value v);
		R visitDefine(Define d);
		R visitLookup(Lookup L);
	}
	
	static class Call extends Expression {
		Token callee;
		List<Expression> args;
		
		Call(Token callee, List<Expression> args) {
			this.callee = callee;
			this.args = args;
		}
		
		@Override
		<R> R accept(Visitor<R> visitor) {
			return visitor.visitCall(this);
		}
		
		
	}
	
	static class Lambda extends Expression {
		List<Token> params;
		Expression body;
		
		Lambda(List<Token> params, Expression body) {
			this.params = params;
			this.body = body;
		}
		
		@Override
		<R> R accept(Visitor<R> visitor) {
			return visitor.visitLambda(this);
		}
		
	}
	
	static class Define extends Expression {

		Token name;
		Expression body;
		
		Define(Token name, Expression body) {
			this.name = name;
			this.body = body;
		}
		
		@Override
		<R> R accept(Visitor<R> visitor) {
			return visitor.visitDefine(this);
		}
		
	}
	
	static class Value extends Expression {
		Object value;

		Value(Object value) {
			this.value = value;
		}
		
		@Override
		<R> R accept(Visitor<R> visitor) {
			return visitor.visitValue(this);
		}
	}
	
	static class Lookup extends Expression {

		Token name;
		
		Lookup(Token name) {
			this.name = name;
		}
		
		@Override
		<R> R accept(Visitor<R> visitor) {
			return visitor.visitLookup(this);
		}
		
	}
	
	abstract<R> R accept(Visitor<R> visitor);
}
