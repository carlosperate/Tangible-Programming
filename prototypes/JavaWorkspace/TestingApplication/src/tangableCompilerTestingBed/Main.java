package tangableCompilerTestingBed;

import compilers.AnsiCGenerator;
import compilers.ArduinoGenerator;
import compilers.JavaGenerator;
import compilers.PythonGenerator;
import interpreter.InterpreterVisitor;
import lexer.Lexer;
import arena.Arena;
import arena.Robot;
import arena.SceneNode.Direction;
import ast.Program;
import core.LanguageDefinition;
import exceptions.SyntaxException;

public class Main {

	
	public final static String fileName = "sampleSquare.tang";
	
	public static void main(String[] args){

		boolean result = true;

		Arena arena = new Arena(10, 10, 1000, 1000, true);
	
		arena.addNode(new Robot(0, 0, Direction.NORTH, "A"));
		arena.addNode(new Robot(5, 5, Direction.NORTH, "B"));
		arena.addNode(new Robot(7, 3, Direction.NORTH, "C"));
		
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
		
		// Generated for compilers
		String fileNameWithoutExtension = fileName.substring(0, fileName.indexOf('.'));
		
		try {
			result = Lexer.Lex("sampleSquare.tang");
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
		PythonGenerator python = new PythonGenerator(fileNameWithoutExtension);
		p.acceptInOrder(python);
		python.CloseSourceFile();
		
		/**
		 * Generate java script from tangible application
		 */
		JavaGenerator java = new JavaGenerator(fileNameWithoutExtension);
		p.acceptInOrder(java);
		java.CloseSourceFile();
		
		/**
		 * Generate ANSI-C script from tangible application
		 */
		AnsiCGenerator ansiC = new AnsiCGenerator(fileNameWithoutExtension);
		p.acceptInOrder(ansiC);
		ansiC.CloseSourceFile();
		
		/**
		 * Generate Arduino script from tangible application
		 */
		ArduinoGenerator ardiuno = new ArduinoGenerator(fileNameWithoutExtension);
		p.acceptInOrder(ardiuno);
		ardiuno.CloseSourceFile();
		
		/**
		 * Interpret the application 
		 */
		p.acceptPreOrder(new InterpreterVisitor());
		
	}
}
