package lexer;

import ast.ASTToken;


public enum Lexeme implements Token {
	
	LOOPSTART,
	LOOPEND;
	
	@Override
	public String toString(){
		switch(this){
		case LOOPEND:
			return "LoopEnd";
		case LOOPSTART:
			return "LoopStart";
		}
		return null;
	}

	@Override
	public ASTToken parse() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}