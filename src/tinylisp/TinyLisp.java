package tinylisp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TinyLisp {

	static final String VERSION = "tinylispv1 (REPL)";

	private static final Interpreter interpreter = new Interpreter();
	private static boolean hadError = false;
	
	public static void main(String[] args) throws IOException {
		if (args.length > 1) {
			System.err.println("Error: Proper usage is tinylisp [file]");
			System.exit(1);
		} else if (args.length == 1) {
			runFile(args[0]);
		} else {
			runRepl();
		}
	}
	
	private static void runRepl() throws IOException {
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		String readIn = "";
		while (true) {
			if (readIn.length() == 0) {
				System.out.print("> ");				
			} else {
				System.out.print("  ");
			}
			
			readIn += reader.readLine();
			
			if (matchingBrackets(readIn)) {
				run(readIn + "\n", true);
				readIn = "";
			}
			
			hadError = false;
		}
	}
	
	private static void runFile(String loc) throws IOException {
		byte[] bytes = Files.readAllBytes(Paths.get(loc));
		run(new String(bytes, Charset.defaultCharset()), false);
		if (hadError) {
			System.exit(1);
		}
	}
	
	private static void run(String source, boolean repl) {
		Tokenizer tokenizer = new Tokenizer(source);
		List<Token> tokens = tokenizer.Tokenize();
		if (hadError) return;
		Parser parser = new Parser(tokens);
		List<Expression> expressions = parser.parse();
		if (hadError) return;
		
		interpreter.interpret(expressions, repl);
	}
	
	public static void error(int line, String message) {
		System.out.printf("ERROR [line %d]: %s\n", line, message);
		hadError = true;
	}
	
	private static boolean matchingBrackets(String input) {
		int left = 0, right = 0;
		for (char c: input.toCharArray()) {
			if (c == ')') left++;
			if (c == '(') right++;
		}
		return left == right;
	}
	
}
