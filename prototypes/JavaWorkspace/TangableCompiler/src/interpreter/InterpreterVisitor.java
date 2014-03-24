package interpreter;

import java.util.Stack;

import ast.Command;
import ast.LiteralNumber;
import ast.Loop;
import ast.Program;
import ast.Statement;
import ast.Visitor;

public class InterpreterVisitor implements Visitor{

	private final Stack<Object> results = new Stack<Object>();
	
	private int indentCnt = 0;
	
	@Override
	public void visit(LiteralNumber number) {
		results.push(number.value);
	
	}
	
	@Override
	public void visit(Command cmd) {
		
		System.out.println(getIndentation() + cmd.name);
		
		try {
			Thread.sleep(cmd.waitTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visit(Loop loop) {
		System.out.println(getIndentation() + "LOOP[" + loop.numberCounter.value + "]");
		indentCnt++;
		int counter = (Integer)results.pop();
		for(int i = 0; i < counter; i++){
			for(Statement st : loop.statements){
				st.acceptPreOrder(this);
			}
		}
		indentCnt--;
	}

	@Override
	public void visit(Program program) {
		return;
	}
	
	private String getIndentation(){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < indentCnt; i++){
			sb.append("\t");
		}
		return sb.toString();
	}

}
