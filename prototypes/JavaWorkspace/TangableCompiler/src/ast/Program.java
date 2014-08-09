package ast;

import java.util.LinkedList;
import java.util.List;
import lexer.Lexer;
import lexer.Token;

/*
 * Represents a full Tangible application
 */
public class Program extends ASTToken{

	// List of resolved statements held within the application
	public final List<Statement> statements = new LinkedList<Statement>();

	@Override
	public ASTToken parse() {
		// Keep parsing the token until the lexer is empty
		while(!Lexer.isEmpty()){
			statements.add(parseNextStatement());
		}
		return this;
	}

	public static Statement parseNextStatement(){
		Token next = Lexer.peek();

		if(next instanceof Command){
			// Parse a new command
			return (Command) new Command().parse();
		}else if (next instanceof Keyword){
			// Parse a new Loop
			if(((Keyword)next).name.equals("Loop-Start")){
				return (Loop) new Loop().parse();
			}
		}

		return null;
	}

	@Override
	public void acceptPreOrder(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public void acceptPostOrder(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public void acceptInOrder(Visitor visitor) {
		visitor.visit(this);
	}


}
