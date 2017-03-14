import java.util.ArrayList;
import java.util.Stack;

public class Parser
{
  public Node root = new Node();

  private Stack<Lexer.Token> parseStack = new Stack<Lexer.Token>();

  public Parser(ArrayList<Lexer.Token> tokens)
  {
    Parse(tokens);
  }

  public void Parse(ArrayList<Lexer.Token> tokens)
  {
    int tokensSize = (tokens.size()-1);
    Lexer.Token cToken;
    Node cNode = root;

    for (int i = 0; i <= tokensSize; i++){
      cToken = tokens.get(i);

      if (cToken.type.name().equals("ERROR")){
        System.err.println("Lex error. Unrecognized symbol \"" + cToken.data + "\"");
        System.exit(0);
      }
      if (cToken.type.name().equals("LPAR")){
        cNode.createLeftChild();
        cNode = cNode.leftChild;
        continue;
      }
      if (cToken.type.name().equals("DIGIT")){
        cNode.token = cToken;
        cNode = cNode.parent;
        continue;
      }
      if (cToken.type.name().equals("PLUS") || cToken.type.name().equals("MINUS")){
        cNode.token = cToken;
        cNode.createRightChild();
        cNode = cNode.rightChild;
        continue;
      }
      if (cToken.type.name().equals("RPAR")){
        cNode = cNode.parent;
      }
    }
    printTree();
  }

// For debugging purposes only
  public void printNode(Node cNode)
  {
    if (cNode.token != null){
      System.out.println(cNode.token.data);
      if (cNode.leftChild != null){
        printNode(cNode.leftChild);
      }
      if (cNode.rightChild != null){
        printNode(cNode.rightChild);
      }
    }
  }

  public void printTree()
  {
    printNode(root);
  }



// The following code is for the Node class only
  public class Node
  {
    public Node leftChild;
    public Node rightChild;
    public Node parent;
    public Lexer.Token token;

    public Node()
    {
    }

    public Node(Lexer.Token aToken){
      token = aToken;
    }

    public void createLeftChild()
    {
      leftChild = new Node();
      leftChild.parent = this;
    }

    public void createRightChild()
    {
      rightChild = new Node();
      rightChild.parent = this;
    }
  }
}
