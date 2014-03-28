package ast;

import java.util.LinkedList;
import java.util.List;
import lexer.Lexer;
import lexer.Token;

public class Program extends ASTToken{

	public final List<Statement> statements = new LinkedList<Statement>();

	@Override
	public ASTToken parse() {
		while(!Lexer.isEmpty()){
			statements.add(parseNextStatement());
		}
		return this;
	}

	public static Statement parseNextStatement(){
		Token next = Lexer.peek();

		if(next instanceof Command){
			return (Command) new Command().parse();
		}else if (next instanceof Keyword){
			if(((Keyword)next).name.equals("Loop-Start")){
				return (Loop) new Loop().parse();
			}
		}

		return null;
	}

	@Override
	public void acceptPreOrder(Visitor visitor) {
		visitor.visit(this);
		for(Statement st : statements)
			st.acceptPreOrder(visitor);
	}

	@Override
	public void acceptPostOrder(Visitor visitor) {
		visitor.visit(this);
		for(Statement st : statements)
			st.acceptPreOrder(visitor);
	}

	@Override
	public void acceptInOrder(Visitor visitor) {
		visitor.visit(this);
		for(Statement st : statements)
			st.acceptPreOrder(visitor);
	}


}
