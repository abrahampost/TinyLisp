package tinylisp;

import java.util.ArrayList;
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
	
	public void interpret(List<Expression> expressions, boolean repl) {
		try {
			for (Expression e : expressions) {
				Object val = evaluate(e);
				if (repl && val != null) System.out.println(val);
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
		Object found = env.get(c.callee);
		if (!(found instanceof Callable)) {
			throw new RuntimeError(c.callee, "Callee not of type 'lambda'");
		}
		
		List<Object> arguments = new ArrayList<Object>();
		for (Expression expr : c.args) {
			arguments.add(evaluate(expr));
		}
		
		Function func = (Function)found;
		Object val = func.call(this, arguments);
		
		return val;
	}

	@Override
	public Object visitLambda(Lambda l) {
		return new Function(l, env);
	}

	@Override
	public Object visitValue(Value v) {
		return v.value;
	}

	@Override
	public Object visitDefine(Define d) {
		Object val = evaluate(d.body);
		env.define(d.name, val);
		return null;
	}

	@Override
	public Object visitLookup(Lookup L) {
		return env.get(L.name);
	}

	@Override
	public Object visitPrint(Print p) {
		Object val = evaluate(p.expr);
		System.out.println(val);
		return null;
	}
	
	public Object executeFunction(Expression body, Environment closure) {
		Environment prev = this.env;
		this.env = closure;
		Object val;
		try {
			val = evaluate(body);
		} finally {
			this.env = prev;
		}
		return val;
	}
	
}
