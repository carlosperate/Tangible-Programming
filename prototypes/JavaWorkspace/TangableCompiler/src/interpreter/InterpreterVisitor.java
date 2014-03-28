package interpreter;

import java.util.Stack;

import ast.Command;
import ast.LiteralNumber;
import ast.Loop;
import ast.Program;
import ast.Statement;
import ast.Visitor;

/**
 * The interpreter for the application.
 * Utilises the visitor pattern to walk the AST to interpret the application
 * 
 * @author Paul Hickman
 *
 */
public class InterpreterVisitor implements Visitor{

	/**
	 * Project Stack. Stores variables and resolved sub branches of the AST
	 */
	private final Stack<Object> results = new Stack<Object>();
	
	/**
	 * Indentation counter to make the output more readable
	 */
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
		for(Statement s : program.statements)
			s.acceptPreOrder(this);
	}
	
	private String getIndentation(){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < indentCnt; i++){
			sb.append("\t");
		}
		return sb.toString();
	}

}
