package lexer;

import ast.ASTToken;

public abstract interface Token {
	
	public abstract ASTToken parse();
	
}
