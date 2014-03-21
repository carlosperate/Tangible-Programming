package ast;

public interface Visitiable {

	public void acceptPreOrder(Visitor visitor);
	public void acceptPostOrder(Visitor visitor);
	public void acceptInOrder(Visitor visitor);
}
