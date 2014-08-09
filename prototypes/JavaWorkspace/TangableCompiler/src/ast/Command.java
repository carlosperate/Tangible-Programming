package ast;

import lexer.Lexer;
import lexer.Token;

public class Command extends Statement{
	
	// Name of the token
	public String name;
	
	// Token ID - Internal use
	public int tokenId;
	
	// Output ID - External use (BoeBot)
	public int outputId;
	
	// Hardcoded wait time required after this command is sent
	public int waitTime;
	
	/**
	 * Default Constructor
	 */
	public Command(){
		this.name = "UNKNOWN";
	}
	
	/**
	 * Constructor
	 * @param name - Token Name
	 */
	public Command(String name){
		this.name = name;
	}
	
	/**
	 * Constructor
	 * @param name - Token Name
	 * @param tokenId -  Token ID - Internal
	 * @param outputId - Output ID - External
	 * @param waitTime - Wait time after command is sent
	 */
	public Command(String name, int tokenId, int outputId, int waitTime){
		this.name = name;
		this.tokenId = tokenId;
		this.outputId = outputId;
		this.waitTime = waitTime;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public ASTToken parse() {
		// Remove the generic token from the lexer
		Token tok = Lexer.pop();
		
		// Ensure the Token is a Command token
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
