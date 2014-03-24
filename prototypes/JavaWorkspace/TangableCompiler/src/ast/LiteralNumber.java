package ast;

import lexer.Lexer;
import lexer.Token;

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
	
	public LiteralNumber(String name, int tokenId, int outputId, int value){
		this.name = name;
		this.tokenId = tokenId;
		this.outputId = outputId;
		this.value = value;
	}
	
	@Override
	public ASTToken parse() {
		Token t = Lexer.peek();
		
		if(t instanceof LiteralNumber){
			do{
				t = Lexer.pop();
				builder.append(((LiteralNumber)t).value);
				t = Lexer.peek();
				
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
