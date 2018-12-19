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
						throw new RuntimeError(line, "expected num, received " + arg.getClass().getSimpleName());
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
		
		env.define(new Token("="), new Callable() {

			@Override
			public Object call(Interpreter interpreter, List<Object> arguments, int line) {
				Object initial = arguments.get(0);
				for (int i = 1; i < arguments.size(); i++) {
					if (!initial.equals(arguments.get(i))) {
						return false;
					}
				}
				return true;
			}

			@Override
			public int arity() {
				return 2;
			}
			
		});
		
		env.define(new Token("<"), new Callable() {
			@Override
			public Object call(Interpreter interpreter, List<Object> arguments, int line) {
				for (int i = 1; i < arguments.size(); i++) {
					Object before = arguments.get(i-1);
					if (!(before instanceof Integer)) {
						throw new RuntimeError(line, "number required, but got " + before);
					}
					Object current = arguments.get(i);
					if (!(current instanceof Integer)) {
						throw new RuntimeError(line, "number required, but got " + current);
					}
					if ((Integer)before >= (Integer)current) return false;
				}
				return true;
			}

			@Override
			public int arity() {
				return 2;
			}
		});
		
		env.define(new Token("<="), new Callable() {
			@Override
			public Object call(Interpreter interpreter, List<Object> arguments, int line) {
				for (int i = 1; i < arguments.size(); i++) {
					Object before = arguments.get(i-1);
					if (!(before instanceof Integer)) {
						throw new RuntimeError(line, "number required, but got " + before);
					}
					Object current = arguments.get(i);
					if (!(current instanceof Integer)) {
						throw new RuntimeError(line, "number required, but got " + current);
					}
					if ((Integer)before > (Integer)current) return false;
				}
				return true;
			}

			@Override
			public int arity() {
				return 2;
			}
		});
		
		env.define(new Token(">"), new Callable() {
			@Override
			public Object call(Interpreter interpreter, List<Object> arguments, int line) {
				for (int i = 1; i < arguments.size(); i++) {
					Object before = arguments.get(i-1);
					if (!(before instanceof Integer)) {
						throw new RuntimeError(line, "number required, but got " + before);
					}
					Object current = arguments.get(i);
					if (!(current instanceof Integer)) {
						throw new RuntimeError(line, "number required, but got " + current);
					}
					if ((Integer)before <= (Integer)current) return false;
				}
				return true;
			}

			@Override
			public int arity() {
				return 2;
			}
		});
		
		env.define(new Token(">="), new Callable() {
			@Override
			public Object call(Interpreter interpreter, List<Object> arguments, int line) {
				for (int i = 1; i < arguments.size(); i++) {
					Object before = arguments.get(i-1);
					if (!(before instanceof Integer)) {
						throw new RuntimeError(line, "number required, but got " + before);
					}
					Object current = arguments.get(i);
					if (!(current instanceof Integer)) {
						throw new RuntimeError(line, "number required, but got " + current);
					}
					if ((Integer)before < (Integer)current) return false;
				}
				return true;
			}

			@Override
			public int arity() {
				return 2;
			}
		});
		return env;
	}
}
