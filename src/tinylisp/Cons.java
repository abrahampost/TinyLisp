package tinylisp;

public class Cons {
	public Object car;
	public Object cdr;
	
	Cons(Object car, Object cdr) {
		this.car = car;
		this.cdr = cdr;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder("(");
		Cons start = this;
		do {
			if (start.car == Globals.NIL) {
				sb.append("()");
			} else {
				sb.append(start.car.toString());
			}
			
			if (start.cdr == Globals.NIL) {
				//if cdr is null, don't print anything and stop trying to iterate through the list
				break;
			} else if (start.cdr == null) {
				throw new RuntimeException("an unspecified error occured");
			}else if ((start.cdr instanceof Cons)) {
				//if it is a link to another cons, it is a list and should iterate through
				start = (Cons)start.cdr;				
				sb.append(" ");
			} else {
				//if it is a non-Cons value, print it and then break the loop
				sb.append(" . ");
				sb.append(start.cdr.toString());
				break;
			}
			
		} while (true);
		
		sb.append(")");
		return sb.toString();
	}
}
