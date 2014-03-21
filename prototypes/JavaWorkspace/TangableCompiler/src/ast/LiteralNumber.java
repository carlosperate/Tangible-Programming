package ast;

import lexer.Lexer;
import lexer.Token;
import core.LanguageDefinition.NumberTokenDefinition;

public class LiteralNumber extends Statement{
	
	public String name;
	public int tokenId;
	public int outputId;
	public int value;
	
	
	private final StringBuilder builder = new StringBuilder();
	
	public LiteralNumber(){
		this.name = "UNKNOWN";
	}
	
	public LiteralNumber(String name){
		this.name = name;
	}
	
	public LiteralNumber(NumberTokenDefinition definition){
		this.name = definition.name;
		this.outputId = definition.outputId;
		this.tokenId = definition.tokenId;
		this.value = definition.value;
	}
	
	@Override
	public ASTToken parse() {
		Token t = Lexer.getPeekNextToken();
		
		if(t instanceof LiteralNumber){
			do{
				t = Lexer.getPopNextToken();
				builder.append(((LiteralNumber)t).value);
				t = Lexer.getPeekNextToken();
				
			}while(t instanceof LiteralNumber);
			
			value = Integer.parseInt(builder.toString());
			
		}else{
			//TODO ERROR
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

	@Override
	public String toString() {
		return String.valueOf(value);
	}
	
	
}
