package ast;

import lexer.Lexer;
import lexer.Token;

public class Keyword extends Statement{
	
	public String name;
	public int tokenId;
	public int outputId;
	public int waitTime;
	
	public Keyword(){
		this.name = "UNKNOWN";
	}
	
	public Keyword(String name){
		this.name = name;
	}
	
	public Keyword(String name, int tokenId, int outputId){
		this.name = name;
		this.tokenId = tokenId;
		this.outputId = outputId;
	}
	
	@Override
	public String toString() {
		return name;
	}

	@Override
	public ASTToken parse() {
		Token tok = Lexer.pop();
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
	}

	@Override
	public void acceptPostOrder(Visitor visitor) {
	}

	@Override
	public void acceptInOrder(Visitor visitor) {
	}
}
