package tinylisp;

import java.util.ArrayList;
import java.util.List;

import tinylisp.Expression.AnonCall;
import tinylisp.Expression.Begin;
import tinylisp.Expression.Call;
import tinylisp.Expression.Define;
import tinylisp.Expression.If;
import tinylisp.Expression.Lambda;
import tinylisp.Expression.Lookup;
import tinylisp.Expression.Print;
import tinylisp.Expression.Value;
import tinylisp.Expression.Visitor;

public class Interpreter implements Visitor<Object> {
	
	Environment globals = Globals.getGlobals();
	Environment env;
	
	public Interpreter() {
		this.env = globals;
	}
	
	public void interpret(List<Expression> expressions, boolean repl) {
		try {
			for (Expression e : expressions) {
				Object val = evaluate(e);
				if (repl && val != null) System.out.println(val);
			}
		} catch (RuntimeError e) {
			TinyLisp.error(e.line, e.message);
		}
	}
	
	public Object evaluate(Expression e) {
		return e.accept(this);
	}

	@Override
	public Object visitCall(Call c) {
		Object found = env.get(c.callee);
		if (!(found instanceof Callable)) {
			throw new RuntimeError(c.callee.line, "Callee not of type 'lambda'");
		}
		
		Callable func = (Callable)found;

		if (c.args.size() < func.arity()) {
			throw new RuntimeError(c.callee.line, "too few arguments (at least: " + func.arity() + " get: " + c.args.size() + ")");
		}
		
		List<Object> arguments = new ArrayList<Object>();
		for (Expression expr : c.args) {
			arguments.add(evaluate(expr));
		}
		
		Object val = func.call(this, arguments, c.callee.line);
		
		return val;
	}
	
	@Override
	public Object visitAnonCall(AnonCall c) {
	
		Function func = new Function(c.lambda, env);
		
		if (c.args.size() < func.arity()) {
			throw new RuntimeError(c.lambdaWord.line, "too few arguments (at least: " + func.arity() + " get: " + c.args.size() + ")");
		}
		
		List<Object> arguments = new ArrayList<Object>();
		for (Expression expr : c.args) {
			arguments.add(evaluate(expr));
		}
		
		Object val = func.call(this, arguments, c.lambdaWord.line);
		
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
	
	@Override
	public Object visitIf(If i) {
		Object value = evaluate(i.condition);
			
		if (isTruthy(value)) {
			return evaluate(i.then);
		} else {
			return evaluate(i.elseExpr);
		}
	}
	
	@Override
	public Object visitBegin(Begin b) {
		Environment closure = new Environment(this.env);
		Environment prev = this.env;
		this.env = closure;
		try {
			for(int i = 0; i < b.expressions.size() - 1; i++) {
				evaluate(b.expressions.get(i));
			}			
			return evaluate(b.expressions.get(b.expressions.size() - 1));
		} finally {
			this.env = prev;
		}
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
	
	private boolean isTruthy(Object value) {
		//scheme has weird truthy values
		if ((value instanceof Boolean)) {
			return (Boolean)value;
		}
		return true;
	}
	
}
