package tinylisp;

import java.util.List;

public class Globals {
	public static Environment getGlobals() {
		Environment env = new Environment(null);
		env.define(new Token("+"), new Callable() {

			@Override
			public Object call(Interpreter interpreter, List<Object> arguments, int line) {
				int total = 0;
				for (Object arg: arguments) {
					if (!(arg instanceof Integer)) {
						throw new RuntimeError(line, "expected int, received " + arg.getClass().getSimpleName());
					}
					total += (Integer)arg;
				}
				return total;
			}

			@Override
			public int arity() {
				return 1;
			}
			
		});
		env.define(new Token("-"), new Callable() {

			@Override
			public Object call(Interpreter interpreter, List<Object> arguments, int line) {
				int start = (int) arguments.get(0);
				for (int i = 1; i < arguments.size(); i++) {
					Object arg = arguments.get(i);
					if (!(arg instanceof Integer)) {
						throw new RuntimeError(line, "expected int, received " + arg.getClass().getSimpleName());
					}
					start -= (Integer)arg;
				}
				return start;
			}

			@Override
			public int arity() {
				return 1;
			}
			
		});
		env.define(new Token("*"), new Callable() {

			@Override
			public Object call(Interpreter interpreter, List<Object> arguments, int line) {
				int total = (int) arguments.get(0);
				for (int i = 1; i < arguments.size(); i++) {
					Object arg = arguments.get(i);
					if (!(arg instanceof Integer)) {
						throw new RuntimeError(line, "expected int, received " + arg.getClass().getSimpleName());
					}
					total *= (Integer)arg;
				}
				return total;
			}

			@Override
			public int arity() {
				return 1;
			}
			
		});
		
		env.define(new Token("/"), new Callable() {

			@Override
			public Object call(Interpreter interpreter, List<Object> arguments, int line) {
				int total = (int) arguments.get(0);
				for (int i = 1; i < arguments.size(); i++) {
					Object arg = arguments.get(i);
					if (!(arg instanceof Integer)) {
						throw new RuntimeError(line, "expected int, received " + arg.getClass().getSimpleName());
					}
					total /= (Integer)arg;
				}
				return total;
			}

			@Override
			public int arity() {
				return 1;
			}
			
		});
		return env;
	}
}
