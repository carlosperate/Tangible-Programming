package lexer;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;

import ast.Command;
import ast.LiteralNumber;
import core.LanguageDefinition;
import core.LanguageDefinition.CommandTokenDefinition;
import core.LanguageDefinition.KeywordTokenDefinition;
import core.LanguageDefinition.NumberTokenDefinition;
import core.LanguageDefinition.TokenDefinition;

/**
 * @author Paul
 *
 */
public class Lexer {

	public static final Stack<Token> tokenList = new Stack<Token>();

	
	public static HashMap<String, Lexeme> registerKeywords = 
			new HashMap<String, Lexeme>();
	
	public static boolean Lex(String fileName){

		// Add keywords
		registerKeywords.put("Loop-Start", Lexeme.LOOPSTART);
		registerKeywords.put("Loop-End", Lexeme.LOOPEND);
		
		BufferedReader br = null;
		String tokenData = "";
		
		LanguageDefinition lang = LanguageDefinition.getInstance();
		
		try{
			br = new BufferedReader(new FileReader(fileName));

			while((tokenData = br.readLine()) != null){
				String[] tokens = tokenData.split(",");

				for(String s : tokens){

					TokenDefinition tokDef = lang.tokens.get(Integer.parseInt(s));
					
					if(tokDef != null){
						
						if(tokDef instanceof CommandTokenDefinition){
							tokenList.add(new Command((CommandTokenDefinition)tokDef));
						}else if(tokDef instanceof KeywordTokenDefinition){
							if(registerKeywords.containsKey(tokDef.name)){
								tokenList.add(registerKeywords.get(tokDef.name));
							}
							//TODO Error
						}else if(tokDef instanceof NumberTokenDefinition){
							tokenList.add(new LiteralNumber((NumberTokenDefinition)tokDef));
							
						}
					
					}else{
						//TODO Found token that does not exist in lang def
					}
				}
				
			}

			br.close();
		}catch(FileNotFoundException fnfe){
			System.err.println(fnfe.getMessage());
			return false;
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
			return false;
		}

		stackReversal(tokenList);

		return true;
	}

	private static void stackReversal(Stack<Token> s)
	{
		if(s.size() == 0) return;
		Token n = getLast(s);
		stackReversal(s);
		s.push(n);
	}
	
    private static Token getLast(Stack<Token> s)
    {
    	Token t = s.pop();
        if(s.size() == 0)
        {
            return t;
        }
        else
        {
        	Token k = getLast(s);
            s.push(t);
            return k;
        }
    }
	

	public static Token getPeekNextToken(){
		if(tokenList.isEmpty())
			return null;

		return tokenList.peek();
	}
	
	public static Token getPopNextToken(){
		if(tokenList.isEmpty())
			return null;

		return tokenList.pop();
	}
	
	public static Token lookahead(int n){
		if (!isEmpty()) {
			return tokenList.get(n-1); 
		}
		return null;
	}

	public static boolean isEmpty(){
		return tokenList.empty();
	}
}
