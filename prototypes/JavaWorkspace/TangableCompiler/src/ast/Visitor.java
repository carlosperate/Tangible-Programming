package ast;


public interface Visitor {

	void visit(LiteralNumber number);
	void visit(Command cmd);
	void visit(Loop loop);
	void visit(Program program);
}
