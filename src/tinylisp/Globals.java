package tinylisp;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
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
			
			public String toString() {
				return "<native lambda>";
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
			
			public String toString() {
				return "<native lambda>";
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
			
			public String toString() {
				return "<native lambda>";
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
					if ((Integer)arg == 0) {
						throw new RuntimeError(line, "/ by zero");
					}
					total /= (Integer)arg;
				}
				return total;
			}

			@Override
			public int arity() {
				return 1;
			}
			
			public String toString() {
				return "<native lambda>";
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
			
			public String toString() {
				return "<native lambda>";
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
			
			public String toString() {
				return "<native lambda>";
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
			
			public String toString() {
				return "<native lambda>";
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
			
			public String toString() {
				return "<native lambda>";
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
			
			public String toString() {
				return "<native lambda>";
			}
		});
		
		env.define(new Token("clock"), new Callable() {
			@Override
			public Object call(Interpreter interpreter, List<Object> arguments, int line) {
				return (int)(System.nanoTime() / 1000000);
			}

			@Override
			public int arity() {
				return 0;
			}
			
			public String toString() {
				return "<native lambda>";
			}
		});
		
		env.define(new Token("print"), new Callable() {
			@Override
			public Object call(Interpreter interpreter, List<Object> arguments, int line) {
				StringBuilder sb = new StringBuilder();
				arguments.forEach(arg -> {
					if (arg == null) {
						sb.append("()");
					} else {
						sb.append(arg);						
					}
				});
				System.out.println(sb);
				return null;
			}

			@Override
			public int arity() {
				return 1;
			}
			
			public String toString() {
				return "<native lambda>";
			}
		});
		
		env.define(new Token("import"), new Callable() {

			@Override
			public Object call(Interpreter interpreter, List<Object> arguments, int line) {
				for (Object arg : arguments) {
					if (!(arg instanceof String)) {
						throw new RuntimeError(line, "Expected string, but got " + arg);
					}
					String loc = (String)arg;
					try {
						byte[] bytes = Files.readAllBytes(Paths.get(loc));
						Tokenizer tokenizer = new Tokenizer(new String(bytes, Charset.defaultCharset()));
						Parser parser = new Parser(tokenizer.Tokenize());
						interpreter.interpret(parser.parse(), false);
					} catch(Exception e) {
						throw new RuntimeError(line, "Error in reading file");
					}
				}
				return null;
			}

			@Override
			public int arity() {
				return 1;
			}
		});
		
		env.define(new Token("cons"), new Callable() {

			@Override
			public Object call(Interpreter interpreter, List<Object> arguments, int line) {
				if (arguments.size() != 2) {
					throw new RuntimeError(line, "wrong number of arguments (expected: 2 got: " + arguments.size() + ")");
				}
				return new Cons(arguments.get(0), arguments.get(1));
			}

			@Override
			public int arity() {
				return 2;
			}
			
		});
		
		env.define(new Token("car"), new Callable() {

			@Override
			public Object call(Interpreter interpreter, List<Object> arguments, int line) {
				if (arguments.size() != 1) {
					throw new RuntimeError(line, "wrong number of arguments (expected: 1 got: " + arguments.size() + ")");
				}
				if (!(arguments.get(0) instanceof Cons)) {
					throw new RuntimeError(line, "Attempt to get car on " + arguments.get(0));
				}
				return ((Cons)arguments.get(0)).car;
			}

			@Override
			public int arity() {
				return 1;
			}
			
		});
		
		env.define(new Token("cdr"), new Callable() {

			@Override
			public Object call(Interpreter interpreter, List<Object> arguments, int line) {
				if (arguments.size() != 1) {
					throw new RuntimeError(line, "wrong number of arguments (expected: 1 got: " + arguments.size() + ")");
				}
				if (!(arguments.get(0) instanceof Cons)) {
					throw new RuntimeError(line, "Attempt to get cdr on " + arguments.get(0));
				}
				return ((Cons)arguments.get(0)).cdr;
			}

			@Override
			public int arity() {
				return 1;
			}
			
		});
		
		env.define(new Token("cadr"), new Callable() {

			@Override
			public Object call(Interpreter interpreter, List<Object> arguments, int line) {
				if (arguments.size() != 1) {
					throw new RuntimeError(line, "wrong number of arguments (expected: 1 got: " + arguments.size() + ")");
				}
				if (!(arguments.get(0) instanceof Cons)) {
					throw new RuntimeError(line, "Attempt to get cadr on " + arguments.get(0));
				}
				Object next = ((Cons)arguments.get(0)).cdr;
				if (!(next instanceof Cons)) {
					throw new RuntimeError(line, "Attempt to get car of " + next);
				}
				return ((Cons)next).car;
			}

			@Override
			public int arity() {
				return 1;
			}
			
		});
		
		return env;
	}
}
