package tinylisp;

import java.util.List;

import tinylisp.Expression.Call;
import tinylisp.Expression.Lambda;
import tinylisp.Expression.Visitor;

public class Interpreter implements Visitor {
	
	public void interpret(List<Expression> expressions) {
		return;
	}

	@Override
	public Object visitCall(Call c) {
		return null;
	}

	@Override
	public Object visitLambda(Lambda l) {
		return null;
	}
	
}
