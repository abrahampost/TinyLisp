package tinylisp;

import java.util.List;

import tinylisp.Expression.Lambda;

interface Callable {
	Object call(Interpreter interpreter, List<Object> arguments);
}
public class Function implements Callable {
	Lambda lambda;
	Environment closure;
	
	Function(Expression.Lambda lambda, Environment closure) {
		this.closure = closure;
		this.lambda = lambda;
	}
	
	@Override
	public Object call(Interpreter interpreter, List<Object> arguments) {
		Environment environment = new Environment(closure);
		for (int i = 0; i < lambda.params.size(); i++) {
			environment.define(lambda.params.get(i), arguments.get(i));
		}
		
		return interpreter.executeFunction(lambda.body, environment);
		
	}
	
	public String toString() {
		return "<lambda>";
	}
	
}
