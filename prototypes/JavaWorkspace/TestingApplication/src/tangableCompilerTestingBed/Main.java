package tangableCompilerTestingBed;

import compilers.AnsiCGenerator;
import compilers.ArduinoGenerator;
import compilers.JavaGenerator;
import compilers.PythonGenerator;
import interpreter.InterpreterVisitor;
import lexer.Lexer;
import ast.Program;
import core.LanguageDefinition;
import exceptions.SyntaxException;

public class Main {

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
		try {
			result = Lexer.Lex("sampleProgram1.tang");
		} catch (SyntaxException e) {
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
		 * Generate python script from tangible application
		 */
		PythonGenerator python = new PythonGenerator("program");
		p.acceptInOrder(python);
		python.CloseSourceFile();
		
		/**
		 * Generate java script from tangible application
		 */
		JavaGenerator java = new JavaGenerator("program");
		p.acceptInOrder(java);
		java.CloseSourceFile();
		
		/**
		 * Generate ANSI-C script from tangible application
		 */
		AnsiCGenerator ansiC = new AnsiCGenerator("program");
		p.acceptInOrder(ansiC);
		ansiC.CloseSourceFile();
		
		/**
		 * Generate Arduino script from tangible application
		 */
		ArduinoGenerator ardiuno = new ArduinoGenerator("program");
		p.acceptInOrder(ardiuno);
		ardiuno.CloseSourceFile();
		
		/**
		 * Interpret the application 
		 */
		p.acceptPreOrder(new InterpreterVisitor());
		
	}
}
