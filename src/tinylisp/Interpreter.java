package tinylisp;

import java.util.List;

import tinylisp.Expression.Call;
import tinylisp.Expression.Define;
import tinylisp.Expression.Lambda;
import tinylisp.Expression.Lookup;
import tinylisp.Expression.Print;
import tinylisp.Expression.Value;
import tinylisp.Expression.Visitor;

public class Interpreter implements Visitor<Object> {
	
	Environment env;
	
	public Interpreter() {
		this.env = new Environment(null);
	}
	
	public void interpret(List<Expression> expressions) {
		try {
			for (Expression e : expressions) {
				evaluate(e);
			}
		} catch (RuntimeError e) {
			TinyLisp.error(e.token.line, e.message);
		}
	}
	
	public Object evaluate(Expression e) {
		return e.accept(this);
	}

	@Override
	public Object visitCall(Call c) {
		return null;
	}

	@Override
	public Object visitLambda(Lambda l) {
		return null;
	}

	@Override
	public Object visitValue(Value v) {
		return v.value;
	}

	@Override
	public Object visitDefine(Define d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitLookup(Lookup L) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitPrint(Print p) {
		Object val = evaluate(p.expr);
		System.out.println(val);
		return null;
	}
	
}
