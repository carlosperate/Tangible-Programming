package ast;

import java.util.LinkedList;
import java.util.List;

import lexer.Lexeme;
import lexer.Lexer;
import lexer.Token;

public class Loop extends Statement{

	public final List<Statement> statements = new LinkedList<Statement>();
	
	public LiteralNumber numberCounter;

	@Override
	public ASTToken parse() {

		// Remove loop start token
		Lexer.getPopNextToken();

		Token next = Lexer.getPeekNextToken();
		
		if(next instanceof LiteralNumber){
			numberCounter = (LiteralNumber) new LiteralNumber().parse();
			next = Lexer.getPeekNextToken();
			while(next != Lexeme.LOOPEND){
				statements.add(Program.parseNextStatement());
				next = Lexer.getPeekNextToken();
			}
		}else{
			System.out.println("ERROR");
		}


		Lexer.getPopNextToken();

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
