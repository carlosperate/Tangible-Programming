package ast;

import lexer.Lexer;
import lexer.Token;
import core.LanguageDefinition.CommandTokenDefinition;

public class Command extends Statement{
	
	public String name;
	public int tokenId;
	public int outputId;
	public int waitTime;
	
	public Command(){
		this.name = "UNKNOWN";
	}
	
	public Command(String name){
		this.name = name;
	}
	
	public Command(CommandTokenDefinition definition){
		this.name = definition.name;
		this.outputId = definition.outputId;
		this.tokenId = definition.tokenId;
		this.waitTime = definition.waitTime;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public ASTToken parse() {
		Token tok = Lexer.getPopNextToken();
		if(tok instanceof Command){
			Command cmd = (Command)tok;
			name = cmd.name;
			tokenId = cmd.tokenId;
			outputId = cmd.outputId;
			waitTime = cmd.waitTime;
		}else{
			// TODO error
		}
		
		return this;
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
