package core;

import interpreter.InterpreterVisitor;
import lexer.Lexer;
import ast.Program;

/**
 * Main application entry point for TangableCompilerApplication test program
 * 
 * @author Paul Hickman
 * @version 1.0
 *
 */
public class TangableCompilerApplication {

	public static void main(String[] args){
		
		boolean result = true;

		/**
		 * Using the supplied xml file, create a language definition to define the syntax,
		 * and the links between the tokens and the application
		 */
		result = LanguageDefinition.getInstance().SetupLanguage("langdefinition.xml");
		if(!result){
			System.err.println("Language Setup Failed");
			return;
		}

		System.out.println("Language Generation Complete");

		/**
		 * Using the supplied .tang file, attempt to convert the primitive token ID's into
		 * valid tokens ready to send to the application
		 */
		result = Lexer.Lex("sampleProgram1.tang");
		if(!result){
			System.err.println("Lexing Failed");
			return;
		}

		System.out.println("Program Lexing Complete");
		
		
		/**
		 * Using the statically reference Lexer tokens, create a AST from the current token
		 * list
		 */
		Program p = new Program();
		p.parse();
		
		
		/**
		 * Send the interpreter using pre-order traversal on the AST.
		 */
		p.acceptPreOrder(new InterpreterVisitor());
	}
}
