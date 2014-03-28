package compilers;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Stack;

import com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithm.WordListener;

import ast.Command;
import ast.LiteralNumber;
import ast.Loop;
import ast.Program;
import ast.Statement;
import ast.Visitor;

public class JavaGenerator implements Visitor{

	/**
	 * Project Stack. Stores variables and resolved sub branches of the AST
	 */
	private final Stack<Object> results = new Stack<Object>();

	/**
	 * Indentation counter to make the output more readable
	 */
	private int indentCnt = 0;
	
	/**
	 * Name of the python file to generate
	 */
	private String filename;
	
	/**
	 * Source file extension
	 */
	private String fileExtention = ".java";
	
	/**
	 * Current Symbol used for loops
	 */
	private char loopSymbol = 'a';
	
	/**
	 * File writer for the source file
	 */
	private PrintWriter writer;

	public JavaGenerator(String fileName){
		this.filename = fileName;
		try {
			writer = new PrintWriter(capitalize(filename) + fileExtention);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void visit(LiteralNumber number) {
		results.push(number.value);
	}

	@Override
	public void visit(Command cmd) {
		writer.write(getIndentation() + cmd.name + "();\n");
	}

	@Override
	public void visit(Loop loop) {
		int counter = (Integer)results.pop();
		writer.write(getIndentation() + "for(int " + loopSymbol + " = 0; " + loopSymbol + " < " + counter + "; " + loopSymbol + "++){\n");
		
		loopSymbol++;
		
		indentCnt++;
		
		for(Statement s : loop.statements){
			s.acceptInOrder(this);
		}

		indentCnt--;
	
		writer.write(getIndentation() + "}\n");
	}

	@Override
	public void visit(Program program) {
		writer.write(getIndentation() + "public class " + capitalize(filename) + "{\n\n");
		
		indentCnt++;
		
		writer.write(getIndentation() + "public static void main(String[] args){\n");
		indentCnt++;
		
		for(Statement s : program.statements){
			s.acceptInOrder(this);
		}
		
		indentCnt--;
		writer.write(getIndentation() + "}\n");
		
		indentCnt--;
		writer.write(getIndentation() + "}\n");
	}

	private String getIndentation(){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < indentCnt; i++){
			sb.append("\t");
		}
		return sb.toString();
	}
	
	private String capitalize(String line)
	{
	  return Character.toUpperCase(line.charAt(0)) + line.substring(1);
	}
	
	public void CloseSourceFile(){
		writer.close();
	}
}
