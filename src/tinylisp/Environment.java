package tinylisp;

import java.util.HashMap;
import java.util.Map;

public class Environment {
	Environment enclosing;
	Map<String, Object> values;
	
	public Environment(Environment enclosing) {
		this.enclosing = enclosing;
		values = new HashMap<String, Object>();
	}
	
	public void define(Token var, Object value) {
		String name = var.text;
		this.values.put(name, value);
	}
	
	public Object get(Token var) {
		String name = var.text;
		Object found = this.values.get(name);
		if (found != null) {
			return found;
		}
		if (enclosing != null) {
			return enclosing.get(var);
		}
		throw new RuntimeError(var.line, "unbound symbol '" + name + "'");
	}
}
