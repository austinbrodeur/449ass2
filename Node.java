
//Class representation for a Node object.
public class Node {
	
	//Class variables
	private final Lexer.Token token;
	private final Node parent, rightChild = null, leftChild = null;
	
	//Constructor
	public Node(Lexer.Token token, Node parent) {
		this.token = token;
		this.parent = parent;
	}
	
	//Creates the left child for this node.
	private createLeftChild(Lexer.Token token) {
		final leftChild = new Node(token, this);
	}
	
	//Creates the right child for this node.
	private createRightChild(Lexer.Token token) {
		final rightChild = new Node(token, this);
	}
	
	//Returns the left child of this node.
	private Node getLeftChild() {
		return leftChild;
	}
	
	//Returns the right child of this node.
	private Node getRightChild() {
		return rightChild;
	}
	
	//Returns the parent of this project.
	private Node getParent() {
		return parent;
	}
}
