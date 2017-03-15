
//Class representation for a Node object.
public class Node {

	//Class variables
	private final Lexer.Token token;
	private final Node parent, child = null;

	//Constructor
	public Node(Lexer.Token token, Node parent) {
		this.token = token;
		this.parent = parent;
	}

	//Creates the child for this node.
	public void createChild(Lexer.Token token) {
		child = new Node(token, this);
	}

	//Returns the child of this node.
	public Node getChild() {
		return child;
	}

	//Returns the parent of this node.
	public Node getParent() {
		return parent;
	}

	public void setToken(Lexer.Token token) {
		this.token = token;
	}
	
	//Return the token of this node.
	public Lexer.Token getToken() {
		return token;
	}
}
