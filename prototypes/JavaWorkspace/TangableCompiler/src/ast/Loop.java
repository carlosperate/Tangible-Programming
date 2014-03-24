package ast;

import java.util.LinkedList;
import java.util.List;

import lexer.Lexer;
import lexer.Token;

public class Loop extends Statement{

	public final List<Statement> statements = new LinkedList<Statement>();
	
	public LiteralNumber numberCounter;
	
	public boolean blockEndFound = false;

	@Override
	public ASTToken parse() {

		// Remove loop start token
		Lexer.pop();

		Token next = Lexer.peek();
		
		if(next instanceof LiteralNumber){
			numberCounter = (LiteralNumber) new LiteralNumber().parse();
			next = Lexer.peek();
			while(!blockEndFound){
				statements.add(Program.parseNextStatement());
				next = Lexer.peek();
				
				// Test for end of loop block
				if(next instanceof Keyword){
					if(((Keyword)next).name.equals("Loop-End")){
						blockEndFound = true;
					}
				}
			}
		}else{
			System.out.println("ERROR");
		}


		Lexer.pop();

		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Loop Start " + numberCounter.toString() + "\n");

		for(Statement s : statements){
			sb.append("\t" + s + "\n");
		}

		sb.append("Loop End");

		return sb.toString();
	}

	@Override
	public void acceptPreOrder(Visitor visitor) {
		numberCounter.acceptPreOrder(visitor);
		visitor.visit(this);
	}

	@Override
	public void acceptPostOrder(Visitor visitor) {
		visitor.visit(this);
		numberCounter.acceptPreOrder(visitor);
	}

	@Override
	public void acceptInOrder(Visitor visitor) {
		numberCounter.acceptPreOrder(visitor);
		visitor.visit(this);
	}
}
